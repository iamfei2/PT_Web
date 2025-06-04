package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxPeer;

/**
 * 节点管理Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-18
 */
public interface GlxPeerMapper 
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
     * 删除节点管理
     * 
     * @param id 节点管理ID
     * @return 结果
     */
    public int deleteGlxPeerById(Long id);

    /**
     * 批量删除节点管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxPeerByIds(Long[] ids);

    /**
     * 获取节点
     * @param peerId
     * @return
     */
    public GlxPeer selectGlxPeerByPeerId(String peerId);

    /**
     * 通过InfoHash获取节点
     * @param infoHash
     * @return
     */
    public List<GlxPeer> selectGlxPeerByInfoHash(String infoHash);

    /**
     * 删除N秒内未活动的节点信息
     * @param seconds
     * @return
     */
    public int deleteGlxPeerByTime(Long seconds);

    public List<GlxPeer> selectGlxPeerByTime(Long seconds);

    public List<GlxPeer> selectGlxPeerByTorrentId(Long torrentId);

    public List<GlxPeer> selectGlxPeerByUserIdGroupByIP(Long userId);

    public GlxPeer selectGlxPeerByPeerIdAndInfoHash(GlxPeer peer);
}
