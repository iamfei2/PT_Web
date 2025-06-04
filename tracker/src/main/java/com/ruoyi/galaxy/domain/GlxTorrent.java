package com.ruoyi.galaxy.domain;

import be.christophedetroyer.torrent.Torrent;
import com.ruoyi.galaxy.service.impl.GlxTorrentTagsServiceImpl;
import com.ruoyi.galaxy.vo.AttachmentVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源广场对象 glx_torrent
 * 
 * @author HexPang
 * @date 2021-03-17
 */
public class GlxTorrent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private GlxTorrentTagsServiceImpl torrentTagsService;

    /** 编号 */
    private Long id;

    /** 哈希值 */
    @Excel(name = "哈希值")
    private String infoHash;

    @Excel(name = "用户编号")
    private Long userId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 种子路径 */
    private String fileName;

    /** 目录 */
    @Excel(name = "目录")
    private Long categories;

    /** 详情 */
    private String description;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String thumburl;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 下载次数 */
    @Excel(name = "下载次数")
    private Long totalDownload;

    /** 上传积分 */
    @Excel(name = "上传积分")
    private Long uploaded;

    /** 下载积分 */
    @Excel(name = "下载积分")
    private Long downloaded;

    /** 上传令牌 */
    private String uploadToken;

    /** 下载令牌 */
    private String downloadToken;

    @Transient
    private Torrent torrent;

    @Transient
    private List<AttachmentVO> attachment;

    @Transient
    @Excel(name = "标签")
    private List<String> tags;

    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<AttachmentVO> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentVO> attachment) {
        this.attachment = attachment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Torrent getTorrent() {
        return torrent;
    }

    public void setTorrent(Torrent torrent) {
        this.torrent = torrent;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInfoHash(String infoHash) 
    {
        this.infoHash = infoHash;
    }

    public String getInfoHash() 
    {
        return infoHash;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setCategories(Long categories)
    {
        this.categories = categories;
    }

    public Long getCategories()
    {
        return categories;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setThumburl(String thumburl) 
    {
        this.thumburl = thumburl;
    }

    public String getThumburl() 
    {
        return thumburl;
    }
    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }
    public void setTotalDownload(Long totalDownload) 
    {
        this.totalDownload = totalDownload;
    }

    public Long getTotalDownload() 
    {
        return totalDownload;
    }
    public void setUploaded(Long uploaded) 
    {
        this.uploaded = uploaded;
    }

    public Long getUploaded() 
    {
        return uploaded;
    }
    public void setDownloaded(Long downloaded) 
    {
        this.downloaded = downloaded;
    }

    public Long getDownloaded() 
    {
        return downloaded;
    }
    public void setUploadToken(String uploadToken) 
    {
        this.uploadToken = uploadToken;
    }

    public String getUploadToken() 
    {
        return uploadToken;
    }
    public void setDownloadToken(String downloadToken) 
    {
        this.downloadToken = downloadToken;
    }

    public String getDownloadToken() 
    {
        return downloadToken;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("infoHash", getInfoHash())
            .append("title", getTitle())
            .append("fileName", getFileName())
            .append("categories", getCategories())
            .append("description", getDescription())
            .append("thumburl", getThumburl())
            .append("fileSize", getFileSize())
            .append("totalDownload", getTotalDownload())
            .append("uploaded", getUploaded())
            .append("downloaded", getDownloaded())
            .append("uploadToken", getUploadToken())
            .append("downloadToken", getDownloadToken())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
