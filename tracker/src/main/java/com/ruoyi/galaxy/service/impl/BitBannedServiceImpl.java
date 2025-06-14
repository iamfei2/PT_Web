package com.ruoyi.galaxy.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitBannedMapper;
import com.ruoyi.galaxy.domain.BitBanned;
import com.ruoyi.galaxy.service.IBitBannedService;

/**
 * 小黑屋Service业务层处理
 * 
 * @author Hex
 * @date 2021-11-17
 */
@Service
public class BitBannedServiceImpl implements IBitBannedService
{
    @Autowired
    private BitBannedMapper bitBannedMapper;

    @Autowired
    private SysUserServiceImpl userService;

    /**
     * 查询小黑屋
     * 
     * @param id 小黑屋ID
     * @return 小黑屋
     */
    @Override
    public BitBanned selectGlxBannedById(Long id)
    {
        return bitBannedMapper.selectGlxBannedById(id);
    }

    /**
     * 查询小黑屋列表
     * 
     * @param bitBanned 小黑屋
     * @return 小黑屋
     */
    @Override
    public List<BitBanned> selectGlxBannedList(BitBanned bitBanned)
    {
        return bitBannedMapper.selectGlxBannedList(bitBanned);
    }

    /**
     * 新增小黑屋
     * 
     * @param bitBanned 小黑屋
     * @return 结果
     */
    @Override
    public int insertGlxBanned(BitBanned bitBanned)
    {
        bitBanned.setCreateTime(DateUtils.getNowDate());
        return bitBannedMapper.insertGlxBanned(bitBanned);
    }

    /**
     * 修改小黑屋
     * 
     * @param bitBanned 小黑屋
     * @return 结果
     */
    @Override
    public int updateGlxBanned(BitBanned bitBanned)
    {
        bitBanned.setUpdateTime(DateUtils.getNowDate());
        return bitBannedMapper.updateGlxBanned(bitBanned);
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
        return bitBannedMapper.deleteGlxBannedByIds(ids);
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
        return bitBannedMapper.deleteGlxBannedById(id);
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
        BitBanned ban = new BitBanned();
        ban.setUserId(user.getUserId());
        ban.setStatus("1");
        ban.setAppeal("N");
        ban.setExpire(banExpire);
        ban.setReason(reason);
        return bitBannedMapper.insertGlxBanned(ban);
    }
}
