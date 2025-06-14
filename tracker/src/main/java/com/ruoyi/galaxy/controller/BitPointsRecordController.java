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
import com.ruoyi.galaxy.domain.BitPointsRecord;
import com.ruoyi.galaxy.service.IBitPointsRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/galaxy/point_record")
public class BitPointsRecordController extends BaseController
{
    @Autowired
    private IBitPointsRecordService glxPointsRecordService;

    /**
     * 查询积分明细列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(BitPointsRecord bitPointsRecord)
    {
        if (!getUser().isAdmin()) {
            bitPointsRecord.setUserId(getUser().getUserId());
        }
        startPage();
        List<BitPointsRecord> list = glxPointsRecordService.selectGlxPointsRecordList(bitPointsRecord);
        return getDataTable(list);
    }

    /**
     * 导出积分明细列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:export')")
    @Log(title = "积分明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BitPointsRecord bitPointsRecord)
    {
        List<BitPointsRecord> list = glxPointsRecordService.selectGlxPointsRecordList(bitPointsRecord);
        ExcelUtil<BitPointsRecord> util = new ExcelUtil<BitPointsRecord>(BitPointsRecord.class);
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
    public AjaxResult add(@RequestBody BitPointsRecord bitPointsRecord)
    {
        return toAjax(glxPointsRecordService.insertGlxPointsRecord(bitPointsRecord));
    }

    /**
     * 修改积分明细
     */
    @PreAuthorize("@ss.hasPermi('galaxy:point_record:edit')")
    @Log(title = "积分明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BitPointsRecord bitPointsRecord)
    {
        return toAjax(glxPointsRecordService.updateGlxPointsRecord(bitPointsRecord));
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
