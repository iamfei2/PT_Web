package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxInviteCodeMapper;
import com.ruoyi.galaxy.domain.GlxInviteCode;
import com.ruoyi.galaxy.service.IGlxInviteCodeService;

/**
 * 邀请注册Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-21
 */
@Service
public class GlxInviteCodeServiceImpl implements IGlxInviteCodeService 
{
    @Autowired
    private GlxInviteCodeMapper glxInviteCodeMapper;

    /**
     * 查询邀请注册
     * 
     * @param id 邀请注册ID
     * @return 邀请注册
     */
    @Override
    public GlxInviteCode selectGlxInviteCodeById(Long id)
    {
        return glxInviteCodeMapper.selectGlxInviteCodeById(id);
    }

    /**
     * 查询邀请注册列表
     * 
     * @param glxInviteCode 邀请注册
     * @return 邀请注册
     */
    @Override
    public List<GlxInviteCode> selectGlxInviteCodeList(GlxInviteCode glxInviteCode)
    {
        return glxInviteCodeMapper.selectGlxInviteCodeList(glxInviteCode);
    }

    /**
     * 新增邀请注册
     * 
     * @param glxInviteCode 邀请注册
     * @return 结果
     */
    @Override
    public int insertGlxInviteCode(GlxInviteCode glxInviteCode)
    {
        glxInviteCode.setCreateTime(DateUtils.getNowDate());
        return glxInviteCodeMapper.insertGlxInviteCode(glxInviteCode);
    }

    /**
     * 修改邀请注册
     * 
     * @param glxInviteCode 邀请注册
     * @return 结果
     */
    @Override
    public int updateGlxInviteCode(GlxInviteCode glxInviteCode)
    {
        glxInviteCode.setUpdateTime(DateUtils.getNowDate());
        return glxInviteCodeMapper.updateGlxInviteCode(glxInviteCode);
    }

    /**
     * 批量删除邀请注册
     * 
     * @param ids 需要删除的邀请注册ID
     * @return 结果
     */
    @Override
    public int deleteGlxInviteCodeByIds(Long[] ids)
    {
        return glxInviteCodeMapper.deleteGlxInviteCodeByIds(ids);
    }

    /**
     * 删除邀请注册信息
     * 
     * @param id 邀请注册ID
     * @return 结果
     */
    @Override
    public int deleteGlxInviteCodeById(Long id)
    {
        return glxInviteCodeMapper.deleteGlxInviteCodeById(id);
    }

    @Override
    public GlxInviteCode selectGlxInviteCodeByCode(String inviteCode) {
        return glxInviteCodeMapper.selectGlxInviteCodeByCode(inviteCode);
    }
}
