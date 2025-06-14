package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.BitTorrentPurchase;
public interface IBitTorrentPurchaseService
{
    /**
     * 查询购买记录
     * 
     * @param id 购买记录ID
     * @return 购买记录
     */
    public BitTorrentPurchase selectGlxTorrentPurchaseById(Long id);

    /**
     * 查询购买记录列表
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 购买记录集合
     */
    public List<BitTorrentPurchase> selectGlxTorrentPurchaseList(BitTorrentPurchase bitTorrentPurchase);

    /**
     * 新增购买记录
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 结果
     */
    public int insertGlxTorrentPurchase(BitTorrentPurchase bitTorrentPurchase);

    /**
     * 修改购买记录
     * 
     * @param bitTorrentPurchase 购买记录
     * @return 结果
     */
    public int updateGlxTorrentPurchase(BitTorrentPurchase bitTorrentPurchase);

    /**
     * 批量删除购买记录
     * 
     * @param ids 需要删除的购买记录ID
     * @return 结果
     */
    public int deleteGlxTorrentPurchaseByIds(Long[] ids);

    /**
     * 删除购买记录信息
     * 
     * @param id 购买记录ID
     * @return 结果
     */
    public int deleteGlxTorrentPurchaseById(Long id);
}
