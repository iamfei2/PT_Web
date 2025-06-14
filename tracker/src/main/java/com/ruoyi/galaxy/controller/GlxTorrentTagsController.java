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
import com.ruoyi.galaxy.domain.GlxTorrentTags;
import com.ruoyi.galaxy.service.IGlxTorrentTagsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 种子标签Controller
 * 
 * @author Hex
 * @date 2021-11-04
 */
@RestController
@RequestMapping("/galaxy/tags")
public class GlxTorrentTagsController extends BaseController
{
    @Autowired
    private IGlxTorrentTagsService glxTorrentTagsService;

    /**
     * 查询种子标签列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxTorrentTags glxTorrentTags)
    {
        startPage();
        List<GlxTorrentTags> list = glxTorrentTagsService.selectGlxTorrentTagsList(glxTorrentTags);
        return getDataTable(list);
    }

    /**
     * 导出种子标签列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:export')")
    @Log(title = "种子标签", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxTorrentTags glxTorrentTags)
    {
        List<GlxTorrentTags> list = glxTorrentTagsService.selectGlxTorrentTagsList(glxTorrentTags);
        ExcelUtil<GlxTorrentTags> util = new ExcelUtil<GlxTorrentTags>(GlxTorrentTags.class);
        return util.exportExcel(list, "tags");
    }

    /**
     * 获取种子标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxTorrentTagsService.selectGlxTorrentTagsById(id));
    }

    /**
     * 新增种子标签
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:add')")
    @Log(title = "种子标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxTorrentTags glxTorrentTags)
    {
        return toAjax(glxTorrentTagsService.insertGlxTorrentTags(glxTorrentTags));
    }

    /**
     * 修改种子标签
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:edit')")
    @Log(title = "种子标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxTorrentTags glxTorrentTags)
    {
        return toAjax(glxTorrentTagsService.updateGlxTorrentTags(glxTorrentTags));
    }

    /**
     * 删除种子标签
     */
    @PreAuthorize("@ss.hasPermi('galaxy:tags:remove')")
    @Log(title = "种子标签", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxTorrentTagsService.deleteGlxTorrentTagsByIds(ids));
    }
}
