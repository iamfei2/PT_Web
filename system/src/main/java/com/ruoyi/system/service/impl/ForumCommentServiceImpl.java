package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.ForumComment;
import com.ruoyi.system.mapper.ForumCommentMapper;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.service.IForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumCommentServiceImpl implements IForumCommentService {

    @Autowired
    private ForumPostMapper forumPostMapper;
    @Autowired
    private ForumCommentMapper commentMapper;

    @Override
    public List<ForumComment> selectCommentList(ForumComment comment) {
        return commentMapper.selectCommentList(comment);
    }

    @Override
    public ForumComment selectCommentById(Long commentId) {
        return commentMapper.selectCommentById(commentId);
    }

    @Override
    public int insertComment(ForumComment comment) {
        comment.setUserId(SecurityUtils.getLoginUser().getUserId());
        int result = commentMapper.insertComment(comment);

        // 新增：更新帖子评论计数
        if (result > 0 && comment.getPostId() != null) {
            forumPostMapper.updateCommentCount(comment.getPostId(), 1);
        }

        return result;
    }


    @Override
    public int updateComment(ForumComment comment) {
        verifyCommentOwnership(comment.getCommentId());
        return commentMapper.updateComment(comment);
    }

    @Override
    public int deleteCommentById(Long commentId) {
        verifyCommentOwnershipOrAdmin(commentId);
        ForumComment comment = commentMapper.selectCommentById(commentId);
        int result = commentMapper.deleteCommentById(commentId);

        // 新增：更新帖子评论计数
        if (result > 0 && comment != null && comment.getPostId() != null) {
            forumPostMapper.updateCommentCount(comment.getPostId(), -1);
        }

        return result;
    }

    @Override
    public int getCommentCountByPostId(Long postId) {
        return commentMapper.selectCommentCountByPostId(postId);
    }

    private void verifyCommentOwnership(Long commentId) {
        ForumComment comment = commentMapper.selectCommentById(commentId);
        Long currentUserId = SecurityUtils.getLoginUser().getUserId();
        if (comment == null || !currentUserId.equals(comment.getUserId())) {
            throw new ServiceException("无权操作此评论");
        }
    }

    private void verifyCommentOwnershipOrAdmin(Long commentId) {
        ForumComment comment = commentMapper.selectCommentById(commentId);
        Long currentUserId = SecurityUtils.getLoginUser().getUserId();

        // 管理员可以删除任何评论
        if (SecurityUtils.isAdmin(currentUserId)) {
            return;
        }

        if (comment == null || !currentUserId.equals(comment.getUserId())) {
            throw new ServiceException("无权操作此评论");
        }
    }
}