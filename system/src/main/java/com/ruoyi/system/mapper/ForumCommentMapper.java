package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ForumComment;
import java.util.List;

public interface ForumCommentMapper {
    List<ForumComment> selectCommentList(ForumComment comment);
    ForumComment selectCommentById(Long commentId);
    int insertComment(ForumComment comment);
    int updateComment(ForumComment comment);
    int deleteCommentById(Long commentId);
    int selectCommentCountByPostId(Long postId);
}