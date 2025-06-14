package com.ruoyi.galaxy.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.galaxy.domain.BitBanned;

public interface IBitBannedService
{
    /**
     * 查询小黑屋
     * 
     * @param id 小黑屋ID
     * @return 小黑屋
     */
    public BitBanned selectGlxBannedById(Long id);

    /**
     * 查询小黑屋列表
     * 
     * @param bitBanned 小黑屋
     * @return 小黑屋集合
     */
    public List<BitBanned> selectGlxBannedList(BitBanned bitBanned);

    /**
     * 新增小黑屋
     * 
     * @param bitBanned 小黑屋
     * @return 结果
     */
    public int insertGlxBanned(BitBanned bitBanned);

    /**
     * 修改小黑屋
     * 
     * @param bitBanned 小黑屋
     * @return 结果
     */
    public int updateGlxBanned(BitBanned bitBanned);

    /**
     * 批量删除小黑屋
     * 
     * @param ids 需要删除的小黑屋ID
     * @return 结果
     */
    public int deleteGlxBannedByIds(Long[] ids);

    /**
     * 删除小黑屋信息
     * 
     * @param id 小黑屋ID
     * @return 结果
     */
    public int deleteGlxBannedById(Long id);

    /**
     * 封禁用户
     * @param user
     * @param hours
     * @param reason
     * @return
     */
    public int Ban(SysUser user, int hours, String reason);
}
