package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxTorrentAttachment;

/**
 * 种子附件Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-24
 */
public interface GlxTorrentAttachmentMapper 
{
    /**
     * 查询种子附件
     * 
     * @param id 种子附件ID
     * @return 种子附件
     */
    public GlxTorrentAttachment selectGlxTorrentAttachmentById(Long id);

    /**
     * 查询种子附件列表
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 种子附件集合
     */
    public List<GlxTorrentAttachment> selectGlxTorrentAttachmentList(GlxTorrentAttachment glxTorrentAttachment);

    /**
     * 新增种子附件
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 结果
     */
    public int insertGlxTorrentAttachment(GlxTorrentAttachment glxTorrentAttachment);

    /**
     * 修改种子附件
     * 
     * @param glxTorrentAttachment 种子附件
     * @return 结果
     */
    public int updateGlxTorrentAttachment(GlxTorrentAttachment glxTorrentAttachment);

    /**
     * 删除种子附件
     * 
     * @param id 种子附件ID
     * @return 结果
     */
    public int deleteGlxTorrentAttachmentById(Long id);

    /**
     * 批量删除种子附件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxTorrentAttachmentByIds(Long[] ids);

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
    public List<GlxTorrentAttachment> selectGlxTorrentAttachmentByTorrentId(Long torrentId);

    /**
     * 通过InfoHash查找附件
     * @param infoHash
     * @return
     */
    public GlxTorrentAttachment selectGlxTorrentAttachmentByInfoHash(String infoHash);

    /**
     * 更新统计
     * @param torrentAttachment
     * @return
     */
    public int updateGlxTorrentAttachmentTransmission(GlxTorrentAttachment torrentAttachment);
}
