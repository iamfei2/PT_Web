package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 种子标签对象 glx_torrent_tags
 * 
 * @author Hex
 * @date 2021-11-04
 */
public class GlxTorrentTags extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 种子 */
    @Excel(name = "种子")
    private Long torrentId;

    /** 目录 */
    @Excel(name = "种子目录")
    private Long categoryId;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
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
            .append("torrentId", getTorrentId())
            .append("tag", getTag())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
