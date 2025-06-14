package com.ruoyi.galaxy.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxPointsRecordMapper;
import com.ruoyi.galaxy.domain.GlxPointsRecord;
import com.ruoyi.galaxy.service.IGlxPointsRecordService;

/**
 * 积分明细Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class GlxPointsRecordServiceImpl implements IGlxPointsRecordService 
{
    @Autowired
    private GlxPointsRecordMapper glxPointsRecordMapper;
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询积分明细
     * 
     * @param id 积分明细ID
     * @return 积分明细
     */
    @Override
    public GlxPointsRecord selectGlxPointsRecordById(Long id)
    {
        return glxPointsRecordMapper.selectGlxPointsRecordById(id);
    }

    /**
     * 查询积分明细列表
     * 
     * @param glxPointsRecord 积分明细
     * @return 积分明细
     */
    @Override
    public List<GlxPointsRecord> selectGlxPointsRecordList(GlxPointsRecord glxPointsRecord)
    {
        return glxPointsRecordMapper.selectGlxPointsRecordList(glxPointsRecord);
    }

    /**
     * 新增积分明细
     * 
     * @param glxPointsRecord 积分明细
     * @return 结果
     */
    @Override
    public int insertGlxPointsRecord(GlxPointsRecord glxPointsRecord)
    {
        SysUser user = new SysUser();
        user.setUserId(glxPointsRecord.getUserId());
        user.setPoints(glxPointsRecord.getPoints());
        userMapper.addPointsToUser(user);
        glxPointsRecord.setCreateTime(new Date());
        return glxPointsRecordMapper.insertGlxPointsRecord(glxPointsRecord);
    }

    /**
     * 修改积分明细
     * 
     * @param glxPointsRecord 积分明细
     * @return 结果
     */
    @Override
    public int updateGlxPointsRecord(GlxPointsRecord glxPointsRecord)
    {
        return glxPointsRecordMapper.updateGlxPointsRecord(glxPointsRecord);
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
        return glxPointsRecordMapper.deleteGlxPointsRecordByIds(ids);
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
        return glxPointsRecordMapper.deleteGlxPointsRecordById(id);
    }

    @Override
    public void addPointToUser(Long userId, Double points, String reason) {
        GlxPointsRecord glxPointsRecord = new GlxPointsRecord();
        glxPointsRecord.setPoints(points);
        glxPointsRecord.setUserId(userId);
        glxPointsRecord.setRemark(reason);
        this.insertGlxPointsRecord(glxPointsRecord);
    }
}
