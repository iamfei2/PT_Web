package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 积分明细对象 glx_points_record
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public class GlxPointsRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 用户 */
    @Excel(name = "用户")
    private Long userId;

    /** 积分 */
    @Excel(name = "积分")
    private Double points;

    /** 种子 */
    @Excel(name = "种子")
    private Long torrentId;

    /** 节点 */
    @Excel(name = "节点")
    private Long peerId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setPoints(Double points)
    {
        this.points = points;
    }

    public Double getPoints()
    {
        return points;
    }
    public void setTorrentId(Long torrentId) 
    {
        this.torrentId = torrentId;
    }

    public Long getTorrentId() 
    {
        return torrentId;
    }
    public void setPeerId(Long peerId) 
    {
        this.peerId = peerId;
    }

    public Long getPeerId() 
    {
        return peerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("points", getPoints())
            .append("torrentId", getTorrentId())
            .append("peerId", getPeerId())
            .append("remark", getRemark())
            .toString();
    }
}
