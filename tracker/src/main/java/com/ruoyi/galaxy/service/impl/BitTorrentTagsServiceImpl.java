package com.ruoyi.galaxy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.galaxy.domain.BitTorrent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitTorrentTagsMapper;
import com.ruoyi.galaxy.domain.BitTorrentTags;
import com.ruoyi.galaxy.service.IBitTorrentTagsService;

/**
 * 种子标签Service业务层处理
 * 
 * @author Hex
 * @date 2021-11-04
 */
@Service
public class BitTorrentTagsServiceImpl implements IBitTorrentTagsService
{
    @Autowired
    private BitTorrentTagsMapper bitTorrentTagsMapper;

    /**
     * 查询种子标签
     * 
     * @param id 种子标签ID
     * @return 种子标签
     */
    @Override
    public BitTorrentTags selectGlxTorrentTagsById(Long id)
    {
        return bitTorrentTagsMapper.selectGlxTorrentTagsById(id);
    }

    /**
     * 查询种子标签列表
     * 
     * @param bitTorrentTags 种子标签
     * @return 种子标签
     */
    @Override
    public List<BitTorrentTags> selectGlxTorrentTagsList(BitTorrentTags bitTorrentTags)
    {
        return bitTorrentTagsMapper.selectGlxTorrentTagsList(bitTorrentTags);
    }

    /**
     * 新增种子标签
     * 
     * @param bitTorrentTags 种子标签
     * @return 结果
     */
    @Override
    public int insertGlxTorrentTags(BitTorrentTags bitTorrentTags)
    {
        bitTorrentTags.setCreateTime(DateUtils.getNowDate());
        return bitTorrentTagsMapper.insertGlxTorrentTags(bitTorrentTags);
    }

    /**
     * 修改种子标签
     * 
     * @param bitTorrentTags 种子标签
     * @return 结果
     */
    @Override
    public int updateGlxTorrentTags(BitTorrentTags bitTorrentTags)
    {
        bitTorrentTags.setUpdateTime(DateUtils.getNowDate());
        return bitTorrentTagsMapper.updateGlxTorrentTags(bitTorrentTags);
    }

    /**
     * 批量删除种子标签
     * 
     * @param ids 需要删除的种子标签ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentTagsByIds(Long[] ids)
    {
        return bitTorrentTagsMapper.deleteGlxTorrentTagsByIds(ids);
    }

    /**
     * 删除种子标签信息
     * 
     * @param id 种子标签ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentTagsById(Long id)
    {
        return bitTorrentTagsMapper.deleteGlxTorrentTagsById(id);
    }

    @Override
    public int updateTorrentTags(BitTorrent torrent) {
        this.deleteGlxTorrentTagsByTorrentId(torrent.getId());
        for(String tag : torrent.getTags()) {
            BitTorrentTags tagItem = new BitTorrentTags();
            tagItem.setCreateBy(SecurityUtils.getUsername());
            tagItem.setCategoryId(torrent.getCategories());
            tagItem.setTag(tag);
            tagItem.setTorrentId(torrent.getId());
            this.insertGlxTorrentTags(tagItem);
        }
        return 0;
    }

    @Override
    public int deleteGlxTorrentTagsByTorrentId(Long id) {
        return bitTorrentTagsMapper.deleteGlxTorrentTagsByTorrentId(id);
    }

    @Override
    public List<String> getByTorrent(BitTorrent torrent) {
        List<String> tags = bitTorrentTagsMapper
                .selectGlxTorrentTagsList(new BitTorrentTags(){{ setTorrentId(torrent.getId()); }})
                .stream().map(tag -> tag.getTag()).collect(Collectors.toList());
        return tags;
    }
}
