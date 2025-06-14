package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxTorrent;

/**
 * 资源广场Service接口
 * 
 * @author HexPang
 * @date 2021-03-17
 */
public interface IGlxTorrentService 
{
    /**
     * 查询资源广场
     * 
     * @param id 资源广场ID
     * @return 资源广场
     */
    public GlxTorrent selectGlxTorrentById(Long id);

    /**
     * 查询资源广场列表
     * 
     * @param glxTorrent 资源广场
     * @return 资源广场集合
     */
    public List<GlxTorrent> selectGlxTorrentList(GlxTorrent glxTorrent);

    /**
     * 新增资源广场
     * 
     * @param glxTorrent 资源广场
     * @return 结果
     */
    public int insertGlxTorrent(GlxTorrent glxTorrent);

    /**
     * 修改资源广场
     * 
     * @param glxTorrent 资源广场
     * @return 结果
     */
    public int updateGlxTorrent(GlxTorrent glxTorrent);

    /**
     * 批量删除资源广场
     * 
     * @param ids 需要删除的资源广场ID
     * @return 结果
     */
    public int deleteGlxTorrentByIds(Long[] ids);

    /**
     * 删除资源广场信息
     * 
     * @param id 资源广场ID
     * @return 结果
     */
    public int deleteGlxTorrentById(Long id);

    public GlxTorrent selectGlxTorrentByInfoHash(String infoHash);

    /**
     * 更新种子上传下载统计
     * @param glxTorrent
     * @return
     */
    public int updateGlxTorrentCounter(GlxTorrent glxTorrent);

    /**
     * 通过标签查找种子
     *
     * @param tag
     * @return
     */
    public List<GlxTorrent> selectGlxTorrentByTags(List<String> tags);
}
