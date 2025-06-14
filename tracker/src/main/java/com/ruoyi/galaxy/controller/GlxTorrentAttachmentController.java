package com.ruoyi.galaxy.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.galaxy.domain.GlxTorrentAttachment;
import com.ruoyi.galaxy.service.IGlxTorrentAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 种子附件Controller
 * 
 * @author HexPang
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/galaxy/attachment")
public class GlxTorrentAttachmentController extends BaseController
{
    @Autowired
    private IGlxTorrentAttachmentService glxTorrentAttachmentService;

    /**
     * 查询种子附件列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxTorrentAttachment glxTorrentAttachment)
    {
        startPage();
        List<GlxTorrentAttachment> list = glxTorrentAttachmentService.selectGlxTorrentAttachmentList(glxTorrentAttachment);
        return getDataTable(list);
    }

    /**
     * 导出种子附件列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:export')")
    @Log(title = "种子附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxTorrentAttachment glxTorrentAttachment)
    {
        List<GlxTorrentAttachment> list = glxTorrentAttachmentService.selectGlxTorrentAttachmentList(glxTorrentAttachment);
        ExcelUtil<GlxTorrentAttachment> util = new ExcelUtil<GlxTorrentAttachment>(GlxTorrentAttachment.class);
        return util.exportExcel(list, "attachment");
    }

    /**
     * 获取种子附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxTorrentAttachmentService.selectGlxTorrentAttachmentById(id));
    }

    /**
     * 新增种子附件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:add')")
    @Log(title = "种子附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxTorrentAttachment glxTorrentAttachment)
    {
        return toAjax(glxTorrentAttachmentService.insertGlxTorrentAttachment(glxTorrentAttachment));
    }

    /**
     * 修改种子附件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:edit')")
    @Log(title = "种子附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxTorrentAttachment glxTorrentAttachment)
    {
        return toAjax(glxTorrentAttachmentService.updateGlxTorrentAttachment(glxTorrentAttachment));
    }

    /**
     * 删除种子附件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:attachment:remove')")
    @Log(title = "种子附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxTorrentAttachmentService.deleteGlxTorrentAttachmentByIds(ids));
    }
}
