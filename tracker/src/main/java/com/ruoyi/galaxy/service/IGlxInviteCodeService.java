package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxInviteCode;

/**
 * 邀请注册Service接口
 * 
 * @author HexPang
 * @date 2021-03-21
 */
public interface IGlxInviteCodeService 
{
    /**
     * 查询邀请注册
     * 
     * @param id 邀请注册ID
     * @return 邀请注册
     */
    public GlxInviteCode selectGlxInviteCodeById(Long id);

    /**
     * 查询邀请注册列表
     * 
     * @param glxInviteCode 邀请注册
     * @return 邀请注册集合
     */
    public List<GlxInviteCode> selectGlxInviteCodeList(GlxInviteCode glxInviteCode);

    /**
     * 新增邀请注册
     * 
     * @param glxInviteCode 邀请注册
     * @return 结果
     */
    public int insertGlxInviteCode(GlxInviteCode glxInviteCode);

    /**
     * 修改邀请注册
     * 
     * @param glxInviteCode 邀请注册
     * @return 结果
     */
    public int updateGlxInviteCode(GlxInviteCode glxInviteCode);

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

    public GlxInviteCode selectGlxInviteCodeByCode(String inviteCode);
}
