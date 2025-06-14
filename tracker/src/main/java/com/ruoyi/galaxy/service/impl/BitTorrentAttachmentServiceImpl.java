package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitTorrentAttachmentMapper;
import com.ruoyi.galaxy.domain.BitTorrentAttachment;
import com.ruoyi.galaxy.service.IBitTorrentAttachmentService;

/**
 * 种子附件Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-24
 */
@Service
public class BitTorrentAttachmentServiceImpl implements IBitTorrentAttachmentService
{
    @Autowired
    private BitTorrentAttachmentMapper bitTorrentAttachmentMapper;

    /**
     * 查询种子附件
     * 
     * @param id 种子附件ID
     * @return 种子附件
     */
    @Override
    public BitTorrentAttachment selectGlxTorrentAttachmentById(Long id)
    {
        return bitTorrentAttachmentMapper.selectGlxTorrentAttachmentById(id);
    }

    /**
     * 查询种子附件列表
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 种子附件
     */
    @Override
    public List<BitTorrentAttachment> selectGlxTorrentAttachmentList(BitTorrentAttachment bitTorrentAttachment)
    {
        return bitTorrentAttachmentMapper.selectGlxTorrentAttachmentList(bitTorrentAttachment);
    }

    /**
     * 新增种子附件
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 结果
     */
    @Override
    public int insertGlxTorrentAttachment(BitTorrentAttachment bitTorrentAttachment)
    {
        bitTorrentAttachment.setCreateTime(DateUtils.getNowDate());
        return bitTorrentAttachmentMapper.insertGlxTorrentAttachment(bitTorrentAttachment);
    }

    /**
     * 修改种子附件
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 结果
     */
    @Override
    public int updateGlxTorrentAttachment(BitTorrentAttachment bitTorrentAttachment)
    {
        bitTorrentAttachment.setUpdateTime(DateUtils.getNowDate());
        return bitTorrentAttachmentMapper.updateGlxTorrentAttachment(bitTorrentAttachment);
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
        return bitTorrentAttachmentMapper.deleteGlxTorrentAttachmentByIds(ids);
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
        return bitTorrentAttachmentMapper.deleteGlxTorrentAttachmentById(id);
    }

    @Override
    public int deleteGlxTorrentAttachmentByTorrentId(Long torrentId) {
        return bitTorrentAttachmentMapper.deleteGlxTorrentAttachmentByTorrentId(torrentId);
    }

    @Override
    public List<BitTorrentAttachment> selectGlxTorrentAttachmentByTorrentId(Long torrentId) {
        return bitTorrentAttachmentMapper.selectGlxTorrentAttachmentByTorrentId(torrentId);
    }

    @Override
    public BitTorrentAttachment selectGlxTorrentAttachmentByInfoHash(String infoHash) {
        return bitTorrentAttachmentMapper.selectGlxTorrentAttachmentByInfoHash(infoHash);
    }

    @Override
    public int updateGlxTorrentAttachmentTransmission(BitTorrentAttachment torrentAttachment) {
        return bitTorrentAttachmentMapper.updateGlxTorrentAttachmentTransmission(torrentAttachment);
    }
}
