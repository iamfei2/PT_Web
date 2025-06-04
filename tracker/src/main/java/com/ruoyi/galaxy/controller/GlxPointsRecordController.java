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
import com.ruoyi.galaxy.domain.GlxPointsRecord;
import com.ruoyi.galaxy.service.IGlxPointsRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 积分明细Controller
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@RestController
@RequestMapping("/galaxy/point_record")
public class GlxPointsRecordController extends BaseController
{
    @Autowired
    private IGlxPointsRecordService glxPointsRecordService;

    /**
     * 查询积分明细列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxPointsRecord glxPointsRecord)
    {
        if (!getUser().isAdmin()) {
            glxPointsRecord.setUserId(getUser().getUserId());
        }
        startPage();
        List<GlxPointsRecord> list = glxPointsRecordService.selectGlxPointsRecordList(glxPointsRecord);
        return getDataTable(list);
    }

    /**
     * 导出积分明细列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:export')")
    @Log(title = "积分明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxPointsRecord glxPointsRecord)
    {
        List<GlxPointsRecord> list = glxPointsRecordService.selectGlxPointsRecordList(glxPointsRecord);
        ExcelUtil<GlxPointsRecord> util = new ExcelUtil<GlxPointsRecord>(GlxPointsRecord.class);
        return util.exportExcel(list, "point_record");
    }

    /**
     * 获取积分明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxPointsRecordService.selectGlxPointsRecordById(id));
    }

    /**
     * 新增积分明细
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:add')")
    @Log(title = "积分明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxPointsRecord glxPointsRecord)
    {
        return toAjax(glxPointsRecordService.insertGlxPointsRecord(glxPointsRecord));
    }

    /**
     * 修改积分明细
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:edit')")
    @Log(title = "积分明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxPointsRecord glxPointsRecord)
    {
        return toAjax(glxPointsRecordService.updateGlxPointsRecord(glxPointsRecord));
    }

    /**
     * 删除积分明细
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:remove')")
    @Log(title = "积分明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxPointsRecordService.deleteGlxPointsRecordByIds(ids));
    }
}
