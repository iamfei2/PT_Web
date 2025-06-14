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
import com.ruoyi.galaxy.domain.BitCategory;
import com.ruoyi.galaxy.service.IBitCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/galaxy/category")
public class BitCategoryController extends BaseController
{
    @Autowired
    private IBitCategoryService glxCategoryService;

    /**
     * 查询分享类别列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:list')")
    @GetMapping("/list")
    public AjaxResult list(BitCategory bitCategory)
    {
        List<BitCategory> list = glxCategoryService.selectGlxCategoryList(bitCategory);
        return AjaxResult.success(list);
    }

    /**
     * 导出分享类别列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:export')")
    @Log(title = "分享类别", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BitCategory bitCategory)
    {
        List<BitCategory> list = glxCategoryService.selectGlxCategoryList(bitCategory);
        ExcelUtil<BitCategory> util = new ExcelUtil<BitCategory>(BitCategory.class);
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
    public AjaxResult add(@RequestBody BitCategory bitCategory)
    {
        return toAjax(glxCategoryService.insertGlxCategory(bitCategory));
    }

    /**
     * 修改分享类别
     */
    @PreAuthorize("@ss.hasPermi('galaxy:category:edit')")
    @Log(title = "分享类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BitCategory bitCategory)
    {
        return toAjax(glxCategoryService.updateGlxCategory(bitCategory));
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
