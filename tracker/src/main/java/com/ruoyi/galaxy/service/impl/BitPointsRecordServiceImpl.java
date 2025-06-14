package com.ruoyi.galaxy.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitPointsRecordMapper;
import com.ruoyi.galaxy.domain.BitPointsRecord;
import com.ruoyi.galaxy.service.IBitPointsRecordService;

/**
 * 积分明细Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class BitPointsRecordServiceImpl implements IBitPointsRecordService
{
    @Autowired
    private BitPointsRecordMapper bitPointsRecordMapper;
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询积分明细
     * 
     * @param id 积分明细ID
     * @return 积分明细
     */
    @Override
    public BitPointsRecord selectGlxPointsRecordById(Long id)
    {
        return bitPointsRecordMapper.selectGlxPointsRecordById(id);
    }

    /**
     * 查询积分明细列表
     * 
     * @param bitPointsRecord 积分明细
     * @return 积分明细
     */
    @Override
    public List<BitPointsRecord> selectGlxPointsRecordList(BitPointsRecord bitPointsRecord)
    {
        return bitPointsRecordMapper.selectGlxPointsRecordList(bitPointsRecord);
    }

    /**
     * 新增积分明细
     * 
     * @param bitPointsRecord 积分明细
     * @return 结果
     */
    @Override
    public int insertGlxPointsRecord(BitPointsRecord bitPointsRecord)
    {
        SysUser user = new SysUser();
        user.setUserId(bitPointsRecord.getUserId());
        user.setPoints(bitPointsRecord.getPoints());
        userMapper.addPointsToUser(user);
        bitPointsRecord.setCreateTime(new Date());
        return bitPointsRecordMapper.insertGlxPointsRecord(bitPointsRecord);
    }

    /**
     * 修改积分明细
     * 
     * @param bitPointsRecord 积分明细
     * @return 结果
     */
    @Override
    public int updateGlxPointsRecord(BitPointsRecord bitPointsRecord)
    {
        return bitPointsRecordMapper.updateGlxPointsRecord(bitPointsRecord);
    }

    /**
     * 批量删除积分明细
     * 
     * @param ids 需要删除的积分明细ID
     * @return 结果
     */
    @Override
    public int deleteGlxPointsRecordByIds(Long[] ids)
    {
        return bitPointsRecordMapper.deleteGlxPointsRecordByIds(ids);
    }

    /**
     * 删除积分明细信息
     * 
     * @param id 积分明细ID
     * @return 结果
     */
    @Override
    public int deleteGlxPointsRecordById(Long id)
    {
        return bitPointsRecordMapper.deleteGlxPointsRecordById(id);
    }

    @Override
    public void addPointToUser(Long userId, Double points, String reason) {
        BitPointsRecord bitPointsRecord = new BitPointsRecord();
        bitPointsRecord.setPoints(points);
        bitPointsRecord.setUserId(userId);
        bitPointsRecord.setRemark(reason);
        this.insertGlxPointsRecord(bitPointsRecord);
    }
}
