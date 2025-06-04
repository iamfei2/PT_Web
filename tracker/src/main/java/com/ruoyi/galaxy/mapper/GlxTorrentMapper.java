package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxTorrent;
import org.springframework.stereotype.Component;

/**
 * 资源广场Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-17
 */
@Component
public interface GlxTorrentMapper 
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
