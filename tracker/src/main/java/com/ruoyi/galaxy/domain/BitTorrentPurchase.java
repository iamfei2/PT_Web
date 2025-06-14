package com.ruoyi.galaxy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;


public class BitTorrentPurchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 种子编号 */
    @Excel(name = "种子编号")
    private Long torrentId;

//    @Transient
//    private String title;

    /** 消费积分 */
    @Excel(name = "消费积分")
    private Double points;

    private Date startTime;

    private Date finishTime;

    private Long maxSpeed;

    private Long avgSpeed;

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Long maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Long getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Long avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

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
    public void setTorrentId(Long torrentId) 
    {
        this.torrentId = torrentId;
    }

    public Long getTorrentId() 
    {
        return torrentId;
    }
    public void setPoints(Double points)
    {
        this.points = points;
    }

    public Double getPoints()
    {
        return points;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("torrentId", getTorrentId())
            .append("points", getPoints())
            .append("createTime", getCreateTime())
            .toString();
    }
}
