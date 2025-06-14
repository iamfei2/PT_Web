package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ForumPost;
import org.apache.ibatis.annotations.Param; // 修复：使用MyBatis的Param注解
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface ForumPostMapper {
    List<ForumPost> selectForumPostList(ForumPost forumPost);
    ForumPost selectForumPostById(Long postId);
    int insertForumPost(ForumPost forumPost);
    int updateForumPost(ForumPost forumPost);
    int deleteForumPostById(Long postId);

    // 修复：使用正确的@Param注解
    int updateCommentCount(@Param("postId") Long postId, @Param("count") int count);

    @Select("SELECT COUNT(*) FROM forum_comment WHERE post_id = #{postId}")
    int selectCommentCountByPostId(Long postId);
}