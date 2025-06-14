package com.ruoyi.galaxy.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxBannedMapper;
import com.ruoyi.galaxy.domain.GlxBanned;
import com.ruoyi.galaxy.service.IGlxBannedService;

/**
 * 小黑屋Service业务层处理
 * 
 * @author Hex
 * @date 2021-11-17
 */
@Service
public class GlxBannedServiceImpl implements IGlxBannedService 
{
    @Autowired
    private GlxBannedMapper glxBannedMapper;

    @Autowired
    private SysUserServiceImpl userService;

    /**
     * 查询小黑屋
     * 
     * @param id 小黑屋ID
     * @return 小黑屋
     */
    @Override
    public GlxBanned selectGlxBannedById(Long id)
    {
        return glxBannedMapper.selectGlxBannedById(id);
    }

    /**
     * 查询小黑屋列表
     * 
     * @param glxBanned 小黑屋
     * @return 小黑屋
     */
    @Override
    public List<GlxBanned> selectGlxBannedList(GlxBanned glxBanned)
    {
        return glxBannedMapper.selectGlxBannedList(glxBanned);
    }

    /**
     * 新增小黑屋
     * 
     * @param glxBanned 小黑屋
     * @return 结果
     */
    @Override
    public int insertGlxBanned(GlxBanned glxBanned)
    {
        glxBanned.setCreateTime(DateUtils.getNowDate());
        return glxBannedMapper.insertGlxBanned(glxBanned);
    }

    /**
     * 修改小黑屋
     * 
     * @param glxBanned 小黑屋
     * @return 结果
     */
    @Override
    public int updateGlxBanned(GlxBanned glxBanned)
    {
        glxBanned.setUpdateTime(DateUtils.getNowDate());
        return glxBannedMapper.updateGlxBanned(glxBanned);
    }

    /**
     * 批量删除小黑屋
     * 
     * @param ids 需要删除的小黑屋ID
     * @return 结果
     */
    @Override
    public int deleteGlxBannedByIds(Long[] ids)
    {
        return glxBannedMapper.deleteGlxBannedByIds(ids);
    }

    /**
     * 删除小黑屋信息
     * 
     * @param id 小黑屋ID
     * @return 结果
     */
    @Override
    public int deleteGlxBannedById(Long id)
    {
        return glxBannedMapper.deleteGlxBannedById(id);
    }

    @Override
    public int Ban(SysUser user, int hours, String reason) {
        if (user.getBanCount() == 0) {
            user.setBanCount(1);
        } else {
            user.setBanCount(user.getBanCount() + 1);
        }
        Date banExpire = DateUtils.addHours(DateUtils.getNowDate(), hours * user.getBanCount());
        user.setBanReason(reason);
        user.setBanExpire(banExpire);
        userService.safeUpdateUser(user);
        GlxBanned ban = new GlxBanned();
        ban.setUserId(user.getUserId());
        ban.setStatus("1");
        ban.setAppeal("N");
        ban.setExpire(banExpire);
        ban.setReason(reason);
        return glxBannedMapper.insertGlxBanned(ban);
    }
}
