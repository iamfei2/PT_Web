package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxBanned;

/**
 * 小黑屋Mapper接口
 * 
 * @author Hex
 * @date 2021-11-17
 */
public interface GlxBannedMapper 
{
    /**
     * 查询小黑屋
     * 
     * @param id 小黑屋ID
     * @return 小黑屋
     */
    public GlxBanned selectGlxBannedById(Long id);

    /**
     * 查询小黑屋列表
     * 
     * @param glxBanned 小黑屋
     * @return 小黑屋集合
     */
    public List<GlxBanned> selectGlxBannedList(GlxBanned glxBanned);

    /**
     * 新增小黑屋
     * 
     * @param glxBanned 小黑屋
     * @return 结果
     */
    public int insertGlxBanned(GlxBanned glxBanned);

    /**
     * 修改小黑屋
     * 
     * @param glxBanned 小黑屋
     * @return 结果
     */
    public int updateGlxBanned(GlxBanned glxBanned);

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
