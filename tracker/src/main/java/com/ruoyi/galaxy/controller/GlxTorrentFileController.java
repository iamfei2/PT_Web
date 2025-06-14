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
import com.ruoyi.galaxy.domain.GlxTorrentFile;
import com.ruoyi.galaxy.service.IGlxTorrentFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 种子文件Controller
 * 
 * @author HexPang
 * @date 2021-03-19
 */
@RestController
@RequestMapping("/galaxy/torrent_file")
public class GlxTorrentFileController extends BaseController
{
    @Autowired
    private IGlxTorrentFileService glxTorrentFileService;

    /**
     * 查询种子文件列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxTorrentFile glxTorrentFile)
    {
        startPage();
        List<GlxTorrentFile> list = glxTorrentFileService.selectGlxTorrentFileList(glxTorrentFile);
        return getDataTable(list);
    }

    /**
     * 导出种子文件列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:export')")
    @Log(title = "种子文件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxTorrentFile glxTorrentFile)
    {
        List<GlxTorrentFile> list = glxTorrentFileService.selectGlxTorrentFileList(glxTorrentFile);
        ExcelUtil<GlxTorrentFile> util = new ExcelUtil<GlxTorrentFile>(GlxTorrentFile.class);
        return util.exportExcel(list, "torrent_file");
    }

    /**
     * 获取种子文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxTorrentFileService.selectGlxTorrentFileById(id));
    }

    /**
     * 新增种子文件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:add')")
    @Log(title = "种子文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxTorrentFile glxTorrentFile)
    {
        return toAjax(glxTorrentFileService.insertGlxTorrentFile(glxTorrentFile));
    }

    /**
     * 修改种子文件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:edit')")
    @Log(title = "种子文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxTorrentFile glxTorrentFile)
    {
        return toAjax(glxTorrentFileService.updateGlxTorrentFile(glxTorrentFile));
    }

    /**
     * 删除种子文件
     */
    @PreAuthorize("@ss.hasPermi('galaxy:torrent_file:remove')")
    @Log(title = "种子文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxTorrentFileService.deleteGlxTorrentFileByIds(ids));
    }
}
