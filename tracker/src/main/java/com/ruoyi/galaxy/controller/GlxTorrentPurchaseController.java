package com.ruoyi.galaxy.controller;

import java.util.List;

import com.ruoyi.galaxy.service.IGlxTorrentService;
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
import com.ruoyi.galaxy.domain.GlxTorrentPurchase;
import com.ruoyi.galaxy.service.IGlxTorrentPurchaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 购买记录Controller
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@RestController
@RequestMapping("/galaxy/torrent_purchase")
public class GlxTorrentPurchaseController extends BaseController
{
    @Autowired
    private IGlxTorrentPurchaseService glxTorrentPurchaseService;

    /**
     * 查询购买记录列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxTorrentPurchase glxTorrentPurchase)
    {
        if (!getUser().isAdmin()) {
            glxTorrentPurchase.setUserId(getUser().getUserId());
        }
        startPage();
        List<GlxTorrentPurchase> list = glxTorrentPurchaseService.selectGlxTorrentPurchaseList(glxTorrentPurchase);

        return getDataTable(list);
    }

    /**
     * 导出购买记录列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:export')")
    @Log(title = "购买记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxTorrentPurchase glxTorrentPurchase)
    {
        if (!getUser().isAdmin()) {
            glxTorrentPurchase.setUserId(getUser().getUserId());
        }
        List<GlxTorrentPurchase> list = glxTorrentPurchaseService.selectGlxTorrentPurchaseList(glxTorrentPurchase);
        ExcelUtil<GlxTorrentPurchase> util = new ExcelUtil<GlxTorrentPurchase>(GlxTorrentPurchase.class);
        return util.exportExcel(list, "torrent_purchase");
    }

    /**
     * 获取购买记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        GlxTorrentPurchase purchase = glxTorrentPurchaseService.selectGlxTorrentPurchaseById(id);
        if (purchase.getUserId() != getUser().getUserId()) {
            return AjaxResult.error("信息不存在或已删除");
        }
        return AjaxResult.success(purchase);
    }

    /**
     * 新增购买记录
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:add')")
    @Log(title = "购买记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxTorrentPurchase glxTorrentPurchase)
    {
        return toAjax(glxTorrentPurchaseService.insertGlxTorrentPurchase(glxTorrentPurchase));
    }

    /**
     * 修改购买记录
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:edit')")
    @Log(title = "购买记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxTorrentPurchase glxTorrentPurchase)
    {
        return toAjax(glxTorrentPurchaseService.updateGlxTorrentPurchase(glxTorrentPurchase));
    }

    /**
     * 删除购买记录
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:remove')")
    @Log(title = "购买记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxTorrentPurchaseService.deleteGlxTorrentPurchaseByIds(ids));
    }
}
