package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxPeer;

/**
 * 节点管理Service接口
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public interface IGlxPeerService 
{
    /**
     * 查询节点管理
     * 
     * @param id 节点管理ID
     * @return 节点管理
     */
    public GlxPeer selectGlxPeerById(Long id);

    /**
     * 查询节点管理列表
     * 
     * @param glxPeer 节点管理
     * @return 节点管理集合
     */
    public List<GlxPeer> selectGlxPeerList(GlxPeer glxPeer);

    /**
     * 新增节点管理
     * 
     * @param glxPeer 节点管理
     * @return 结果
     */
    public int insertGlxPeer(GlxPeer glxPeer);

    /**
     * 修改节点管理
     * 
     * @param glxPeer 节点管理
     * @return 结果
     */
    public int updateGlxPeer(GlxPeer glxPeer);

    /**
     * 批量删除节点管理
     * 
     * @param ids 需要删除的节点管理ID
     * @return 结果
     */
    public int deleteGlxPeerByIds(Long[] ids);

    /**
     * 删除节点管理信息
     * 
     * @param id 节点管理ID
     * @return 结果
     */
    public int deleteGlxPeerById(Long id);

    public GlxPeer selectGlxPeerByPeerId(String peerId);

    public List<GlxPeer> selectGlxPeerByInfoHash(String infoHash);

    public int deleteGlxPeerByTime(Long seconds);

    public List<GlxPeer> selectGlxPeerByTime(Long seconds);

    public List<GlxPeer> selectGlxPeerByTorrentId(Long torrentId);

    public List<GlxPeer> selectGlxPeerByUserIdGroupByIP(Long userId);

    public GlxPeer selectGlxPeerByPeerIdAndInfoHash(String peerId, String infoHash);
}
