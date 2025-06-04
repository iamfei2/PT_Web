package com.ruoyi.galaxy.controller;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
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
import com.ruoyi.galaxy.domain.GlxBanned;
import com.ruoyi.galaxy.service.IGlxBannedService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 小黑屋Controller
 * 
 * @author Hex
 * @date 2021-11-17
 */
@RestController
@RequestMapping("/galaxy/banned")
public class GlxBannedController extends BaseController
{
    @Autowired
    private IGlxBannedService glxBannedService;

    @Autowired
    private SysUserServiceImpl userService;

    /**
     * 查询小黑屋列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxBanned glxBanned)
    {
        startPage();
        glxBanned.setUserId(getUser().getUserId());
        List<GlxBanned> list = glxBannedService.selectGlxBannedList(glxBanned);
        return getDataTable(list);
    }

    /**
     * 导出小黑屋列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:export')")
    @Log(title = "小黑屋", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxBanned glxBanned)
    {
        glxBanned.setUserId(getUser().getUserId());
        List<GlxBanned> list = glxBannedService.selectGlxBannedList(glxBanned);
        ExcelUtil<GlxBanned> util = new ExcelUtil<GlxBanned>(GlxBanned.class);
        return util.exportExcel(list, "banned");
    }

    /**
     * 获取小黑屋详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(glxBannedService.selectGlxBannedById(id));
    }

    /**
     * 新增小黑屋
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:add')")
    @Log(title = "小黑屋", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxBanned glxBanned)
    {
        return toAjax(glxBannedService.insertGlxBanned(glxBanned));
    }

    /**
     * 修改小黑屋
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:edit')")
    @Log(title = "小黑屋", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxBanned glxBanned)
    {
        return toAjax(glxBannedService.updateGlxBanned(glxBanned));
    }

    /**
     * 申诉提交
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:appeal')")
    @Log(title = "小黑屋-申诉处理", businessType = BusinessType.UPDATE)
    @PostMapping("/appeal/create")
    public AjaxResult createAppeal(@RequestBody GlxBanned data) {
        GlxBanned banned = glxBannedService.selectGlxBannedById(data.getId());
        if (banned.equals(null)) {
            return AjaxResult.error("信息错误！");
        }
        if (banned.getAppeal().equalsIgnoreCase("y")) {
            return AjaxResult.error("请勿重复提交！");
        }
        banned.setAppealState("0");
        banned.setAppeal("Y");
        banned.setRemark(data.getRemark());
        glxBannedService.updateGlxBanned(banned);
        return AjaxResult.success("提交成功，请耐心等待处理。");
    }

    /**
     * 申诉处理
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:appeal')")
    @Log(title = "小黑屋-申诉处理", businessType = BusinessType.UPDATE)
    @PostMapping("/appeal/audit")
    public AjaxResult appeal(@RequestBody GlxBanned data) {
        GlxBanned banned = glxBannedService.selectGlxBannedById(data.getId());
        if (banned.getAppealState().equalsIgnoreCase("0")) {
            banned.setAppealResult(data.getAppealResult());
            banned.setAppealUserId(getUser().getUserId());
            banned.setAppealState(data.getAppealState());
            if (data.getAppealState().equalsIgnoreCase("1")) {
                //申诉通过
                SysUser user = userService.selectUserById(banned.getUserId());
                userService.unBanByUser(user);
            }
            glxBannedService.updateGlxBanned(banned);
        } else {
            return AjaxResult.error("信息状态不正确或已被处理。");
        }
        return AjaxResult.success("信息处理完成。");
    }

    /**
     * 删除小黑屋
     */
    @PreAuthorize("@ss.hasPermi('galaxy:banned:remove')")
    @Log(title = "小黑屋", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxBannedService.deleteGlxBannedByIds(ids));
    }
}
