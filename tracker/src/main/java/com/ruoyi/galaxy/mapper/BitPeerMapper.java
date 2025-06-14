package com.ruoyi.galaxy.mapper;

import java.util.List;

import com.ruoyi.galaxy.domain.BitPeer;


public interface BitPeerMapper
{
    /**
     * 查询节点管理
     * 
     * @param id 节点管理ID
     * @return 节点管理
     */
    public BitPeer selectGlxPeerById(Long id);

    /**
     * 查询节点管理列表
     * 
     * @param bitPeer 节点管理
     * @return 节点管理集合
     */
    public List<BitPeer> selectGlxPeerList(BitPeer bitPeer);

    /**
     * 新增节点管理
     * 
     * @param bitPeer 节点管理
     * @return 结果
     */
    public int insertGlxPeer(BitPeer bitPeer);

    /**
     * 修改节点管理
     * 
     * @param bitPeer 节点管理
     * @return 结果
     */
    public int updateGlxPeer(BitPeer bitPeer);

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
    public BitPeer selectGlxPeerByPeerId(String peerId);

    /**
     * 通过InfoHash获取节点
     * @param infoHash
     * @return
     */
    public List<BitPeer> selectGlxPeerByInfoHash(String infoHash);

    /**
     * 删除N秒内未活动的节点信息
     * @param seconds
     * @return
     */
    public int deleteGlxPeerByTime(Long seconds);

    public List<BitPeer> selectGlxPeerByTime(Long seconds);

    public List<BitPeer> selectGlxPeerByTorrentId(Long torrentId);

    public List<BitPeer> selectGlxPeerByUserIdGroupByIP(Long userId);

    public BitPeer selectGlxPeerByPeerIdAndInfoHash(BitPeer peer);
}
