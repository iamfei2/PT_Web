package com.ruoyi.galaxy.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import be.christophedetroyer.torrent.Torrent;
import be.christophedetroyer.torrent.TorrentParser;
import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.galaxy.domain.BitTorrentAttachment;
import com.ruoyi.galaxy.domain.BitTorrentFile;
import com.ruoyi.galaxy.service.IBitTorrentAttachmentService;
import com.ruoyi.galaxy.service.IBitTorrentFileService;
import com.ruoyi.galaxy.vo.AttachmentVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.galaxy.domain.BitTorrent;
import com.ruoyi.galaxy.service.IBitTorrentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/galaxy/torrent")
public class BitTorrentController extends BaseController
{
    @Autowired
    private IBitTorrentService glxTorrentService;

    @Autowired
    private IBitTorrentFileService torrentFileService;

    @Autowired
    private IBitTorrentAttachmentService attachmentService;

    private List<BitTorrentFile> getDuplicateFiles (Torrent torrent) {
        Map<String, List<Object>> data = new HashMap<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> sizeList = new ArrayList<>();
        if (torrent.getFileList() != null) {
            torrent.getFileList().forEach(x -> {
                nameList.add(x.getFileDirs().get(x.getFileDirs().size() - 1));
                sizeList.add(x.getFileLength());
            });
        } else {
            nameList.add(torrent.getName());
            sizeList.add(torrent.getTotalSize());
        }
        data.put("fileName", nameList);
        data.put("fileSize", sizeList);
        return torrentFileService.selectGlxTorrentFileByFile(data);
    }

    private String uploadPathToLocal(String path) {
        String filePath = RuoYiConfig.getUploadPath();
        path = filePath + path.substring(path.indexOf("/upload") + 7);
        return path;
    }

    private String prepareTorrentBuffer(String torrentPath, boolean attachment, String seed) throws IOException {
        Torrent bitTorrent = getTorrent(torrentPath);
        Bencode bencode = new Bencode(true);
        byte[] fileBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File(uploadPathToLocal(torrentPath)));
        Map<String, Object> fileMap = bencode.decode(fileBytes, Type.DICTIONARY);

        if (fileMap != null) {
            String announce = this.getUrl() + "/announce?token=" + getUser().getToken() + "&seed=" + seed;
            fileMap.remove("announce-list");
            fileMap.put("announce", announce);
            fileMap.put("publisher-url", "https://www.galaxy-bit.com:9999/");
            byte[] fileBuffer = bencode.encode(fileMap);
            return Base64.encode(fileBuffer);
        }

