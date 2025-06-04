package com.ruoyi.galaxy.controller;

import be.christophedetroyer.bencoding.Utils;
import be.christophedetroyer.torrent.Torrent;
import be.christophedetroyer.torrent.TorrentParser;
import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.BencodeInputStream;
import com.dampcake.bencode.BencodeOutputStream;
import com.dampcake.bencode.Type;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.galaxy.domain.*;
import com.ruoyi.galaxy.service.*;
import com.ruoyi.galaxy.util.ConfigUtil;
import com.ruoyi.galaxy.util.PointsUtil;
import com.ruoyi.galaxy.vo.AttachmentVO;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/piazza")
public class IndexAPIController extends BaseController {
    @Autowired
    private IGlxTorrentService glxTorrentService;

    @Autowired
    private IGlxTorrentPurchaseService torrentPurchaseService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IGlxPointsRecordService pointsRecordService;

    @Autowired
    private IGlxPeerService peerService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private PointsUtil pointsUtil;

    @Autowired
    private IGlxTorrentAttachmentService attachmentService;

    @Autowired
    private ISysConfigService configService;

    public IndexAPIController() {}

    @GetMapping("/reward")
    public AjaxResult pointsReward () {
        DecimalFormat df = new DecimalFormat(".000");
        AjaxResult result = AjaxResult.success();
        List<GlxPeer> peers = peerService.selectGlxPeerList(new GlxPeer() {{
            setUserId(getUser().getUserId());
            setLeftSize(0L);
        }});
        double pointsPreHour = 0d;
        List<Map<String, Object>> details = new ArrayList<>();
        for(int i=0;i<peers.size();i++) {
            GlxPeer x = peers.get(i);
            GlxTorrent torrent = null;
            GlxTorrentAttachment attachment = null;
            double p = 0d;
            double rewardRate = 1d;
            Map<String, Object> item = new HashMap<>();
            if (x.getAttachmentId() != null) {
                GlxPeer filter = new GlxPeer();
                filter.setUserId(getUser().getUserId());
                filter.setAttachmentId(x.getAttachmentId());
                attachment = attachmentService.selectGlxTorrentAttachmentById(x.getAttachmentId());
                if (attachment != null) {
                    p = pointsUtil.countUserPoint(attachment.getCreateTime(), (double)attachment.getTorrentSize(), peerService.selectGlxPeerList(filter).size());
                    item.put("torrent_title", attachment.getTitle());
                    item.put("torrent_infohash", attachment.getInfoHash());
                    item.put("torrent_size", attachment.getTorrentSize());
                    item.put("attachment", true);
                    if (attachment.getUserId() == getUser().getUserId()) {
                        rewardRate = 2;
                    }
                } else {
                    item = null;
                }
            } else {
                torrent = glxTorrentService.selectGlxTorrentById(x.getTorrentId());
                if (torrent != null) {
                    p = pointsUtil.countUserPoint(torrent.getCreateTime(), (double)torrent.getFileSize(), peerService.selectGlxPeerByTorrentId(torrent.getId()).stream().filter(y -> { return y.getAttachmentId() == null; }).collect(Collectors.toList()).size());
                    item.put("torrent_title", torrent.getTitle());
                    item.put("torrent_infohash", torrent.getInfoHash());
                    item.put("torrent_size", torrent.getFileSize());
                    if (torrent.getUserId() == getUser().getUserId()) {
                        rewardRate = 2;
                    }
                } else {
                    item = null;
                }

            }
            if (item != null) {
                p *= rewardRate;
                item.put("torrent_points", p);
                item.put("reward_rate", rewardRate);
                details.add(item);
                pointsPreHour += p;
            }
        }
        result.put("peers", details);
        result.put("points_ph", Double.parseDouble(df.format(pointsPreHour)));
        return result;
    }

    private Torrent getTorrent(String torrentPath) throws IOException {
        torrentPath = uploadPathToLocal(torrentPath);
        Torrent torrent = TorrentParser.parseTorrent(torrentPath);
        torrent.setPieces(null);
        torrent.setPiecesBlob(null);
        return torrent;
    }

    private GlxTorrent setInfoSecret(GlxTorrent torrent) {
        torrent.setCreateBy(null);
        torrent.setUploadToken(null);
        torrent.setInfoHash(null);
        torrent.setFileName(null);
        torrent.setDownloadToken(null);
        torrent.setUserId(null);
        if (torrent.getTorrent() != null) {
            Torrent t = torrent.getTorrent();
            t.setInfo_hash(null);
            t.setAnnounce(null);
            t.setAnnounceList(null);
            torrent.setTorrent(t);
        }
        return torrent;
    }

