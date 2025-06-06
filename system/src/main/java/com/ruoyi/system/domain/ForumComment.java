package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

public class ForumComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long commentId;
    private Long postId;
    private Long parentId; // 父评论ID（0表示顶级评论）
    @Excel(name = "评论内容")
    private String content;
    @Excel(name = "用户ID")
    private Long userId;
    @Excel(name = "状态")
    private String status;
    private Date createTime;
    private Date updateTime;

    // Getters and Setters
    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    @Override public Date getCreateTime() { return createTime; }
    @Override public void setCreateTime(Date createTime) { this.createTime = createTime; }
    @Override public Date getUpdateTime() { return updateTime; }
    @Override public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}