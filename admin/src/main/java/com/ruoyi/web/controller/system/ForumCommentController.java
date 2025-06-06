package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ForumComment;
import com.ruoyi.system.service.IForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum/comment")
public class ForumCommentController extends BaseController {

    @Autowired
    private IForumCommentService commentService;

    // 获取评论列表（可按帖子ID过滤）
    @GetMapping("/list")
    public TableDataInfo list(ForumComment comment) {
        startPage();
        List<ForumComment> list = commentService.selectCommentList(comment);
        return getDataTable(list);
    }

    // 获取评论详情
    @GetMapping("/{commentId}")
    public AjaxResult getInfo(@PathVariable Long commentId) {
        return AjaxResult.success(commentService.selectCommentById(commentId));
    }

    // 添加评论
    @PostMapping
    public AjaxResult add(@RequestBody ForumComment comment) {
        return toAjax(commentService.insertComment(comment));
    }

    // 修改评论
    @PutMapping
    public AjaxResult edit(@RequestBody ForumComment comment) {
        return toAjax(commentService.updateComment(comment));
    }

    // 删除评论
    @DeleteMapping("/{commentId}")
    public AjaxResult remove(@PathVariable Long commentId) {
        return toAjax(commentService.deleteCommentById(commentId));
    }

    // 获取帖子评论数
    @GetMapping("/count/{postId}")
    public AjaxResult getCommentCount(@PathVariable Long postId) {
        return AjaxResult.success(commentService.getCommentCountByPostId(postId));
    }
}