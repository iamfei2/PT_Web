package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.BitPointsRecord;

public interface IBitPointsRecordService
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
     * 批量删除积分明细
     * 
     * @param ids 需要删除的积分明细ID
     * @return 结果
     */
    public int deleteGlxPointsRecordByIds(Long[] ids);

    /**
     * 删除积分明细信息
     * 
     * @param id 积分明细ID
     * @return 结果
     */
    public int deleteGlxPointsRecordById(Long id);

    /**
     * 给用户添加资源
     * @param userId
     * @param points
     * @param reason
     */
    public void addPointToUser(Long userId, Double points, String reason);
}
