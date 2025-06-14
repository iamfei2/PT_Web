package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.ForumPost;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.service.IForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumServiceImpl implements IForumService {
    @Autowired
    private ForumPostMapper forumPostMapper;

    @Override
    public List<ForumPost> selectForumPostList(ForumPost forumPost) {
        return forumPostMapper.selectForumPostList(forumPost);
    }

    @Override
    public ForumPost selectForumPostById(Long postId) {
        ForumPost post = forumPostMapper.selectForumPostById(postId);
        if (post == null) {
            throw new ServiceException("帖子不存在");
        }
        return post;
    }

    @Override
    public int insertForumPost(ForumPost forumPost) {
        // 从当前登录用户获取ID (示例)
        forumPost.setUserId(getCurrentUserId());
        return forumPostMapper.insertForumPost(forumPost);
    }

    @Override
    public int updateForumPost(ForumPost forumPost) {
        // 检查操作权限
        verifyPostOwnership(forumPost.getPostId());
        return forumPostMapper.updateForumPost(forumPost);
    }

    @Override
    public int deleteForumPostById(Long postId) {
        // 检查操作权限
        verifyPostOwnership(postId);
        return forumPostMapper.deleteForumPostById(postId);
    }

    private Long getCurrentUserId() {
        // 从SecurityUtils获取当前登录用户ID
        return SecurityUtils.getLoginUser().getUserId();
    }

    private void verifyPostOwnership(Long postId) {
        ForumPost post = forumPostMapper.selectForumPostById(postId);
        Long currentUserId = getCurrentUserId();
        boolean isAdmin = SecurityUtils.isAdmin(currentUserId);

        if (post == null) {
            throw new ServiceException("帖子不存在");
        }

        if (!currentUserId.equals(post.getUserId()) && !isAdmin) {
            throw new ServiceException("无权操作此帖子");
        }
    }

    // 修改权限检查方法
    private void verifyPostOwnershipOrAdmin(Long postId) {
        ForumPost post = forumPostMapper.selectForumPostById(postId);
        Long currentUserId = getCurrentUserId();
        boolean isAdmin = SecurityUtils.isAdmin(currentUserId);

        if (post == null) {
            throw new ServiceException("帖子不存在");
        }

        if (!currentUserId.equals(post.getUserId()) && !isAdmin) {
            throw new ServiceException("无权操作此帖子");
        }
    }


}