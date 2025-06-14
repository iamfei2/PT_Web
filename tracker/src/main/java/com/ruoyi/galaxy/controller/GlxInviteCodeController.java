package com.ruoyi.galaxy.controller;

import java.util.List;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.galaxy.service.IGlxPointsRecordService;
import com.ruoyi.galaxy.util.ConfigUtil;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.galaxy.domain.GlxInviteCode;
import com.ruoyi.galaxy.service.IGlxInviteCodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 邀请注册Controller
 * 
 * @author HexPang
 * @date 2021-03-21
 */
@RestController
@RequestMapping("/galaxy/invite")
public class GlxInviteCodeController extends BaseController
{
    @Autowired
    private IGlxInviteCodeService glxInviteCodeService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IGlxPointsRecordService pointsRecordService;

    /**
     * 查询邀请注册列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:list')")
    @GetMapping("/list")
    public TableDataInfo list(GlxInviteCode glxInviteCode)
    {
        startPage();
//        if (!getUser().isAdmin()) {
//            glxInviteCode.setUserId(getUser().getUserId());
//        }
        List<GlxInviteCode> list = glxInviteCodeService.selectGlxInviteCodeList(glxInviteCode);
        return getDataTable(list);
    }

    /**
     * 导出邀请注册列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:export')")
    @Log(title = "邀请注册", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GlxInviteCode glxInviteCode)
    {
//        if (!getUser().isAdmin()) {
//            glxInviteCode.setUserId(getUser().getUserId());
//        }
        List<GlxInviteCode> list = glxInviteCodeService.selectGlxInviteCodeList(glxInviteCode);
        ExcelUtil<GlxInviteCode> util = new ExcelUtil<GlxInviteCode>(GlxInviteCode.class);
        return util.exportExcel(list, "invite");
    }

    @PreAuthorize("@ss.hasPermi('galaxy:invite:buy')")
    @PostMapping("/purchase")
    public AjaxResult confirmPurchase() {
        AjaxResult result = AjaxResult.success();
        Double needPoints = Double.parseDouble(configService.selectConfigByKey("sys.user.invite.points"));
        Double remainPoints = userService.selectUserById(getUser().getUserId()).getPoints();
        if (remainPoints < needPoints) {
            return AjaxResult.error("可用积分不足.");
        }
        pointsRecordService.addPointToUser(getUser().getUserId(), -needPoints, "邀请码购买");
        GlxInviteCode inviteCode = new GlxInviteCode();
        inviteCode.setUserId(getUser().getUserId());
        inviteCode.setInviteCode(UUID.fastUUID().toString());
        inviteCode.setStatus("0");
        glxInviteCodeService.insertGlxInviteCode(inviteCode);
        result.put("remainingPoints", (remainPoints - needPoints));
        return result;
    }

    @PreAuthorize("@ss.hasPermi('galaxy:invite:buy')")
    @GetMapping("/purchase")
    public AjaxResult getPurchase() {
        AjaxResult result = AjaxResult.success();
        result.put("inviteOnly", configService.selectConfigByKey("sys.user.invite.only"));
        result.put("inviteCodePoints", configService.selectConfigByKey("sys.user.invite.points"));
        result.put("remainingPoints", userService.selectUserById(getUser().getUserId()).getPoints());
        return result;
    }

    /**
     * 获取邀请注册详细信息
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        GlxInviteCode inviteCode = glxInviteCodeService.selectGlxInviteCodeById(id);
        if (!getUser().isAdmin()) {
            if (!inviteCode.getUserId().equals(getUser().getUserId())) {
                return AjaxResult.error("数据不存在");
            }
        }
        return AjaxResult.success(inviteCode);
    }

    /**
     * 新增邀请注册
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:add')")
    @Log(title = "邀请注册", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GlxInviteCode glxInviteCode)
    {
        return toAjax(glxInviteCodeService.insertGlxInviteCode(glxInviteCode));
    }

    /**
     * 修改邀请注册
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:edit')")
    @Log(title = "邀请注册", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GlxInviteCode glxInviteCode)
    {
        GlxInviteCode inviteCode = glxInviteCodeService.selectGlxInviteCodeById(glxInviteCode.getId());
        if (!getUser().isAdmin()) {
            if (!inviteCode.getUserId().equals(getUser().getUserId())) {
                return AjaxResult.error("数据不存在");
            }
        }
        return toAjax(glxInviteCodeService.updateGlxInviteCode(glxInviteCode));
    }

    /**
     * 删除邀请注册
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:remove')")
    @Log(title = "邀请注册", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(glxInviteCodeService.deleteGlxInviteCodeByIds(ids));
    }
}
