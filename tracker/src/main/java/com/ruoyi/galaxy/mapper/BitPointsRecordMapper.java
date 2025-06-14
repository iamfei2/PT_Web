package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.BitPointsRecord;

public interface BitPointsRecordMapper
{
    /**
     * 查询积分明细
     * 
     * @param id 积分明细ID
     * @return 积分明细
     */
    public BitPointsRecord selectGlxPointsRecordById(Long id);

    /**
     * 查询积分明细列表
     * 
     * @param bitPointsRecord 积分明细
     * @return 积分明细集合
     */
    public List<BitPointsRecord> selectGlxPointsRecordList(BitPointsRecord bitPointsRecord);

    /**
     * 新增积分明细
     * 
     * @param bitPointsRecord 积分明细
     * @return 结果
     */
    public int insertGlxPointsRecord(BitPointsRecord bitPointsRecord);

    /**
     * 修改积分明细
     * 
     * @param bitPointsRecord 积分明细
     * @return 结果
     */
    public int updateGlxPointsRecord(BitPointsRecord bitPointsRecord);

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
