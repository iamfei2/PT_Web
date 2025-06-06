package com.ruoyi.system.service;

import com.ruoyi.system.domain.ForumPost;
import java.util.List;

public interface IForumService {
    List<ForumPost> selectForumPostList(ForumPost forumPost);
    ForumPost selectForumPostById(Long postId);
    int insertForumPost(ForumPost forumPost);
    int updateForumPost(ForumPost forumPost);
    int deleteForumPostById(Long postId);
}