package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class BitTorrentAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 哈希值 */
    @Excel(name = "哈希值")
    private String infoHash;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String fileName;

    /** 所属用户 */
    @Excel(name = "所属用户")
    private Long userId;

    /** 所属种子 */
    @Excel(name = "所属种子")
    private Long torrentId;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long torrentSize;

    /** 上传积分 */
    @Excel(name = "上传积分")
    private Long uploaded;

    /** 下载积分 */
    @Excel(name = "下载积分")
    private Long downloaded;


    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    public Long getUploaded() {
        return uploaded;
    }

    public void setUploaded(Long uploaded) {
        this.uploaded = uploaded;
    }

    public Long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Long downloaded) {
        this.downloaded = downloaded;
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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTorrentId(Long torrentId) 
    {
        this.torrentId = torrentId;
    }

    public Long getTorrentId() 
    {
        return torrentId;
    }
    public void setTorrentSize(Long torrentSize) 
    {
        this.torrentSize = torrentSize;
    }

    public Long getTorrentSize() 
    {
        return torrentSize;
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
            .append("userId", getUserId())
            .append("torrentId", getTorrentId())
            .append("torrentSize", getTorrentSize())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