        return null;
    }

    @GetMapping("/torrent/seed")
    public AjaxResult downloadSeed(Long id) throws IOException {
        BitTorrent torrent = glxTorrentService.selectGlxTorrentById(id);
        if (torrent.getUserId() != getUser().getUserId()) {
            return AjaxResult.error("没有权限下载");
        }
        String buffer = prepareTorrentBuffer(torrent.getFileName(), false, torrent.getUploadToken());
        if (buffer != null) {
            return AjaxResult.success(torrent.getFileName().substring(torrent.getFileName().lastIndexOf("/") + 1), buffer);
        }
        return AjaxResult.error("信息出错");
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        if (!file.getOriginalFilename().endsWith(".torrent")) {
            return AjaxResult.error("只能上传种子文件");
        }
        try
        {
            Torrent torrent = TorrentParser.parseTorrent(file.getInputStream());
            List<BitTorrent> torrents = glxTorrentService.selectGlxTorrentList(new BitTorrent(){{
                setInfoHash(torrent.getInfo_hash());
            }});
            if (torrents.size() > 0) {
                return AjaxResult.error("种子已存在.");
            }
            List<BitTorrentFile> dFiles = getDuplicateFiles(torrent);
            torrent.setPiecesBlob(null);
            torrent.setPieces(null);
            torrent.setAnnounce(this.getUrl() + "/announce?token=" + SecurityUtils.getLoginUser().getUser().getToken());
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = this.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            ajax.put("torrent", torrent);
            ajax.put("duplicate", dFiles);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询资源广场列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:list')")
    @GetMapping("/list")
    public TableDataInfo list(BitTorrent bitTorrent)
    {
        startPage();
        if (!getUser().isAdmin()) {
            bitTorrent.setUserId(getUser().getUserId());
        }
        List<BitTorrent> list = glxTorrentService.selectGlxTorrentList(bitTorrent);
        return getDataTable(list);
    }

    /**
     * 查询资源广场列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:verify')")
    @GetMapping("/review/list")
    public TableDataInfo auditList(BitTorrent bitTorrent)
    {
        startPage();
        bitTorrent.setStatus("0");
        List<BitTorrent> list = glxTorrentService.selectGlxTorrentList(bitTorrent);
        return getDataTable(list);
    }

    /**
     * 导出资源广场列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:export')")
    @Log(title = "资源广场", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BitTorrent bitTorrent)
    {
        List<BitTorrent> list = glxTorrentService.selectGlxTorrentList(bitTorrent);
        ExcelUtil<BitTorrent> util = new ExcelUtil<BitTorrent>(BitTorrent.class);
        return util.exportExcel(list, "torrent");
    }

    /**
     * 获取资源广场详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) throws IOException {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        BitTorrent bitTorrent = glxTorrentService.selectGlxTorrentById(id);
        if (bitTorrent == null) {
            return AjaxResult.error("种子不存在");
        }
        if (!bitTorrent.getUserId().equals(user.getUserId()) && !getUser().isAdmin()) {
            //检查购买状态
            return AjaxResult.error("没有查询权限");
        }
        String torrentPath = bitTorrent.getFileName();
        Torrent torrent = getTorrent(torrentPath);
        bitTorrent.setRemark(user.getToken());
        torrent.setAnnounce(this.getUrl() + "/announce?token=" + user.getToken());
        bitTorrent.setTorrent(torrent);
        List<AttachmentVO> vo = new ArrayList();
        attachmentService.selectGlxTorrentAttachmentByTorrentId(id).forEach(x -> {
            try {
                AttachmentVO attachmentVO = new AttachmentVO();
                attachmentVO.setId(x.getId());
                attachmentVO.setName(x.getTitle());
                attachmentVO.setUrl(x.getFileName());
                attachmentVO.setTorrent(getTorrent(x.getFileName()));
                vo.add(attachmentVO);
            } catch (IOException e) {

            }
        });
        bitTorrent.setAttachment(vo);
        return AjaxResult.success(bitTorrent);
    }

    /**
     * 获取种子
     * @param torrentPath
     * @return
     * @throws IOException
     */
    private Torrent getTorrent(String torrentPath) throws IOException {
        String filePath = RuoYiConfig.getUploadPath();
        torrentPath = filePath + torrentPath.substring(torrentPath.indexOf("/upload") + 7);
        Torrent torrent = TorrentParser.parseTorrent(torrentPath);
        torrent.setPieces(null);
        torrent.setPiecesBlob(null);
        return torrent;
    }

    private BitTorrent fillTorrentData (BitTorrent bitTorrent) throws IOException {
        Torrent torrent = getTorrent(bitTorrent.getFileName());
        bitTorrent.setTorrent(torrent);
        long fileSize = 0;
        if(torrent.getFileList() != null) {
            for(int i = 0 ; i < torrent.getFileList().size(); i++) {
                fileSize += torrent.getFileList().get(i).getFileLength();
            }
        } else {
            fileSize = torrent.getTotalSize();
        }

        if (!StringUtils.isNotEmpty(bitTorrent.getUploadToken())) {
            bitTorrent.setUploadToken(UUID.fastUUID().toString());
        }
        if (!StringUtils.isNotEmpty(bitTorrent.getDownloadToken())) {
            bitTorrent.setDownloadToken(UUID.fastUUID().toString());
        }
        bitTorrent.setFileSize(fileSize);
        bitTorrent.setInfoHash(torrent.getInfo_hash());
        bitTorrent.setCreateBy(SecurityUtils.getUsername());
        return bitTorrent;
    }

    private void insertTorrentFiles(BitTorrent torrent) {
        Torrent t = torrent.getTorrent();
        if (t.getFileList() != null) {
            t.getFileList().forEach(x -> {
                BitTorrentFile gtf = new BitTorrentFile();
                gtf.setFileName(x.getFileDirs().get(x.getFileDirs().size() - 1));
                gtf.setFileSize(x.getFileLength());
                gtf.setTorrentId(torrent.getId());
                torrentFileService.insertGlxTorrentFile(gtf);
            });
        } else {
            BitTorrentFile gtf = new BitTorrentFile();
            gtf.setFileName(torrent.getTorrent().getName());
            gtf.setFileSize(torrent.getTorrent().getTotalSize());
            gtf.setTorrentId(torrent.getId());
            torrentFileService.insertGlxTorrentFile(gtf);
        }
    }

    private boolean shouldReview() {
        if (getUser().isAdmin()) {
            return false;
        }

        List<SysRole> roles = getUser().getRoles();
        if (roles == null || roles.size() == 0) return true;
        for(int i = 0 ; i < roles.size(); i ++) {
            if (roles.get(i).getRoleKey().equals("user_plus")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 新增资源广场
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:add')")
    @Log(title = "资源广场", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BitTorrent bitTorrent) throws Exception {
        torrentDataVerify(bitTorrent);
        bitTorrent.setUserId(getUser().getUserId());
        bitTorrent.setUploaded(0L);
        bitTorrent.setDownloaded(0L);
        bitTorrent.setTotalDownload(0L);
        if (!shouldReview() && StringUtils.isNotEmpty(bitTorrent.getStatus())) {
            if (bitTorrent.getStatus().equals("0") || bitTorrent.getStatus().equals("1")) {
                bitTorrent.setStatus(bitTorrent.getStatus());
            }
        } else {
            bitTorrent.setStatus("0");
        }
        bitTorrent = fillTorrentData(bitTorrent);
        int ret = glxTorrentService.insertGlxTorrent(bitTorrent);
        insertTorrentFiles(bitTorrent);
        attachment(bitTorrent, false);
        return toAjax(ret);
    }

    private void attachment(BitTorrent torrent, boolean isUpdate) throws IOException {
        if (torrent.getAttachment() == null) {
            return;
        }
        if (isUpdate) {
            attachmentService.deleteGlxTorrentAttachmentByTorrentId(torrent.getId());
        }
        for(int i = 0 ; i < torrent.getAttachment().size(); i++) {
            AttachmentVO x = torrent.getAttachment().get(i);
            Torrent t = getTorrent(x.getUrl());
            BitTorrentAttachment attachment = new BitTorrentAttachment();
            attachment.setFileName(x.getUrl());
            attachment.setTitle(x.getName());
            attachment.setTorrentId(torrent.getId());
            attachment.setInfoHash(t.getInfo_hash());
            attachment.setUserId(torrent.getUserId());
            attachment.setCreateBy(getUser().getUserName());
            if (t.getTotalSize() == null) {
                long size = 0L;
                for (int j = 0 ; j < t.getFileList().size(); j++) {
                    size += t.getFileList().get(j).getFileLength();
                }
                t.setTotalSize(size);
            }
            attachment.setTorrentSize(t.getTotalSize());
            attachmentService.insertGlxTorrentAttachment(attachment);
        }
    }

    /**
     * 新增资源广场
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:verify')")
    @Log(title = "资源广场", businessType = BusinessType.UPDATE)
    @GetMapping("/verify")
    public AjaxResult verify(Long torrentId, String verify, String msg) throws IOException {
        BitTorrent torrent = glxTorrentService.selectGlxTorrentById(torrentId);
        if (torrent.getStatus().equals("1")) {
            return AjaxResult.error("已审核");
        }
        torrent.setRemark(msg);
        torrent.setStatus(verify);
        return toAjax(glxTorrentService.updateGlxTorrent(torrent));
    }

    private void torrentDataVerify (BitTorrent torrent) throws Exception {
        if (StringUtils.isEmpty(torrent.getFileName())) {
            throw new Exception("种子文件不能为空.");
        }
        if (StringUtils.isEmpty(torrent.getThumburl())) {
            throw new Exception("封面图不能为空.");
        }
        if (StringUtils.isEmpty(torrent.getDescription())) {
            throw new Exception("介绍信息不能为空.");
        }
    }

    /**
     * 修改资源广场
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:edit')")
    @Log(title = "资源广场", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BitTorrent bitTorrent) throws Exception {
        torrentDataVerify(bitTorrent);
        String status = bitTorrent.getStatus();
        BitTorrent torrent = glxTorrentService.selectGlxTorrentById(bitTorrent.getId());
        if (!torrent.getUserId().equals(getUser().getUserId()) && !getUser().isAdmin()) {
            return AjaxResult.error("没有编辑权限");
        }
        bitTorrent.setStatus("0");
        if (!shouldReview() && StringUtils.isNotEmpty(status)) {
            if (status.equals("1")) {
                bitTorrent.setStatus(status);
            }
        }
        if (bitTorrent.getRemark() != null && bitTorrent.getRemark().equalsIgnoreCase(getUser().getToken())) {
            bitTorrent.setRemark(null);
        }
        attachment(bitTorrent, true);
        return toAjax(glxTorrentService.updateGlxTorrent(fillTorrentData(bitTorrent)));
    }

    /**
     * 删除资源广场
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent:remove')")
    @Log(title = "资源广场", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        if (!getUser().isAdmin()) {
            for (Long id:
                 ids) {
                BitTorrent torrent = glxTorrentService.selectGlxTorrentById(id);
                if (!torrent.getUserId().equals(getUser().getUserId())) {
                    return AjaxResult.error("没有删除权限");
                }
            }
        }
        return toAjax(glxTorrentService.deleteGlxTorrentByIds(ids));
    }
}
