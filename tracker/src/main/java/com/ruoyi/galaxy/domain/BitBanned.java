package com.ruoyi.galaxy.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class BitBanned extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 用户 */
    @Excel(name = "用户")
    private Long userId;

    /** 解封时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解封时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expire;

    /** 封禁原因 */
    @Excel(name = "封禁原因")
    private String reason;

    /** 是否申诉 */
    @Excel(name = "是否申诉")
    private String appeal;

    @Excel(name = "申诉状态")
    private String appealState;

    /** 申诉回复 */
    @Excel(name = "申诉回复")
    private String appealResult;

    /** 申诉处理人 */
    @Excel(name = "申诉处理人")
    private Long appealUserId;

    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    public String getAppealState() {
        return appealState;
    }

    public void setAppealState(String appealState) {
        this.appealState = appealState;
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
    public void setExpire(Date expire) 
    {
        this.expire = expire;
    }

    public Date getExpire() 
    {
        return expire;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }
    public void setAppeal(String appeal)
    {
        this.appeal = appeal;
    }

    public String getAppeal()
    {
        return appeal;
    }
    public void setAppealResult(String appealResult) 
    {
        this.appealResult = appealResult;
    }

    public String getAppealResult() 
    {
        return appealResult;
    }
    public void setAppealUserId(Long appealUserId) 
    {
        this.appealUserId = appealUserId;
    }

    public Long getAppealUserId() 
    {
        return appealUserId;
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
            .append("userId", getUserId())
            .append("expire", getExpire())
            .append("reason", getReason())
            .append("appeal", getAppeal())
            .append("appealResult", getAppealResult())
            .append("appealUserId", getAppealUserId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
