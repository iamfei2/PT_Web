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
import com.ruoyi.galaxy.domain.BitTorrentPurchase;
import com.ruoyi.galaxy.service.IBitTorrentPurchaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/galaxy/torrent_purchase")
public class BitTorrentPurchaseController extends BaseController
{
    @Autowired
    private IBitTorrentPurchaseService glxTorrentPurchaseService;

    /**
     * 查询购买记录列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(BitTorrentPurchase bitTorrentPurchase)
    {
        if (!getUser().isAdmin()) {
            bitTorrentPurchase.setUserId(getUser().getUserId());
        }
        startPage();
        List<BitTorrentPurchase> list = glxTorrentPurchaseService.selectGlxTorrentPurchaseList(bitTorrentPurchase);

        return getDataTable(list);
    }

    /**
     * 导出购买记录列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:export')")
    @Log(title = "购买记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BitTorrentPurchase bitTorrentPurchase)
    {
        if (!getUser().isAdmin()) {
            bitTorrentPurchase.setUserId(getUser().getUserId());
        }
        List<BitTorrentPurchase> list = glxTorrentPurchaseService.selectGlxTorrentPurchaseList(bitTorrentPurchase);
        ExcelUtil<BitTorrentPurchase> util = new ExcelUtil<BitTorrentPurchase>(BitTorrentPurchase.class);
        return util.exportExcel(list, "torrent_purchase");
    }

    /**
     * 获取购买记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        BitTorrentPurchase purchase = glxTorrentPurchaseService.selectGlxTorrentPurchaseById(id);
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
    public AjaxResult add(@RequestBody BitTorrentPurchase bitTorrentPurchase)
    {
        return toAjax(glxTorrentPurchaseService.insertGlxTorrentPurchase(bitTorrentPurchase));
    }

    /**
     * 修改购买记录
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_purchase:edit')")
    @Log(title = "购买记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BitTorrentPurchase bitTorrentPurchase)
    {
        return toAjax(glxTorrentPurchaseService.updateGlxTorrentPurchase(bitTorrentPurchase));
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
