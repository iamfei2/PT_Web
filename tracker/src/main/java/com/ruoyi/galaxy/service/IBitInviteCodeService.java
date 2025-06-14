package com.ruoyi.galaxy.service;

import java.util.List;

import com.ruoyi.galaxy.domain.BitInviteCode;


public interface IBitInviteCodeService
{
    /**
     * 查询邀请注册
     * 
     * @param id 邀请注册ID
     * @return 邀请注册
     */
    public BitInviteCode selectGlxInviteCodeById(Long id);

    /**
     * 查询邀请注册列表
     * 
     * @param bitInviteCode 邀请注册
     * @return 邀请注册集合
     */
    public List<BitInviteCode> selectGlxInviteCodeList(BitInviteCode bitInviteCode);

    /**
     * 新增邀请注册
     * 
     * @param bitInviteCode 邀请注册
     * @return 结果
     */
    public int insertGlxInviteCode(BitInviteCode bitInviteCode);

    /**
     * 修改邀请注册
     * 
     * @param bitInviteCode 邀请注册
     * @return 结果
     */
    public int updateGlxInviteCode(BitInviteCode bitInviteCode);

    /**
     * 批量删除邀请注册
     * 
     * @param ids 需要删除的邀请注册ID
     * @return 结果
     */
    public int deleteGlxInviteCodeByIds(Long[] ids);

    /**
     * 删除邀请注册信息
     * 
     * @param id 邀请注册ID
     * @return 结果
     */
    public int deleteGlxInviteCodeById(Long id);

    public BitInviteCode selectGlxInviteCodeByCode(String inviteCode);
}
