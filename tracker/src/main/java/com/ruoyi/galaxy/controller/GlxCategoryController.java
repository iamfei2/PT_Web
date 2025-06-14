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
import com.ruoyi.galaxy.domain.GlxCategory;
import com.ruoyi.galaxy.service.IGlxCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 分享类别Controller
 * 
 * @author Hex
 * @date 2021-03-17
 */
@RestController
@RequestMapping("/galaxy/category")
public class GlxCategoryController extends BaseController
{
    @Autowired
    private IGlxCategoryService glxCategoryService;

    /**
     * 查询分享类别列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:list')")
    @GetMapping("/list")
    public AjaxResult list(GlxCategory glxCategory)
    {
        List<GlxCategory> list = glxCategoryService.selectGlxCategoryList(glxCategory);
        return AjaxResult.success(list);
    }

    /**
     * 导出分享类别列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:export')")
    @Log(title = "分享类别", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxCategory glxCategory)
    {
        List<GlxCategory> list = glxCategoryService.selectGlxCategoryList(glxCategory);
        ExcelUtil<GlxCategory> util = new ExcelUtil<GlxCategory>(GlxCategory.class);
        return util.exportExcel(list, "category");
    }

    /**
     * 获取分享类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxCategoryService.selectGlxCategoryById(id));
    }

    /**
     * 新增分享类别
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:add')")
    @Log(title = "分享类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxCategory glxCategory)
    {
        return toAjax(glxCategoryService.insertGlxCategory(glxCategory));
    }

    /**
     * 修改分享类别
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:edit')")
    @Log(title = "分享类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxCategory glxCategory)
    {
        return toAjax(glxCategoryService.updateGlxCategory(glxCategory));
    }

    /**
     * 删除分享类别
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:remove')")
    @Log(title = "分享类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxCategoryService.deleteGlxCategoryByIds(ids));
    }
}
