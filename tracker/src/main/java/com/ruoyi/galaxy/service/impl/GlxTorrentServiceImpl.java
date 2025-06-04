package com.ruoyi.galaxy.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.galaxy.domain.GlxTorrentTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxTorrentMapper;
import com.ruoyi.galaxy.domain.GlxTorrent;
import com.ruoyi.galaxy.service.IGlxTorrentService;

/**
 * 资源广场Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-17
 */
@Service
public class GlxTorrentServiceImpl implements IGlxTorrentService 
{
    @Autowired
    private GlxTorrentMapper glxTorrentMapper;

    @Autowired
    private GlxTorrentTagsServiceImpl torrentTagsService;

    /**
     * 查询资源广场
     * 
     * @param id 资源广场ID
     * @return 资源广场
     */
    @Override
    public GlxTorrent selectGlxTorrentById(Long id)
    {
        GlxTorrent torrent = glxTorrentMapper.selectGlxTorrentById(id);
        torrent.setTags(torrentTagsService.getByTorrent(torrent));
        return torrent;
    }

    /**
     * 查询资源广场列表
     * 
     * @param glxTorrent 资源广场
     * @return 资源广场
     */
    @Override
    public List<GlxTorrent> selectGlxTorrentList(GlxTorrent glxTorrent)
    {
        List<GlxTorrent> torrents = glxTorrentMapper.selectGlxTorrentList(glxTorrent);
        torrents.forEach(torrent -> {
            torrent.setTags(torrentTagsService.getByTorrent(torrent));
        });
        return torrents;
    }

    /**
     * 新增资源广场
     * 
     * @param glxTorrent 资源广场
     * @return 结果
     */
    @Override
    public int insertGlxTorrent(GlxTorrent glxTorrent)
    {
        glxTorrent.setCreateTime(DateUtils.getNowDate());
        int result = glxTorrentMapper.insertGlxTorrent(glxTorrent);
        torrentTagsService.updateTorrentTags(glxTorrent);
        return result;
    }

    /**
     * 修改资源广场
     * 
     * @param glxTorrent 资源广场
     * @return 结果
     */
    @Override
    public int updateGlxTorrent(GlxTorrent glxTorrent)
    {
        glxTorrent.setUpdateTime(DateUtils.getNowDate());
        int result = glxTorrentMapper.updateGlxTorrent(glxTorrent);
        torrentTagsService.updateTorrentTags(glxTorrent);
        return result;
    }

    /**
     * 批量删除资源广场
     * 
     * @param ids 需要删除的资源广场ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentByIds(Long[] ids)
    {
        return glxTorrentMapper.deleteGlxTorrentByIds(ids);
    }

    /**
     * 删除资源广场信息
     * 
     * @param id 资源广场ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentById(Long id)
    {
        return glxTorrentMapper.deleteGlxTorrentById(id);
    }

    @Override
    public GlxTorrent selectGlxTorrentByInfoHash(String infoHash) {
        return glxTorrentMapper.selectGlxTorrentByInfoHash(infoHash);
    }

    @Override
    public int updateGlxTorrentCounter(GlxTorrent glxTorrent) {
        return glxTorrentMapper.updateGlxTorrentCounter(glxTorrent);
    }

    @Override
    public List<GlxTorrent> selectGlxTorrentByTags(List<String> tags) {
        return glxTorrentMapper.selectGlxTorrentByTags(tags);
    }
}
