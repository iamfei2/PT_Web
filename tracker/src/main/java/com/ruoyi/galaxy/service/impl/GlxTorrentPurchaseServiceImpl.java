package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxTorrentPurchaseMapper;
import com.ruoyi.galaxy.domain.GlxTorrentPurchase;
import com.ruoyi.galaxy.service.IGlxTorrentPurchaseService;

/**
 * 购买记录Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class GlxTorrentPurchaseServiceImpl implements IGlxTorrentPurchaseService 
{
    @Autowired
    private GlxTorrentPurchaseMapper glxTorrentPurchaseMapper;

    /**
     * 查询购买记录
     * 
     * @param id 购买记录ID
     * @return 购买记录
     */
    @Override
    public GlxTorrentPurchase selectGlxTorrentPurchaseById(Long id)
    {
        return glxTorrentPurchaseMapper.selectGlxTorrentPurchaseById(id);
    }

    /**
     * 查询购买记录列表
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 购买记录
     */
    @Override
    public List<GlxTorrentPurchase> selectGlxTorrentPurchaseList(GlxTorrentPurchase glxTorrentPurchase)
    {
        return glxTorrentPurchaseMapper.selectGlxTorrentPurchaseList(glxTorrentPurchase);
    }

    /**
     * 新增购买记录
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 结果
     */
    @Override
    public int insertGlxTorrentPurchase(GlxTorrentPurchase glxTorrentPurchase)
    {
        glxTorrentPurchase.setCreateTime(DateUtils.getNowDate());
        return glxTorrentPurchaseMapper.insertGlxTorrentPurchase(glxTorrentPurchase);
    }

    /**
     * 修改购买记录
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 结果
     */
    @Override
    public int updateGlxTorrentPurchase(GlxTorrentPurchase glxTorrentPurchase)
    {
        return glxTorrentPurchaseMapper.updateGlxTorrentPurchase(glxTorrentPurchase);
    }

    /**
     * 批量删除购买记录
     * 
     * @param ids 需要删除的购买记录ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentPurchaseByIds(Long[] ids)
    {
        return glxTorrentPurchaseMapper.deleteGlxTorrentPurchaseByIds(ids);
    }

    /**
     * 删除购买记录信息
     * 
     * @param id 购买记录ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentPurchaseById(Long id)
    {
        return glxTorrentPurchaseMapper.deleteGlxTorrentPurchaseById(id);
    }
}
