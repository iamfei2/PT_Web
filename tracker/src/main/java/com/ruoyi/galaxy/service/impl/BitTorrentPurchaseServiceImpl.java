package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitTorrentPurchaseMapper;
import com.ruoyi.galaxy.domain.BitTorrentPurchase;
import com.ruoyi.galaxy.service.IBitTorrentPurchaseService;

/**
 * 购买记录Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class BitTorrentPurchaseServiceImpl implements IBitTorrentPurchaseService
{
    @Autowired
    private BitTorrentPurchaseMapper bitTorrentPurchaseMapper;

    /**
     * 查询购买记录
     * 
     * @param id 购买记录ID
     * @return 购买记录
     */
    @Override
    public BitTorrentPurchase selectGlxTorrentPurchaseById(Long id)
    {
        return bitTorrentPurchaseMapper.selectGlxTorrentPurchaseById(id);
    }

    /**
     * 查询购买记录列表
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 购买记录
     */
    @Override
    public List<BitTorrentPurchase> selectGlxTorrentPurchaseList(BitTorrentPurchase bitTorrentPurchase)
    {
        return bitTorrentPurchaseMapper.selectGlxTorrentPurchaseList(bitTorrentPurchase);
    }

    /**
     * 新增购买记录
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 结果
     */
    @Override
    public int insertGlxTorrentPurchase(BitTorrentPurchase bitTorrentPurchase)
    {
        bitTorrentPurchase.setCreateTime(DateUtils.getNowDate());
        return bitTorrentPurchaseMapper.insertGlxTorrentPurchase(bitTorrentPurchase);
    }

    /**
     * 修改购买记录
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 结果
     */
    @Override
    public int updateGlxTorrentPurchase(BitTorrentPurchase bitTorrentPurchase)
    {
        return bitTorrentPurchaseMapper.updateGlxTorrentPurchase(bitTorrentPurchase);
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
        return bitTorrentPurchaseMapper.deleteGlxTorrentPurchaseByIds(ids);
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
        return bitTorrentPurchaseMapper.deleteGlxTorrentPurchaseById(id);
    }
}
