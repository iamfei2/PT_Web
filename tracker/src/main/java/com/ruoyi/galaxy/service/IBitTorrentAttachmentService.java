package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.BitTorrentAttachment;

public interface IBitTorrentAttachmentService
{
    /**
     * 查询种子附件
     * 
     * @param id 种子附件ID
     * @return 种子附件
     */
    public BitTorrentAttachment selectGlxTorrentAttachmentById(Long id);

    /**
     * 查询种子附件列表
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 种子附件集合
     */
    public List<BitTorrentAttachment> selectGlxTorrentAttachmentList(BitTorrentAttachment bitTorrentAttachment);

    /**
     * 新增种子附件
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 结果
     */
    public int insertGlxTorrentAttachment(BitTorrentAttachment bitTorrentAttachment);

    /**
     * 修改种子附件
     * 
     * @param bitTorrentAttachment 种子附件
     * @return 结果
     */
    public int updateGlxTorrentAttachment(BitTorrentAttachment bitTorrentAttachment);

    /**
     * 批量删除种子附件
     * 
     * @param ids 需要删除的种子附件ID
     * @return 结果
     */
    public int deleteGlxTorrentAttachmentByIds(Long[] ids);

    /**
     * 删除种子附件信息
     * 
     * @param id 种子附件ID
     * @return 结果
     */
    public int deleteGlxTorrentAttachmentById(Long id);

    /**
     * 通过种子删除相关附件
     * @param torrentId
     * @return
     */
    public int deleteGlxTorrentAttachmentByTorrentId(Long torrentId);

    /**
     * 通过种子查询附件
     * @param torrentId
     * @return
     */
    public List<BitTorrentAttachment> selectGlxTorrentAttachmentByTorrentId(Long torrentId);

    /**
     * 通过InfoHash查找附件
     * @param infoHash
     * @return
     */
    public BitTorrentAttachment selectGlxTorrentAttachmentByInfoHash(String infoHash);

    /**
     * 更新统计
     * @param torrentAttachment
     * @return
     */
    public int updateGlxTorrentAttachmentTransmission(BitTorrentAttachment torrentAttachment);
}
