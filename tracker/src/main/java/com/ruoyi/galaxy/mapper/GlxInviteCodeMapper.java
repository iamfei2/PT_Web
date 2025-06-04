package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxInviteCode;

/**
 * 邀请注册Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-21
 */
public interface GlxInviteCodeMapper 
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
     * 删除邀请注册
     * 
     * @param id 邀请注册ID
     * @return 结果
     */
    public int deleteGlxInviteCodeById(Long id);

    /**
     * 批量删除邀请注册
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxInviteCodeByIds(Long[] ids);

    /**
     * 通过邀请码查找
     * @param inviteCode
     * @return
     */
    public GlxInviteCode selectGlxInviteCodeByCode(String inviteCode);
}
