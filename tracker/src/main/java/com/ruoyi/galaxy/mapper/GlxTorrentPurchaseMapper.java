package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxTorrentPurchase;

/**
 * 购买记录Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public interface GlxTorrentPurchaseMapper 
{
    /**
     * 查询购买记录
     * 
     * @param id 购买记录ID
     * @return 购买记录
     */
    public GlxTorrentPurchase selectGlxTorrentPurchaseById(Long id);

    /**
     * 查询购买记录列表
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 购买记录集合
     */
    public List<GlxTorrentPurchase> selectGlxTorrentPurchaseList(GlxTorrentPurchase glxTorrentPurchase);

    /**
     * 新增购买记录
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 结果
     */
    public int insertGlxTorrentPurchase(GlxTorrentPurchase glxTorrentPurchase);

    /**
     * 修改购买记录
     * 
     * @param glxTorrentPurchase 购买记录
     * @return 结果
     */
    public int updateGlxTorrentPurchase(GlxTorrentPurchase glxTorrentPurchase);

    /**
     * 删除购买记录
     * 
     * @param id 购买记录ID
     * @return 结果
     */
    public int deleteGlxTorrentPurchaseById(Long id);

    /**
     * 批量删除购买记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxTorrentPurchaseByIds(Long[] ids);
}
