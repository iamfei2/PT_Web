package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.BitTorrent;
import org.springframework.stereotype.Component;

@Component
public interface BitTorrentMapper
{
    /**
     * 查询资源广场
     * 
     * @param id 资源广场ID
     * @return 资源广场
     */
    public BitTorrent selectGlxTorrentById(Long id);

    /**
     * 查询资源广场列表
     * 
     * @param bitTorrent 资源广场
     * @return 资源广场集合
     */
    public List<BitTorrent> selectGlxTorrentList(BitTorrent bitTorrent);

    /**
     * 新增资源广场
     * 
     * @param bitTorrent 资源广场
     * @return 结果
     */
    public int insertGlxTorrent(BitTorrent bitTorrent);

    /**
     * 修改资源广场
     * 
     * @param bitTorrent 资源广场
     * @return 结果
     */
    public int updateGlxTorrent(BitTorrent bitTorrent);

    /**
     * 删除资源广场
     * 
     * @param id 资源广场ID
     * @return 结果
     */
    public int deleteGlxTorrentById(Long id);

    /**
     * 批量删除资源广场
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxTorrentByIds(Long[] ids);

    /**
     * 通过InfoHash获取种子信息
     * @param infoHash
     * @return
     */
    public BitTorrent selectGlxTorrentByInfoHash(String infoHash);

    /**
     * 更新种子上传下载统计
     * @param bitTorrent
     * @return
     */
    public int updateGlxTorrentCounter(BitTorrent bitTorrent);

    /**
     * 通过标签查找种子
     *
     * @param tag
     * @return
     */
    public List<BitTorrent> selectGlxTorrentByTags(List<String> tags);
}
