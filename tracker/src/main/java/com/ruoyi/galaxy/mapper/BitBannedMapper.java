package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.BitBanned;

public interface BitBannedMapper
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
     * 删除小黑屋
     * 
     * @param id 小黑屋ID
     * @return 结果
     */
    public int deleteGlxBannedById(Long id);

    /**
     * 批量删除小黑屋
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxBannedByIds(Long[] ids);
}
