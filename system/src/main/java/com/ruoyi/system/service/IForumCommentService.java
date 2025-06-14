package com.ruoyi.system.service;

import com.ruoyi.system.domain.ForumComment;
import java.util.List;

public interface IForumCommentService {
    List<ForumComment> selectCommentList(ForumComment comment);
    ForumComment selectCommentById(Long commentId);
    int insertComment(ForumComment comment);
    int updateComment(ForumComment comment);
    int deleteCommentById(Long commentId);
    int getCommentCountByPostId(Long postId);
}