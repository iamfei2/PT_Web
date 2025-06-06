package com.ruoyi.system.domain;

import java.util.Date;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class ForumPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long postId;
    @Excel(name = "标题")
    private String title;
    @Excel(name = "内容")
    private String content;
    @Excel(name = "用户ID")
    private Long userId;
    @Excel(name = "状态")
    private String status;
    private Date createTime;
    private Date updateTime;
    private Integer commentCount;
    private String userName; // 添加用户名字段
// 增加 getter/setter

    // Getters and Setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    @Override
    public Date getCreateTime() { return createTime; }
    @Override
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    @Override
    public Date getUpdateTime() { return updateTime; }
    @Override
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}