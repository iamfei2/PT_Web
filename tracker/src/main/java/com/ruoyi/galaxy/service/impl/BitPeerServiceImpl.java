package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.galaxy.domain.BitPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitPeerMapper;
import com.ruoyi.galaxy.service.IBitPeerService;

/**
 * 节点管理Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class BitPeerServiceImpl implements IBitPeerService
{
    @Autowired
    private BitPeerMapper bitPeerMapper;

    /**
     * 查询节点管理
     * 
     * @param id 节点管理ID
     * @return 节点管理
     */
    @Override
    public BitPeer selectGlxPeerById(Long id)
    {
        return bitPeerMapper.selectGlxPeerById(id);
    }

    /**
     * 查询节点管理列表
     * 
     * @param bitPeer 节点管理
     * @return 节点管理
     */
    @Override
    public List<BitPeer> selectGlxPeerList(BitPeer bitPeer)
    {
        return bitPeerMapper.selectGlxPeerList(bitPeer);
    }

    /**
     * 新增节点管理
     * 
     * @param bitPeer 节点管理
     * @return 结果
     */
    @Override
    public int insertGlxPeer(BitPeer bitPeer)
    {
        bitPeer.setCreateTime(DateUtils.getNowDate());
        return bitPeerMapper.insertGlxPeer(bitPeer);
    }

    /**
     * 修改节点管理
     * 
     * @param bitPeer 节点管理
     * @return 结果
     */
    @Override
    public int updateGlxPeer(BitPeer bitPeer)
    {
        bitPeer.setUpdateTime(DateUtils.getNowDate());
        return bitPeerMapper.updateGlxPeer(bitPeer);
    }

    /**
     * 批量删除节点管理
     * 
     * @param ids 需要删除的节点管理ID
     * @return 结果
     */
    @Override
    public int deleteGlxPeerByIds(Long[] ids)
    {
        return bitPeerMapper.deleteGlxPeerByIds(ids);
    }

    /**
     * 删除节点管理信息
     * 
     * @param id 节点管理ID
     * @return 结果
     */
    @Override
    public int deleteGlxPeerById(Long id)
    {
        return bitPeerMapper.deleteGlxPeerById(id);
    }

    @Override
    public BitPeer selectGlxPeerByPeerId(String peerId) {
        return bitPeerMapper.selectGlxPeerByPeerId(peerId);
    }

    @Override
    public List<BitPeer> selectGlxPeerByInfoHash(String infoHash) {
        return bitPeerMapper.selectGlxPeerByInfoHash(infoHash);
    }

    @Override
    public int deleteGlxPeerByTime(Long seconds) {
        return bitPeerMapper.deleteGlxPeerByTime(seconds);
    }

    @Override
    public List<BitPeer> selectGlxPeerByTime(Long seconds) {
        return bitPeerMapper.selectGlxPeerByTime(seconds);
    }

    @Override
    public List<BitPeer> selectGlxPeerByTorrentId(Long torrentId) {
        return bitPeerMapper.selectGlxPeerByTorrentId(torrentId);
    }

    @Override
    public List<BitPeer> selectGlxPeerByUserIdGroupByIP(Long userId) {
        return bitPeerMapper.selectGlxPeerByUserIdGroupByIP(userId);
    }

    @Override
    public BitPeer selectGlxPeerByPeerIdAndInfoHash(String peerId, String infoHash) {
        return bitPeerMapper.selectGlxPeerByPeerIdAndInfoHash(new BitPeer(){{
            setPeerId(peerId);
            setInfoHash(infoHash);
        }});
    }
}
