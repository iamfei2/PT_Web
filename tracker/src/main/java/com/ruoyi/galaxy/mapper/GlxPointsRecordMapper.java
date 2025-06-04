package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxPointsRecord;

/**
 * 积分明细Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public interface GlxPointsRecordMapper 
{
    /**
     * 查询积分明细
     * 
     * @param id 积分明细ID
     * @return 积分明细
     */
    public GlxPointsRecord selectGlxPointsRecordById(Long id);

    /**
     * 查询积分明细列表
     * 
     * @param glxPointsRecord 积分明细
     * @return 积分明细集合
     */
    public List<GlxPointsRecord> selectGlxPointsRecordList(GlxPointsRecord glxPointsRecord);

    /**
     * 新增积分明细
     * 
     * @param glxPointsRecord 积分明细
     * @return 结果
     */
    public int insertGlxPointsRecord(GlxPointsRecord glxPointsRecord);

    /**
     * 修改积分明细
     * 
     * @param glxPointsRecord 积分明细
     * @return 结果
     */
    public int updateGlxPointsRecord(GlxPointsRecord glxPointsRecord);

    /**
     * 删除积分明细
     * 
     * @param id 积分明细ID
     * @return 结果
     */
    public int deleteGlxPointsRecordById(Long id);

    /**
     * 批量删除积分明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxPointsRecordByIds(Long[] ids);
}
