package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ForumPost;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ForumPostMapper {
    List<ForumPost> selectForumPostList(ForumPost forumPost);
    ForumPost selectForumPostById(Long postId);
    int insertForumPost(ForumPost forumPost);
    int updateForumPost(ForumPost forumPost);
    int deleteForumPostById(Long postId);
    int updateCommentCount(@Param("postId") Long postId, @Param("count") int count);

    @Select("SELECT COUNT(*) FROM forum_comment WHERE post_id = #{postId}")
    int selectCommentCountByPostId(Long postId);
}