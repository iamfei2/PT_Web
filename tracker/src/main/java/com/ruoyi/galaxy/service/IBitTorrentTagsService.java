package com.ruoyi.galaxy.service;

import java.util.List;

import com.ruoyi.galaxy.domain.BitTorrent;
import com.ruoyi.galaxy.domain.BitTorrentTags;

public interface IBitTorrentTagsService
{
    /**
     * 查询种子标签
     * 
     * @param id 种子标签ID
     * @return 种子标签
     */
    public BitTorrentTags selectGlxTorrentTagsById(Long id);

    /**
     * 查询种子标签列表
     * 
     * @param bitTorrentTags 种子标签
     * @return 种子标签集合
     */
    public List<BitTorrentTags> selectGlxTorrentTagsList(BitTorrentTags bitTorrentTags);

    /**
     * 新增种子标签
     * 
     * @param bitTorrentTags 种子标签
     * @return 结果
     */
    public int insertGlxTorrentTags(BitTorrentTags bitTorrentTags);

    /**
     * 修改种子标签
     * 
     * @param bitTorrentTags 种子标签
     * @return 结果
     */
    public int updateGlxTorrentTags(BitTorrentTags bitTorrentTags);

    /**
     * 批量删除种子标签
     * 
     * @param ids 需要删除的种子标签ID
     * @return 结果
     */
    public int deleteGlxTorrentTagsByIds(Long[] ids);

    /**
     * 删除种子标签信息
     * 
     * @param id 种子标签ID
     * @return 结果
     */
    public int deleteGlxTorrentTagsById(Long id);

    /**
     * 更新种子标签
     * @param torrent
     * @return
     */
    public int updateTorrentTags(BitTorrent torrent);

    /**
     * 通过种子编号删除标签
     *
     * @param id
     * @return
     */
    public int deleteGlxTorrentTagsByTorrentId(Long id);

    /**
     * 通过种子查找标签
     *
     * @param torrent
     * @return
     */
    public List<String> getByTorrent(BitTorrent torrent);
}
