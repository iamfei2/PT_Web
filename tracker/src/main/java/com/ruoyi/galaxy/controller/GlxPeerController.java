package com.ruoyi.galaxy.controller;

import java.util.List;

import com.ruoyi.galaxy.domain.GlxTorrent;
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
import com.ruoyi.galaxy.domain.GlxPeer;
import com.ruoyi.galaxy.service.IGlxPeerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 节点管理Controller
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@RestController
@RequestMapping("/galaxy/peer")
public class GlxPeerController extends BaseController
{
    @Autowired
    private IGlxPeerService glxPeerService;
    @Autowired
    private IGlxTorrentService torrentService;

    /**
     * 查询节点管理列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxPeer glxPeer)
    {
        startPage();
        if (!getUser().isAdmin()) {
            glxPeer.setUserId(getUser().getUserId());
        }
        List<GlxPeer> list = glxPeerService.selectGlxPeerList(glxPeer);
        list.forEach(x -> {
            GlxTorrent torrent = torrentService.selectGlxTorrentById(x.getTorrentId());
            if (torrent != null) {
                x.setTorrentTitle(torrent.getTitle());
            } else {
                x.setTorrentTitle("已删除");
            }
        });
        return getDataTable(list);
    }

    /**
     * 导出节点管理列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:export')")
    @Log(title = "节点管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxPeer glxPeer)
    {
        List<GlxPeer> list = glxPeerService.selectGlxPeerList(glxPeer);
        ExcelUtil<GlxPeer> util = new ExcelUtil<GlxPeer>(GlxPeer.class);
        return util.exportExcel(list, "peer");
    }

    /**
     * 获取节点管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxPeerService.selectGlxPeerById(id));
    }

    /**
     * 新增节点管理
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:add')")
    @Log(title = "节点管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxPeer glxPeer)
    {
        return toAjax(glxPeerService.insertGlxPeer(glxPeer));
    }

    /**
     * 修改节点管理
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:edit')")
    @Log(title = "节点管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxPeer glxPeer)
    {
        return toAjax(glxPeerService.updateGlxPeer(glxPeer));
    }

    /**
     * 删除节点管理
     */
    @PreAuthorize("@ss.hasPermi('galaxy:peer:remove')")
    @Log(title = "节点管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxPeerService.deleteGlxPeerByIds(ids));
    }
}
