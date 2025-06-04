package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxTorrentAttachmentMapper;
import com.ruoyi.galaxy.domain.GlxTorrentAttachment;
import com.ruoyi.galaxy.service.IGlxTorrentAttachmentService;

/**
 * 种子附件Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-24
 */
@Service
public class GlxTorrentAttachmentServiceImpl implements IGlxTorrentAttachmentService 
{
    @Autowired
    private GlxTorrentAttachmentMapper glxTorrentAttachmentMapper;

    /**
     * 查询种子附件
     * 
     * @param id 种子附件ID
     * @return 种子附件
     */
    @Override
    public GlxTorrentAttachment selectGlxTorrentAttachmentById(Long id)
    {
        return glxTorrentAttachmentMapper.selectGlxTorrentAttachmentById(id);
    }

    /**
     * 查询种子附件列表
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 种子附件
     */
    @Override
    public List<GlxTorrentAttachment> selectGlxTorrentAttachmentList(GlxTorrentAttachment glxTorrentAttachment)
    {
        return glxTorrentAttachmentMapper.selectGlxTorrentAttachmentList(glxTorrentAttachment);
    }

    /**
     * 新增种子附件
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 结果
     */
    @Override
    public int insertGlxTorrentAttachment(GlxTorrentAttachment glxTorrentAttachment)
    {
        glxTorrentAttachment.setCreateTime(DateUtils.getNowDate());
        return glxTorrentAttachmentMapper.insertGlxTorrentAttachment(glxTorrentAttachment);
    }

    /**
     * 修改种子附件
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 结果
     */
    @Override
    public int updateGlxTorrentAttachment(GlxTorrentAttachment glxTorrentAttachment)
    {
        glxTorrentAttachment.setUpdateTime(DateUtils.getNowDate());
        return glxTorrentAttachmentMapper.updateGlxTorrentAttachment(glxTorrentAttachment);
    }

    /**
     * 批量删除种子附件
     * 
     * @param ids 需要删除的种子附件ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentAttachmentByIds(Long[] ids)
    {
        return glxTorrentAttachmentMapper.deleteGlxTorrentAttachmentByIds(ids);
    }

    /**
     * 删除种子附件信息
     * 
     * @param id 种子附件ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentAttachmentById(Long id)
    {
        return glxTorrentAttachmentMapper.deleteGlxTorrentAttachmentById(id);
    }

    @Override
    public int deleteGlxTorrentAttachmentByTorrentId(Long torrentId) {
        return glxTorrentAttachmentMapper.deleteGlxTorrentAttachmentByTorrentId(torrentId);
    }

    @Override
    public List<GlxTorrentAttachment> selectGlxTorrentAttachmentByTorrentId(Long torrentId) {
        return glxTorrentAttachmentMapper.selectGlxTorrentAttachmentByTorrentId(torrentId);
    }

    @Override
    public GlxTorrentAttachment selectGlxTorrentAttachmentByInfoHash(String infoHash) {
        return glxTorrentAttachmentMapper.selectGlxTorrentAttachmentByInfoHash(infoHash);
    }

    @Override
    public int updateGlxTorrentAttachmentTransmission(GlxTorrentAttachment torrentAttachment) {
        return glxTorrentAttachmentMapper.updateGlxTorrentAttachmentTransmission(torrentAttachment);
    }
}
