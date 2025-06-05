package com.ruoyi.galaxy.controller;

import com.dampcake.bencode.Bencode;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.galaxy.domain.*;
import com.ruoyi.galaxy.service.IGlxPeerService;
import com.ruoyi.galaxy.service.IGlxPointsRecordService;
import com.ruoyi.galaxy.service.IGlxTorrentAttachmentService;
import com.ruoyi.galaxy.service.IGlxTorrentPurchaseService;
import com.ruoyi.galaxy.service.impl.GlxBannedServiceImpl;
import com.ruoyi.galaxy.service.impl.GlxTorrentServiceImpl;
import com.ruoyi.galaxy.util.BitConvert;
import com.ruoyi.galaxy.util.ConfigUtil;
import com.ruoyi.galaxy.util.PointsUtil;
import com.ruoyi.galaxy.vo.AnnounceVO;
import com.ruoyi.system.service.ISysUserService;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import java.lang.Boolean;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/announce")
public class TrackerAPIController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TrackerAPIController.class);

    @Autowired
    private GlxBannedServiceImpl bannedService;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private GlxTorrentServiceImpl torrentService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IGlxPeerService peerService;

    @Autowired
    private IGlxTorrentPurchaseService torrentPurchaseService;

    @Autowired
    private IGlxPointsRecordService pointsRecordService;

    @Autowired
    private IGlxTorrentAttachmentService attachmentService;

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
    public boolean checkCheating (AnnounceVO announceVO, SysUser user, String infoHash, GlxPeer peer) {
        if (peer == null) return false;
        String banMsg = null;
        int banTime = 0;
        if (announceVO.getDownloaded() < 0 || announceVO.getUploaded() < 0) {
            banMsg = "[CHEAT] 用户令牌: " + announceVO.getToken() + " 节点编号: " + announceVO.getPeer_id() + " 下载或上传数据不正常: " + announceVO.getDownloaded() + ":" + announceVO.getUploaded();
            banTime = 6;
        }
        List<GlxPeer> peers = peerService.selectGlxPeerByInfoHash(infoHash);
        long totalDownloadSpeed = 0;
        long totalUploadSpeed = 0;
        long tsp = (new Date().getTime() - peer.getUpdateTime().getTime()) / 1000;
        long uploadSpeed = (announceVO.getUploaded() - peer.getUploaded()) / tsp;
        long downloadSpeed = (announceVO.getDownloaded() - peer.getDownloaded()) / tsp;
        for (GlxPeer p : peers) {
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
        //检查客户端标识
        if (StringUtils.isEmpty(announceVO.getPeer_id())) {
            return error("ERROR_PEER_ID", announceVO);
        }
        //检查用户凭证
        if (!StringUtils.isNotEmpty(announceVO.getToken())) {
            return error("ERROR_ACCESS_TOKEN", announceVO);
        }

        //通过用户凭证获取用户信息
        SysUser user = userService.selectUserByToken(announceVO.getToken());
        if (user == null) {
            return error("ERROR_ACCESS_TOKEN_NOT_EXISTS", announceVO);
        }
        if (user.isBan()) {
            return error("ERROR_BANDED", announceVO);
        }

        //传过来的url
        String queryString = request.getQueryString();
        System.out.println(queryString);

        //从 URL 参数提取并解码资源标识
        String infoHash = queryString.substring(queryString.indexOf("info_hash=") + 10);
        infoHash = infoHash.substring(0, infoHash.indexOf("&"));
        infoHash = BitConvert.byteArrayToHexString(UriUtils.decode(infoHash, "ISO-8859-1").getBytes("ISO-8859-1")).toLowerCase();

        //获取种子附件信息
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


        GlxTorrent torrent = null;
        GlxTorrentAttachment attachment = null;
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


        //种子购买
        GlxTorrentPurchase glxTorrentPurchase = null;
        if (!torrent.getUserId().equals(user.getUserId())) {
            GlxTorrentPurchase filter = new GlxTorrentPurchase();
            filter.setUserId(user.getUserId());
            filter.setTorrentId(torrent.getId());
            List<GlxTorrentPurchase> torrentPurchaseList = torrentPurchaseService.selectGlxTorrentPurchaseList(filter);

            if (torrentPurchaseList.size() == 0) {
                return error("ERROR_TORRENT_NOT_PURCHASE", announceVO);
            } else {
                glxTorrentPurchase = torrentPurchaseList.get(0);
            }
        }


        GlxPeer peer = peerService.selectGlxPeerByPeerIdAndInfoHash(announceVO.getPeer_id(), infoHash);
        if (checkCheating(announceVO, user, infoHash, peer)) {
            return error("ERROR_CHEATER", announceVO);
        }

        // 删除停止的Peer
        if (announceVO.getEvent()!= null && announceVO.getEvent().equals("stopped")) {
            //删除已停止的节点
            if (peer != null && peer.getId() != null) {
                peerService.deleteGlxPeerById(peer.getId());
                return error("Bye", announceVO);
            }
            return error("Bye", announceVO);
        }

        // 新建Peer记录
        if (peer == null) {
            peer = new GlxPeer();
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
//        GlxPeer lastData = redisCache.getCacheObject(peer.getPeerId());
        GlxPeer lastData = peerService.selectGlxPeerByPeerId(peer.getPeerId());
        boolean shouldUpdatePurchaseInfo = false;
        if (glxTorrentPurchase != null && glxTorrentPurchase.getStartTime() == null) {
            glxTorrentPurchase.setStartTime(new Date());
            shouldUpdatePurchaseInfo = true;
        }
        if (announceVO.getEvent() != null && glxTorrentPurchase != null) {
            if (announceVO.getEvent().equals("completed") && glxTorrentPurchase.getFinishTime() == null) {
                glxTorrentPurchase.setFinishTime(new Date());
                peer.setLeftSize(0L);
                long t = (glxTorrentPurchase.getFinishTime().getTime() - glxTorrentPurchase.getStartTime().getTime());
                if (t > 0) {
                    glxTorrentPurchase.setAvgSpeed(torrent.getFileSize() / t);
                    shouldUpdatePurchaseInfo = true;
                }
            } else if (announceVO.getEvent().equals("started") && glxTorrentPurchase.getStartTime() == null) {
                glxTorrentPurchase.setStartTime(new Date());
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

            // 1. 速度计算
            long tsp = (new Date().getTime() - lastData.getUpdateTime().getTime()) / 1000;
            long downloaded = peer.getDownloaded() - lastData.getDownloaded();
            if (downloaded > 0) {
                peer.setDownloadSpeed(downloaded / tsp); // 实时下载速度

                // 2. 资源及用户数据更新
                torrent.setDownloaded(torrent.getDownloaded() + downloaded);
                // @todo 更新最大下载速度,需要写入数据库及事件处理
                if (glxTorrentPurchase != null && glxTorrentPurchase.getMaxSpeed() == null) {
                    glxTorrentPurchase.setMaxSpeed(0L);
                }
                if (glxTorrentPurchase != null && glxTorrentPurchase.getMaxSpeed() < peer.getDownloadSpeed()) {
                    glxTorrentPurchase.setMaxSpeed(peer.getDownloadSpeed());
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
                user.setUploaded(uploaded);
                user.setDownloaded(downloaded);
                userService.updateUserCounter(user);
            }

            double p = 0d;
            if (attachment != null) {
                GlxPeer filter = new GlxPeer();
                filter.setUserId(user.getUserId());
                filter.setAttachmentId(attachment.getId());
                p = pointsUtil.countUserPoint(attachment.getCreateTime(), (double) attachment.getTorrentSize(), peerService.selectGlxPeerList(filter).size());
            } else {
                List<GlxPeer> pp = peerService.selectGlxPeerByTorrentId(torrent.getId()).stream().filter(x -> { return x.getAttachmentId() == null; }).collect(Collectors.toList());
                p = pointsUtil.countUserPoint(torrent.getCreateTime(), (double) torrent.getFileSize(), pp.size());
            }


            // 3. 积分奖励（做种激励）
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
                    GlxPointsRecord pointsRecord = new GlxPointsRecord();
                    pointsRecord.setPeerId(peer.getId());
                    pointsRecord.setPoints((double)record.get("point"));
                    pointsRecord.setTorrentId(torrent.getId());
                    pointsRecord.setUserId(user.getUserId());
                    pointsRecord.setRemark("[累计]做种奖励");
                    pointsRecordService.insertGlxPointsRecord(pointsRecord);
                    record.put("expire", new Date().getTime() / 1000 + 3600);
                    record.put("point",0d);
                }
                // 缓存累计
                redisCache.setCacheMap(pk, record);
                // 日志记录
                System.out.println("[" + user.getUserName() + "(" + user.getUserId() + ")]同步时间差: " + tsp + " 上传量: " + uploaded + " 下载量: " + downloaded + " 奖励分: " + points + " 奖池: " + remain);
            }
        }
        redisCache.setCacheObject(peer.getPeerId(), peer);

        if (shouldUpdatePurchaseInfo) {
            torrentPurchaseService.updateGlxTorrentPurchase(glxTorrentPurchase);
        }

        peerService.updateGlxPeer(peer);
        HashMap<Object, Object> resp = new HashMap<>();
        List<HashMap<String, Object>> peers = new ArrayList<>();

        List<GlxPeer> peerList = peerService.selectGlxPeerByInfoHash(infoHash);
        if (peerList.size() > announceVO.getNumwant()) {
            if (announceVO.getNumwant() > 0) {
                peerList = peerList.subList(0, announceVO.getNumwant());
            }
        }
        for (GlxPeer p : peerList) {
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

        // 下次汇报间隔
        resp.put("interval", ConfigUtil.REPORT_TIME);

        // 可连接Peer列表
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
