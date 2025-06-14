package com.ruoyi.galaxy.controller;

import com.dampcake.bencode.Bencode;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.galaxy.domain.*;
import com.ruoyi.galaxy.service.IBitPeerService;
import com.ruoyi.galaxy.service.IBitPointsRecordService;
import com.ruoyi.galaxy.service.IBitTorrentAttachmentService;
import com.ruoyi.galaxy.service.IBitTorrentPurchaseService;
import com.ruoyi.galaxy.service.impl.BitBannedServiceImpl;
import com.ruoyi.galaxy.service.impl.BitTorrentServiceImpl;
import com.ruoyi.galaxy.util.BitConvert;
import com.ruoyi.galaxy.util.ConfigUtil;
import com.ruoyi.galaxy.util.PointsUtil;
import com.ruoyi.galaxy.vo.AnnounceVO;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/announce")
public class TrackerAPIController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TrackerAPIController.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private BitBannedServiceImpl bannedService;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private BitTorrentServiceImpl torrentService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBitPeerService peerService;

    @Autowired
    private IBitTorrentPurchaseService torrentPurchaseService;

    @Autowired
    private IBitPointsRecordService pointsRecordService;

    @Autowired
    private IBitTorrentAttachmentService attachmentService;

    @Autowired
    private PointsUtil pointsUtil;

    public void saveBanToRedis(SysUser user, Integer day, String reason) {
        String key = "ban_user_" + user.getUserId();
        redisCache.setCacheObject(key, true, day * 86400, TimeUnit.SECONDS);
    }

    public void banded(SysUser user, Integer hour, String reason) {
        bannedService.Ban(user, hour, reason);
    }

    public boolean checkBanded (SysUser user) {
        String key = "ban_user_" + user.getUserId();
        boolean banded = redisCache.getCacheObject(key) != null;
        if (!banded && user.getBanExpire() != null) {
            long banTime = (user.getBanExpire().getTime() - DateUtils.getNowDate().getTime()) / 1000;
            if (banTime > 0) {
                saveBanToRedis(user, (int)(banTime / 86400), user.getBanReason());
            }
        }
        return banded;
    }

    private String getIp() {
        String[] ips = IpUtils.getIpAddr(request).split(",");
        return ips[0].trim();
    }
    /**
     * 作弊检测
     * @param announceVO
     * @return
     */
    public boolean checkCheating (AnnounceVO announceVO, SysUser user, String infoHash, BitPeer peer) {
        if (peer == null) return false;
        String banMsg = null;
        int banTime = 0;
        if (announceVO.getDownloaded() < 0 || announceVO.getUploaded() < 0) {
            banMsg = "[CHEAT] 用户令牌: " + announceVO.getToken() + " 节点编号: " + announceVO.getPeer_id() + " 下载或上传数据不正常: " + announceVO.getDownloaded() + ":" + announceVO.getUploaded();
            banTime = 6;
        }
        List<BitPeer> peers = peerService.selectGlxPeerByInfoHash(infoHash);
        long totalDownloadSpeed = 0;
        long totalUploadSpeed = 0;
        long tsp = (new Date().getTime() - peer.getUpdateTime().getTime()) / 1000;
        long uploadSpeed = (announceVO.getUploaded() - peer.getUploaded()) / tsp;
        long downloadSpeed = (announceVO.getDownloaded() - peer.getDownloaded()) / tsp;
        for (BitPeer p : peers) {
            if (p.getUserId() != user.getUserId()) {
                totalDownloadSpeed += p.getDownloadSpeed();
                totalUploadSpeed += p.getUploadSpeed();
            }
        }
        if (uploadSpeed > totalDownloadSpeed) {
            banTime += 12;
            banMsg = "数据异常：实际上传速度 " + uploadSpeed + " 全局下载速度：" + totalDownloadSpeed;
        }
        if (downloadSpeed > totalUploadSpeed) {
            banTime += 12;
            banMsg = "数据异常：实际下载速度 " + downloadSpeed + " 全局上传速度：" + totalUploadSpeed;
        }
        if (banTime > 0) {
            log.error(banMsg);
            banded(user, banTime, banMsg);
            return true;
        }
        return false;
    }

    private void lockPeer(AnnounceVO announceVO) {
        String peerLocker = "peer_locker_" + announceVO.getPeer_id();
        redisCache.setCacheObject(peerLocker, true, 3, TimeUnit.SECONDS);
    }

    private void unlockPeer(AnnounceVO announceVO) {
        String peerLocker = "peer_locker_" + announceVO.getPeer_id();
        redisCache.deleteObject(peerLocker);
    }

    private boolean checkPeerLocker(AnnounceVO announceVO) {
        String peerLocker = "peer_locker_" + announceVO.getPeer_id();
        return redisCache.getCacheObject(peerLocker) != null;
    }

    @GetMapping
    public String announce(AnnounceVO announceVO) throws UnsupportedEncodingException {
//        if (checkPeerLocker(announceVO)) {
//            return "";
//        }
//        lockPeer(announceVO);
        SimpleLogger logger = new SimpleLogger();


        if (StringUtils.isEmpty(announceVO.getPeer_id())) {
            return error("ERROR_PEER_ID", announceVO);
        }
        if (!StringUtils.isNotEmpty(announceVO.getToken())) {
            return error("ERROR_ACCESS_TOKEN", announceVO);
        }
        SysUser user = userService.selectUserByToken(announceVO.getToken());
        if (user == null) {
            return error("ERROR_ACCESS_TOKEN_NOT_EXISTS", announceVO);
        }
        if (user.isBan()) {
            return error("ERROR_BANDED", announceVO);
        }

        String queryString = request.getQueryString();
        System.out.println(queryString);
        String infoHash = queryString.substring(queryString.indexOf("info_hash=") + 10);
        infoHash = infoHash.substring(0, infoHash.indexOf("&"));
        infoHash = BitConvert.byteArrayToHexString(UriUtils.decode(infoHash, "ISO-8859-1").getBytes("ISO-8859-1")).toLowerCase();
        String torrentInfoHash = null;
        String attachmentInfoHash = null;
        if (announceVO.getType() == null) {
            announceVO.setType("torrent");
        }
        if (announceVO.getType().equals("attachment")) {
            attachmentInfoHash = infoHash;
        } else {
            torrentInfoHash = infoHash;
        }

        if (StringUtils.isEmpty(torrentInfoHash) && StringUtils.isEmpty(attachmentInfoHash)) {
            return error("ERROR_TORRENT", announceVO);
        }
        BitTorrent torrent = null;
        BitTorrentAttachment attachment = null;
        if (StringUtils.isNotEmpty(attachmentInfoHash)) {
            attachment = attachmentService.selectGlxTorrentAttachmentByInfoHash(attachmentInfoHash);
            if (attachment == null) {
                return error("ERROR_ATTACHMENT_NOT_FOUND", announceVO);
            } else {
                torrent = torrentService.selectGlxTorrentById(attachment.getTorrentId());
                torrentInfoHash = torrent.getInfoHash();
            }
        } else {
            torrent = torrentService.selectGlxTorrentByInfoHash(torrentInfoHash);
        }
        if (torrent == null) {
            return error("ERROR_TORRENT_NOT_EXISTS", announceVO);
        }
        BitTorrentPurchase bitTorrentPurchase = null;
        if (!torrent.getUserId().equals(user.getUserId())) {
            BitTorrentPurchase filter = new BitTorrentPurchase();
            filter.setUserId(user.getUserId());
            filter.setTorrentId(torrent.getId());
            List<BitTorrentPurchase> torrentPurchaseList = torrentPurchaseService.selectGlxTorrentPurchaseList(filter);

            if (torrentPurchaseList.size() == 0) {
                return error("ERROR_TORRENT_NOT_PURCHASE", announceVO);
            } else {
                bitTorrentPurchase = torrentPurchaseList.get(0);
            }
        }
        BitPeer peer = peerService.selectGlxPeerByPeerIdAndInfoHash(announceVO.getPeer_id(), infoHash);
//        if (checkCheating(announceVO, user, infoHash, peer)) {
//            return error("ERROR_CHEATER", announceVO);
//        }
        if (announceVO.getEvent()!= null && announceVO.getEvent().equals("stopped")) {
            //删除已停止的节点
            if (peer != null && peer.getId() != null) {
                peerService.deleteGlxPeerById(peer.getId());
                return error("Bye", announceVO);
            }
            return error("Bye", announceVO);
        }
        if (peer == null) {
            peer = new BitPeer();
            peer.setUserId(user.getUserId());
            peer.setInfoHash(infoHash);
            peer.setPeerId(announceVO.getPeer_id());
            peer.setIp(getIp());
            peer.setTorrentId(torrent.getId());
            if (attachment != null) {
                peer.setAttachmentId(attachment.getId());
            }
            peerService.insertGlxPeer(peer);
        }
        if (attachment != null) {
            peer.setAttachmentId(attachment.getId());
        }
        if (announceVO.getPort() > 0) {
            peer.setPort(announceVO.getPort());
        }
        if (announceVO.getUploaded()  > 0) {
            peer.setUploaded(announceVO.getUploaded());
        }
        if (announceVO.getDownloaded()  > 0) {
            peer.setDownloaded(announceVO.getDownloaded());
        }
        if (announceVO.getLeft()  > 0) {
            peer.setLeftSize(announceVO.getLeft());
        }
        if (announceVO.getKey() != null) {
            peer.setKey(announceVO.getKey());
        }
        if (announceVO.getEvent() != null) {
            peer.setEvent(announceVO.getEvent());
        }
        if (!peer.getIp().equalsIgnoreCase(getIp())) {
            peer.setIp(getIp());
        }
        peer.setDownloadSpeed(0L);
        peer.setTorrentId(torrent.getId());
        peer.setUpdateTime(new Date());
//        BitPeer lastData = redisCache.getCacheObject(peer.getPeerId());
        BitPeer lastData = peerService.selectGlxPeerByPeerId(peer.getPeerId());
        boolean shouldUpdatePurchaseInfo = false;
        if (bitTorrentPurchase != null && bitTorrentPurchase.getStartTime() == null) {
            bitTorrentPurchase.setStartTime(new Date());
            shouldUpdatePurchaseInfo = true;
        }
        if (announceVO.getEvent() != null && bitTorrentPurchase != null) {
            if (announceVO.getEvent().equals("completed") && bitTorrentPurchase.getFinishTime() == null) {
                bitTorrentPurchase.setFinishTime(new Date());
                peer.setLeftSize(0L);
                long t = (bitTorrentPurchase.getFinishTime().getTime() - bitTorrentPurchase.getStartTime().getTime());
                if (t > 0) {
                    bitTorrentPurchase.setAvgSpeed(torrent.getFileSize() / t);
                    shouldUpdatePurchaseInfo = true;
                }
            } else if (announceVO.getEvent().equals("started") && bitTorrentPurchase.getStartTime() == null) {
                bitTorrentPurchase.setStartTime(new Date());
                shouldUpdatePurchaseInfo = true;
            }
        }
        if (lastData != null && lastData.getUpdateTime() != null) {
            if (peer.getDownloaded() == null) {
                peer.setDownloaded(0L);
            }
            if (peer.getUploaded() == null) {
                peer.setUploaded(0L);
            }
            if (peer.getUploaded() == null) {
                peer.setUploaded(0L);
            }
            if (lastData.getUploaded() == null) {
                lastData.setUploaded(0L);
            }
            if (lastData.getDownloaded() == null) {
                lastData.setDownloaded(0L);
            }
            long tsp = (new Date().getTime() - lastData.getUpdateTime().getTime()) / 1000;
            long downloaded = peer.getDownloaded() - lastData.getDownloaded();
            if (downloaded > 0) {
                peer.setDownloadSpeed(downloaded / tsp);
                torrent.setDownloaded(torrent.getDownloaded() + downloaded);
                // @todo 更新最大下载速度,需要写入数据库及事件处理
                if (bitTorrentPurchase != null && bitTorrentPurchase.getMaxSpeed() == null) {
                    bitTorrentPurchase.setMaxSpeed(0L);
                }
                if (bitTorrentPurchase != null && bitTorrentPurchase.getMaxSpeed() < peer.getDownloadSpeed()) {
                    bitTorrentPurchase.setMaxSpeed(peer.getDownloadSpeed());
                    shouldUpdatePurchaseInfo = true;
                }
            }
            long uploaded = peer.getUploaded() - lastData.getUploaded();
            if (uploaded > 0) {
                peer.setUploadSpeed(uploaded / tsp);
                torrent.setUploaded(torrent.getUploaded() + uploaded);
            }
            if (uploaded > 0 || downloaded > 0) {
                if (user.getUploaded() == null) {
                    user.setUploaded(0L);
                }
                if (user.getDownloaded() == null) {
                    user.setDownloaded(0L);
                }
                if (attachment == null) {
                    torrentService.updateGlxTorrentCounter(torrent);
                } else {
                    attachment.setUploaded(uploaded);
                    attachment.setDownloaded(downloaded);
                    attachmentService.updateGlxTorrentAttachmentTransmission(attachment);
                }
                if(uploaded < 0){
                    uploaded = 0;
                }
                if(downloaded < 0){
                    downloaded = 0;
                }
                double ratio = 0;
                if(user.getDownloaded()!=0){
                    ratio = (double) user.getUploaded() / (double) user.getDownloaded();
                }else{
                    ratio = 10;
                }
                double point_ratio = 0.00000001;

                if(ratio < 0.5) {
                    if(uploaded!=0){
                        user.setUploaded(uploaded);
                    }
                    if(downloaded!=0){
                        user.setDownloaded((long)(downloaded*1.2));
                    }
                    user.setPoints(user.getPoints() + uploaded*point_ratio);
                    SimpleLogger.log("用户" + user.getUserId() + "低活跃用户：上传" + uploaded + " 下载" + (long)(downloaded*1.2));
                }else if(ratio >= 0.5 && ratio < 1.5) {
                    if(uploaded!=0){
                        user.setUploaded(uploaded);
                    }
                    if(downloaded!=0){
                        user.setDownloaded((long)(downloaded));
                    }
                    user.setPoints(user.getPoints() + uploaded*point_ratio);
                    SimpleLogger.log("用户" + user.getUserId() + "正常用户：上传" + uploaded + " 下载" + downloaded);
                }else{
                    if(uploaded!=0){
                        user.setUploaded(uploaded);
                    }
                    if(downloaded!=0){
                        user.setDownloaded((long)(downloaded*0.8));
                    }
                    user.setPoints(user.getPoints() + uploaded*point_ratio);
                    SimpleLogger.log("用户" + user.getUserId() + "高活跃用户：上传" + uploaded + " 下载" + (long)(downloaded*0.8));
                }
                SimpleLogger.log("积分增加" + uploaded*point_ratio);
                SimpleLogger.log("用户积分" + user.getPoints());
                userService.updateUserCounter(user);
                userMapper.safeUpdateUser(user);
            }

            double p = 0d;
            if (attachment != null) {
                BitPeer filter = new BitPeer();
                filter.setUserId(user.getUserId());
                filter.setAttachmentId(attachment.getId());
                p = pointsUtil.countUserPoint(attachment.getCreateTime(), (double) attachment.getTorrentSize(), peerService.selectGlxPeerList(filter).size());
            } else {
                List<BitPeer> pp = peerService.selectGlxPeerByTorrentId(torrent.getId()).stream().filter(x -> { return x.getAttachmentId() == null; }).collect(Collectors.toList());
                p = pointsUtil.countUserPoint(torrent.getCreateTime(), (double) torrent.getFileSize(), pp.size());
            }

            double points = p / 3600 * tsp;//uploaded / ConfigUtil.TRANSMISSION_UNIT;
            if (points > 0 && announceVO.getLeft() == 0) {
                //下载完成之后才可以
                if (torrent.getUserId().equals(user.getUserId())) {
                    points *= 2;
                }
                String pk = "user_point_counter_" + user.getUserId();
                Map<String, Object> record = redisCache.getCacheMap(pk);
                if (!record.containsKey("point")) {
                    record.put("point", 0d);
                }
                if (!record.containsKey("expire")) {
                    record.put("expire", new Date().getTime() / 1000 + 3600);
                }
                double remain = (double)record.get("point");
                record.put("point", remain + points);
                if ((long)record.get("expire") < new Date().getTime() / 1000) {
                    BitPointsRecord pointsRecord = new BitPointsRecord();
                    pointsRecord.setPeerId(peer.getId());
                    pointsRecord.setPoints((double)record.get("point"));
                    pointsRecord.setTorrentId(torrent.getId());
                    pointsRecord.setUserId(user.getUserId());
                    pointsRecord.setRemark("[累计]做种奖励");
                    pointsRecordService.insertGlxPointsRecord(pointsRecord);
                    record.put("expire", new Date().getTime() / 1000 + 3600);
                    record.put("point",0d);
                }
                redisCache.setCacheMap(pk, record);
                System.out.println("[" + user.getUserName() + "(" + user.getUserId() + ")]同步时间差: " + tsp + " 上传量: " + uploaded + " 下载量: " + downloaded + " 奖励分: " + points + " 奖池: " + remain);
            }
        }
        redisCache.setCacheObject(peer.getPeerId(), peer);

        if (shouldUpdatePurchaseInfo) {
            torrentPurchaseService.updateGlxTorrentPurchase(bitTorrentPurchase);
        }

        peerService.updateGlxPeer(peer);
        HashMap<Object, Object> resp = new HashMap<>();
        List<HashMap<String, Object>> peers = new ArrayList<>();

        List<BitPeer> peerList = peerService.selectGlxPeerByInfoHash(infoHash);
        if (peerList.size() > announceVO.getNumwant()) {
            if (announceVO.getNumwant() > 0) {
                peerList = peerList.subList(0, announceVO.getNumwant());
            }
        }
        for (BitPeer p : peerList) {
            if (p.getTorrentId() == null) {
                continue;
            }
            if (!p.getPeerId().equals(peer.getPeerId()) && p.getTorrentId().equals(peer.getTorrentId()) && p.getPort() != null) {
                HashMap<String, Object> peerMap = new HashMap<>();
                peerMap.put("ip", p.getIp());
                peerMap.put("port", p.getPort());
                peerMap.put("peer_id", p.getPeerId());
                peers.add(peerMap);
            }
        }
        resp.put("interval", ConfigUtil.REPORT_TIME);
        resp.put("peers", peers);
        return response(resp, announceVO);
    }


    private String response(HashMap<Object, Object> data, AnnounceVO announceVO) {
//        unlockPeer(announceVO);
        Bencode bencode = new Bencode();
        return new String(bencode.encode(data), bencode.getCharset());
    }

    private String error(String msg, AnnounceVO announceVO) {
        return response(new HashMap<Object, Object>(){
            {
                put("failure reason", new HashMap<String, String>(){
                    {
                        put("type", "string");
                        put("value", msg);
                    }
                });
            }
        }, announceVO);
    }
}


class SimpleLogger {

    // 日志文件路径（相对路径）
    private static final String LOG_DIR = "logger";

    // 日志文件名
    private static final String LOG_FILE = "application.log";

    // 日期格式化
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日志记录方法
     * @param message 要记录的日志消息
     */
    public static void log(String message) {
        // 确保日志目录存在
        ensureLogDirectory();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLogFilePath(), true))) {
            // 创建时间戳
            String timestamp = "[" + dateFormat.format(new Date()) + "]";

            // 写入日志
            writer.write(timestamp + " " + message);
            writer.newLine();

            // 同时在控制台输出
            System.out.println(timestamp + " " + message);
        } catch (IOException e) {
            System.err.println("日志写入失败: " + e.getMessage());
        }
    }

    /**
     * 确保日志目录存在
     */
    private static void ensureLogDirectory() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                System.out.println("创建日志目录: " + LOG_DIR);
            } else {
                System.err.println("无法创建日志目录: " + LOG_DIR);
            }
        }
    }

    /**
     * 获取日志文件完整路径
     */
    private static String getLogFilePath() {
        return LOG_DIR + File.separator + LOG_FILE;
    }

}