package com.ruoyi.galaxy.service;

import java.util.List;

import com.ruoyi.galaxy.domain.GlxTorrent;
import com.ruoyi.galaxy.domain.GlxTorrentTags;

/**
 * 种子标签Service接口
 * 
 * @author Hex
 * @date 2021-11-04
 */
public interface IGlxTorrentTagsService 
{
    /**
     * 查询种子标签
     * 
     * @param id 种子标签ID
     * @return 种子标签
     */
    public GlxTorrentTags selectGlxTorrentTagsById(Long id);

    /**
     * 查询种子标签列表
     * 
     * @param glxTorrentTags 种子标签
     * @return 种子标签集合
     */
    public List<GlxTorrentTags> selectGlxTorrentTagsList(GlxTorrentTags glxTorrentTags);

    /**
     * 新增种子标签
     * 
     * @param glxTorrentTags 种子标签
     * @return 结果
     */
    public int insertGlxTorrentTags(GlxTorrentTags glxTorrentTags);

    /**
     * 修改种子标签
     * 
     * @param glxTorrentTags 种子标签
     * @return 结果
     */
    public int updateGlxTorrentTags(GlxTorrentTags glxTorrentTags);

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
    public int updateTorrentTags(GlxTorrent torrent);

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
    public List<String> getByTorrent(GlxTorrent torrent);
}
