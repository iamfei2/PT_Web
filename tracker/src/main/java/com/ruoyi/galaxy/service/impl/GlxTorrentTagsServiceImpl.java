package com.ruoyi.galaxy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.galaxy.domain.GlxTorrent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxTorrentTagsMapper;
import com.ruoyi.galaxy.domain.GlxTorrentTags;
import com.ruoyi.galaxy.service.IGlxTorrentTagsService;

/**
 * 种子标签Service业务层处理
 * 
 * @author Hex
 * @date 2021-11-04
 */
@Service
public class GlxTorrentTagsServiceImpl implements IGlxTorrentTagsService 
{
    @Autowired
    private GlxTorrentTagsMapper glxTorrentTagsMapper;

    /**
     * 查询种子标签
     * 
     * @param id 种子标签ID
     * @return 种子标签
     */
    @Override
    public GlxTorrentTags selectGlxTorrentTagsById(Long id)
    {
        return glxTorrentTagsMapper.selectGlxTorrentTagsById(id);
    }

    /**
     * 查询种子标签列表
     * 
     * @param glxTorrentTags 种子标签
     * @return 种子标签
     */
    @Override
    public List<GlxTorrentTags> selectGlxTorrentTagsList(GlxTorrentTags glxTorrentTags)
    {
        return glxTorrentTagsMapper.selectGlxTorrentTagsList(glxTorrentTags);
    }

    /**
     * 新增种子标签
     * 
     * @param glxTorrentTags 种子标签
     * @return 结果
     */
    @Override
    public int insertGlxTorrentTags(GlxTorrentTags glxTorrentTags)
    {
        glxTorrentTags.setCreateTime(DateUtils.getNowDate());
        return glxTorrentTagsMapper.insertGlxTorrentTags(glxTorrentTags);
    }

    /**
     * 修改种子标签
     * 
     * @param glxTorrentTags 种子标签
     * @return 结果
     */
    @Override
    public int updateGlxTorrentTags(GlxTorrentTags glxTorrentTags)
    {
        glxTorrentTags.setUpdateTime(DateUtils.getNowDate());
        return glxTorrentTagsMapper.updateGlxTorrentTags(glxTorrentTags);
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
        return glxTorrentTagsMapper.deleteGlxTorrentTagsByIds(ids);
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
        return glxTorrentTagsMapper.deleteGlxTorrentTagsById(id);
    }

    @Override
    public int updateTorrentTags(GlxTorrent torrent) {
        this.deleteGlxTorrentTagsByTorrentId(torrent.getId());
        for(String tag : torrent.getTags()) {
            GlxTorrentTags tagItem = new GlxTorrentTags();
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
        return glxTorrentTagsMapper.deleteGlxTorrentTagsByTorrentId(id);
    }

    @Override
    public List<String> getByTorrent(GlxTorrent torrent) {
        List<String> tags = glxTorrentTagsMapper
                .selectGlxTorrentTagsList(new GlxTorrentTags(){{ setTorrentId(torrent.getId()); }})
                .stream().map(tag -> tag.getTag()).collect(Collectors.toList());
        return tags;
    }
}
