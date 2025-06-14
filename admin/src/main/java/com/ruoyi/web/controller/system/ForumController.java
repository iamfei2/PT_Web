package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.ForumPost;
import com.ruoyi.system.service.IForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum/post")
public class ForumController extends BaseController {
    @Autowired
    private IForumService forumService;

    @GetMapping("/list")
    public TableDataInfo list(ForumPost forumPost) {
        startPage();
        List<ForumPost> list = forumService.selectForumPostList(forumPost);
        return getDataTable(list);
    }

    @GetMapping("/{postId}")
    public AjaxResult getInfo(@PathVariable Long postId) {
        return AjaxResult.success(forumService.selectForumPostById(postId));
    }

    // 所有用户都可以发帖
    @PostMapping
    public AjaxResult add(@RequestBody ForumPost forumPost) {
        // 设置当前用户ID
        Long userId = SecurityUtils.getLoginUser().getUserId();
        forumPost.setUserId(userId);
        return toAjax(forumService.insertForumPost(forumPost));
    }

    // 用户只能修改自己的帖子，管理员可以修改所有帖子
    @PutMapping
    public AjaxResult edit(@RequestBody ForumPost forumPost) {
        // 获取原始帖子信息
        ForumPost originalPost = forumService.selectForumPostById(forumPost.getPostId());
        if (originalPost == null) {
            return AjaxResult.error("帖子不存在");
        }

        // 检查权限：当前用户是否是作者或管理员
        Long currentUserId = SecurityUtils.getLoginUser().getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(currentUserId);
        boolean isAuthor = originalPost.getUserId().equals(currentUserId);

        if (!isAuthor && !isAdmin) {
            return AjaxResult.error("无权修改此帖子");
        }

        // 防止篡改作者信息
        forumPost.setUserId(originalPost.getUserId());

        // 更新帖子
        return toAjax(forumService.updateForumPost(forumPost));
    }

    // 用户只能删除自己的帖子，管理员可以删除所有帖子
    @DeleteMapping("/{postId}")
    public AjaxResult remove(@PathVariable Long postId) {
        // 获取帖子信息
        ForumPost post = forumService.selectForumPostById(postId);
        if (post == null) {
            return AjaxResult.error("帖子不存在");
        }

        // 检查权限：当前用户是否是作者或管理员
        Long currentUserId = SecurityUtils.getLoginUser().getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(currentUserId);
        boolean isAuthor = post.getUserId().equals(currentUserId);

        if (!isAuthor && !isAdmin) {
            return AjaxResult.error("无权删除此帖子");
        }

        // 删除帖子
        return toAjax(forumService.deleteForumPostById(postId));
    }
}