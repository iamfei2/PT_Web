package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.galaxy.domain.BitInviteCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitInviteCodeMapper;
import com.ruoyi.galaxy.service.IBitInviteCodeService;

/**
 * 邀请注册Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-21
 */
@Service
public class BitInviteCodeServiceImpl implements IBitInviteCodeService
{
    @Autowired
    private BitInviteCodeMapper bitInviteCodeMapper;

    /**
     * 查询邀请注册
     * 
     * @param id 邀请注册ID
     * @return 邀请注册
     */
    @Override
    public BitInviteCode selectGlxInviteCodeById(Long id)
    {
        return bitInviteCodeMapper.selectGlxInviteCodeById(id);
    }

    /**
     * 查询邀请注册列表
     * 
     * @param bitInviteCode 邀请注册
     * @return 邀请注册
     */
    @Override
    public List<BitInviteCode> selectGlxInviteCodeList(BitInviteCode bitInviteCode)
    {
        return bitInviteCodeMapper.selectGlxInviteCodeList(bitInviteCode);
    }

    /**
     * 新增邀请注册
     * 
     * @param bitInviteCode 邀请注册
     * @return 结果
     */
    @Override
    public int insertGlxInviteCode(BitInviteCode bitInviteCode)
    {
        bitInviteCode.setCreateTime(DateUtils.getNowDate());
        return bitInviteCodeMapper.insertGlxInviteCode(bitInviteCode);
    }

    /**
     * 修改邀请注册
     * 
     * @param bitInviteCode 邀请注册
     * @return 结果
     */
    @Override
    public int updateGlxInviteCode(BitInviteCode bitInviteCode)
    {
        bitInviteCode.setUpdateTime(DateUtils.getNowDate());
        return bitInviteCodeMapper.updateGlxInviteCode(bitInviteCode);
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
        return bitInviteCodeMapper.deleteGlxInviteCodeByIds(ids);
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
        return bitInviteCodeMapper.deleteGlxInviteCodeById(id);
    }

    @Override
    public BitInviteCode selectGlxInviteCodeByCode(String inviteCode) {
        return bitInviteCodeMapper.selectGlxInviteCodeByCode(inviteCode);
    }
}
