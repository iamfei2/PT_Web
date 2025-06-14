package com.ruoyi.galaxy.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitTorrentMapper;
import com.ruoyi.galaxy.domain.BitTorrent;
import com.ruoyi.galaxy.service.IBitTorrentService;

/**
 * 资源广场Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-17
 */
@Service
public class BitTorrentServiceImpl implements IBitTorrentService
{
    @Autowired
    private BitTorrentMapper bitTorrentMapper;

    @Autowired
    private BitTorrentTagsServiceImpl torrentTagsService;

    /**
     * 查询资源广场
     * 
     * @param id 资源广场ID
     * @return 资源广场
     */
    @Override
    public BitTorrent selectGlxTorrentById(Long id)
    {
        BitTorrent torrent = bitTorrentMapper.selectGlxTorrentById(id);
        torrent.setTags(torrentTagsService.getByTorrent(torrent));
        return torrent;
    }

    /**
     * 查询资源广场列表
     * 
     * @param bitTorrent 资源广场
     * @return 资源广场
     */
    @Override
    public List<BitTorrent> selectGlxTorrentList(BitTorrent bitTorrent)
    {
        List<BitTorrent> torrents = bitTorrentMapper.selectGlxTorrentList(bitTorrent);
        torrents.forEach(torrent -> {
            torrent.setTags(torrentTagsService.getByTorrent(torrent));
        });
        return torrents;
    }

    /**
     * 新增资源广场
     * 
     * @param bitTorrent 资源广场
     * @return 结果
     */
    @Override
    public int insertGlxTorrent(BitTorrent bitTorrent)
    {
        bitTorrent.setCreateTime(DateUtils.getNowDate());
        int result = bitTorrentMapper.insertGlxTorrent(bitTorrent);
        torrentTagsService.updateTorrentTags(bitTorrent);
        return result;
    }

    /**
     * 修改资源广场
     * 
     * @param bitTorrent 资源广场
     * @return 结果
     */
    @Override
    public int updateGlxTorrent(BitTorrent bitTorrent)
    {
        bitTorrent.setUpdateTime(DateUtils.getNowDate());
        int result = bitTorrentMapper.updateGlxTorrent(bitTorrent);
        torrentTagsService.updateTorrentTags(bitTorrent);
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
        return bitTorrentMapper.deleteGlxTorrentByIds(ids);
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
        return bitTorrentMapper.deleteGlxTorrentById(id);
    }

    @Override
    public BitTorrent selectGlxTorrentByInfoHash(String infoHash) {
        return bitTorrentMapper.selectGlxTorrentByInfoHash(infoHash);
    }

    @Override
    public int updateGlxTorrentCounter(BitTorrent bitTorrent) {
        return bitTorrentMapper.updateGlxTorrentCounter(bitTorrent);
    }

    @Override
    public List<BitTorrent> selectGlxTorrentByTags(List<String> tags) {
        return bitTorrentMapper.selectGlxTorrentByTags(tags);
    }
}
