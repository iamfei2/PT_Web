package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class BitTorrentFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 种子编号 */
    @Excel(name = "种子编号")
    private Long torrentId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTorrentId(Long torrentId) 
    {
        this.torrentId = torrentId;
    }

    public Long getTorrentId() 
    {
        return torrentId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("torrentId", getTorrentId())
            .append("fileName", getFileName())
            .append("fileSize", getFileSize())
            .toString();
    }
}
