package com.ruoyi.galaxy.controller;

import java.util.List;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.galaxy.domain.BitInviteCode;
import com.ruoyi.galaxy.service.IBitPointsRecordService;
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
import com.ruoyi.galaxy.service.IBitInviteCodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;


@RestController
@RequestMapping("/galaxy/invite")
public class BitInviteCodeController extends BaseController
{
    @Autowired
    private IBitInviteCodeService glxInviteCodeService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBitPointsRecordService pointsRecordService;

    /**
     * 查询邀请注册列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:list')")
    @GetMapping("/list")
    public TableDataInfo list(BitInviteCode bitInviteCode)
    {
        startPage();
//        if (!getUser().isAdmin()) {
//            bitInviteCode.setUserId(getUser().getUserId());
//        }
        List<BitInviteCode> list = glxInviteCodeService.selectGlxInviteCodeList(bitInviteCode);
        return getDataTable(list);
    }

    /**
     * 导出邀请注册列表
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:export')")
    @Log(title = "邀请注册", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BitInviteCode bitInviteCode)
    {
//        if (!getUser().isAdmin()) {
//            bitInviteCode.setUserId(getUser().getUserId());
//        }
        List<BitInviteCode> list = glxInviteCodeService.selectGlxInviteCodeList(bitInviteCode);
        ExcelUtil<BitInviteCode> util = new ExcelUtil<BitInviteCode>(BitInviteCode.class);
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
        BitInviteCode inviteCode = new BitInviteCode();
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
        BitInviteCode inviteCode = glxInviteCodeService.selectGlxInviteCodeById(id);
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
    public AjaxResult add(@RequestBody BitInviteCode bitInviteCode)
    {
        return toAjax(glxInviteCodeService.insertGlxInviteCode(bitInviteCode));
    }

    /**
     * 修改邀请注册
     */
    @PreAuthorize("@ss.hasPermi('galaxy:invite:edit')")
    @Log(title = "邀请注册", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BitInviteCode bitInviteCode)
    {
        BitInviteCode inviteCode = glxInviteCodeService.selectGlxInviteCodeById(bitInviteCode.getId());
        if (!getUser().isAdmin()) {
            if (!inviteCode.getUserId().equals(getUser().getUserId())) {
                return AjaxResult.error("数据不存在");
            }
        }
        return toAjax(glxInviteCodeService.updateGlxInviteCode(bitInviteCode));
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