    @GetMapping("/notice")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) throws Exception {
        String cacheKey = "torrent_" + id;
        GlxTorrent torrent = null; //redisCache.getCacheObject(cacheKey);
        if (torrent == null || torrent.getTorrent() == null) {
            torrent = getTorrentById(id);
            SysUser user = userService.selectUserById(torrent.getUserId());
            List<AttachmentVO> vo = new ArrayList();
            attachmentService.selectGlxTorrentAttachmentByTorrentId(id).forEach(x -> {
                try {
                    AttachmentVO attachmentVO = new AttachmentVO();
                    attachmentVO.setName(x.getTitle());
                    attachmentVO.setId(x.getId());
//                    attachmentVO.setUrl(x.getFileName());
                    Torrent t = getTorrent(x.getFileName());
                    t.setInfo_hash(null);
                    t.setAnnounce(null);
                    t.setAnnounceList(null);
                    attachmentVO.setTorrent(t);
                    vo.add(attachmentVO);
                } catch (IOException e) {

                }
            });
            torrent.setAttachment(vo);
            torrent.setTorrent(getTorrent(torrent.getFileName()));
            torrent = setInfoSecret(torrent);
            torrent.setCreateBy(user.getNickName());
            // 未下载完成的节点不做为种子节点
            List<GlxPeer> peers = peerService.selectGlxPeerByTorrentId(torrent.getId()).stream().filter(x -> {return x.getLeftSize() == 0L;}).collect(Collectors.toList());
            Map<String, Object> params = new HashMap<String, Object>(){{
                put("peers", peers.size());
            }};
            torrent.setParams(params);
            redisCache.setCacheObject(cacheKey, torrent, 10, TimeUnit.MINUTES);
        }
//        double ml = pointsUtil.countUserPoint(torrent);
        return AjaxResult.success(torrent);
    }

    @GetMapping("/list")
    public TableDataInfo list(GlxTorrent glxTorrent)
    {
        startPage();
        if (!getUser().isAdmin()) {
            glxTorrent.setStatus("1");
        }

        List<GlxTorrent> list = null;
        if (StringUtils.isEmpty(glxTorrent.getTags())) {
            list = glxTorrentService.selectGlxTorrentList(glxTorrent);
        } else {
            list = glxTorrentService.selectGlxTorrentByTags(glxTorrent.getTags());
        }
        list.forEach(x -> {
            x = setInfoSecret(x);
        });
        return getDataTable(list);
    }

    private boolean isPaid(Long torrentId) {
        List<GlxTorrentPurchase> torrentPurchases = torrentPurchaseService.selectGlxTorrentPurchaseList(new GlxTorrentPurchase() {
            {
                setTorrentId(torrentId);
                setUserId(getUser().getUserId());
            }
        });
        return torrentPurchases.size() != 0;
    }

    @GetMapping("/download/attachment/{id}")
    public AjaxResult prepareTorrentAttachmentFile (HttpServletResponse response, @PathVariable("id") Long attachmentId) throws IOException {
        GlxTorrentAttachment attachment = attachmentService.selectGlxTorrentAttachmentById(attachmentId);
        if (attachment == null) {
            return AjaxResult.error("文件不存在.");
        }
        if (!isPaid(attachment.getTorrentId()) && attachment.getUserId() != getUser().getUserId()) {
            return AjaxResult.error("购买种子之后才能下载.");
        }
        //SysUser publisher = userService.selectUserById(attachment.getUserId());
        String buffer = prepareTorrentBuffer(attachment.getFileName(), true );
        if (buffer != null) {
            return AjaxResult.success(attachment.getFileName().substring(attachment.getFileName().lastIndexOf("/") + 1), buffer);
        }
        return AjaxResult.error("种子文件无法读取,请联系管理员或上传者");
    }

    private String uploadPathToLocal(String path) {
        String filePath = RuoYiConfig.getUploadPath();
        path = filePath + path.substring(path.indexOf("/upload") + 7);
        return path;
    }

    private String prepareTorrentBuffer(String torrentPath, boolean attachment) throws IOException {
        Torrent bitTorrent = getTorrent(torrentPath);
        Bencode bencode = new Bencode(true);
        byte[] fileBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File(uploadPathToLocal(torrentPath)));
        Map<String, Object> fileMap = bencode.decode(fileBytes, Type.DICTIONARY);

        if (fileMap != null) {
            String announce = this.getUrl() + "/announce?token=" + getUser().getToken() + "&type=" + (attachment ? "attachment" : "torrent");
            fileMap.remove("announce-list");
            fileMap.put("announce", announce);
            fileMap.put("publisher-url", "https://www.galaxy-bit.com:9999/");
            byte[] fileBuffer = bencode.encode(fileMap);
            return Base64.encode(fileBuffer);
        }

        return null;
    }

    @GetMapping("/download/{id}")
    public AjaxResult prepareTorrentFile (HttpServletResponse response, @PathVariable("id") Long torrentId) throws IOException {
        SysUser user = userService.selectUserById(getUser().getUserId());
        if (user.isBan()) {
            return AjaxResult.error("您的账号已被封禁，目前您不能进行此操作。");
        }
        GlxTorrent torrent = glxTorrentService.selectGlxTorrentById(torrentId);
        if (!torrent.getUserId().equals(getUser().getUserId())) {
            if (!isPaid(torrentId)) {
                return AjaxResult.error("再不乱折腾哦.");
            }
        }
        // SysUser publisher = userService.selectUserById(torrent.getUserId());
        String buffer = prepareTorrentBuffer(torrent.getFileName(), false);

        if (buffer != null) {
            return AjaxResult.success(torrent.getFileName().substring(torrent.getFileName().lastIndexOf("/") + 1), buffer);
        }
        return AjaxResult.error("种子文件无法读取,请联系管理员或上传者");
    }

    private GlxTorrent getTorrentById(Long torrentId) throws Exception {
        GlxTorrent torrent = glxTorrentService.selectGlxTorrentById(torrentId);
        if (torrent == null) throw new Exception("资源不存在");
        if (!getUser().isAdmin()) {
            if (!torrent.getStatus().equals("1")) {
                throw new Exception("资源不存在或正在审核");
            }
        }
        return torrent;
    }

    @GetMapping("/payment/confirm")
    public AjaxResult confirmPayment(Long torrentId) throws Exception {
        DecimalFormat df = new DecimalFormat(".000");
        GlxTorrent torrent = getTorrentById(torrentId);
        SysUser user = userService.selectUserById(getUser().getUserId());
        HashMap<String, Object> paymentResult = new HashMap<>();
        boolean paid = isPaid(torrentId);
        if (!paid) {
            double pointRate = Double.parseDouble(configService.selectConfigByKey("drawbot.points.download"));
            Double torrentPoints = Double.parseDouble(df.format(torrent.getFileSize() / ConfigUtil.TRANSMISSION_UNIT * pointRate));
            if (user.getPoints() >= torrentPoints) {
                GlxPointsRecord record = new GlxPointsRecord() { {
                    setTorrentId(torrentId);
                    setUserId(getUser().getUserId());
                    setRemark("种子兑换");
                    setPoints(-torrentPoints);
                }};
                pointsRecordService.insertGlxPointsRecord(record);
                GlxTorrentPurchase torrentPurchase = new GlxTorrentPurchase() {
                    {
                        setUserId(getUser().getUserId());
                        setTorrentId(torrentId);
                        setPoints(torrentPoints);
                    }
                } ;
                torrentPurchaseService.insertGlxTorrentPurchase(torrentPurchase);
                torrent.setTotalDownload(torrent.getTotalDownload() + 1);
                glxTorrentService.updateGlxTorrentCounter(torrent);
                return AjaxResult.success();
            }
        } else {
            return AjaxResult.error("已购买,请勿重复提交");
        }

        return AjaxResult.error("购买失败, 积分不足.");
    }

    @GetMapping("/payment/info")
    public AjaxResult getPaymentInfo(Long torrentId) throws Exception {
        DecimalFormat df = new DecimalFormat(".000");
        GlxTorrent torrent = getTorrentById(torrentId);
        SysUser user = userService.selectUserById(getUser().getUserId());
        HashMap<String, Object> paymentResult = new HashMap<>();
        double pointRate = Double.parseDouble(configService.selectConfigByKey("drawbot.points.download"));
        Double torrentPoints = torrent.getFileSize() / ConfigUtil.TRANSMISSION_UNIT * pointRate;
        boolean paid = false;
        if (!user.getUserId().equals(torrent.getUserId())) {
            paid = isPaid(torrentId);
        } else {
            paid = true;
            torrentPoints = 0d;
        }
        paymentResult.put("isPaid", paid);
        paymentResult.put("points_available", user.getPoints());
        paymentResult.put("torrent_points", Double.parseDouble(df.format(torrentPoints)));
        return AjaxResult.success(paymentResult);
    }
}
