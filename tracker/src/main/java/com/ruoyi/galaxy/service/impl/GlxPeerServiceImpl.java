package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxPeerMapper;
import com.ruoyi.galaxy.domain.GlxPeer;
import com.ruoyi.galaxy.service.IGlxPeerService;

/**
 * 节点管理Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-18
 */
@Service
public class GlxPeerServiceImpl implements IGlxPeerService 
{
    @Autowired
    private GlxPeerMapper glxPeerMapper;

    /**
     * 查询节点管理
     * 
     * @param id 节点管理ID
     * @return 节点管理
     */
    @Override
    public GlxPeer selectGlxPeerById(Long id)
    {
        return glxPeerMapper.selectGlxPeerById(id);
    }

    /**
     * 查询节点管理列表
     * 
     * @param glxPeer 节点管理
     * @return 节点管理
     */
    @Override
    public List<GlxPeer> selectGlxPeerList(GlxPeer glxPeer)
    {
        return glxPeerMapper.selectGlxPeerList(glxPeer);
    }

    /**
     * 新增节点管理
     * 
     * @param glxPeer 节点管理
     * @return 结果
     */
    @Override
    public int insertGlxPeer(GlxPeer glxPeer)
    {
        glxPeer.setCreateTime(DateUtils.getNowDate());
        return glxPeerMapper.insertGlxPeer(glxPeer);
    }

    /**
     * 修改节点管理
     * 
     * @param glxPeer 节点管理
     * @return 结果
     */
    @Override
    public int updateGlxPeer(GlxPeer glxPeer)
    {
        glxPeer.setUpdateTime(DateUtils.getNowDate());
        return glxPeerMapper.updateGlxPeer(glxPeer);
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
        return glxPeerMapper.deleteGlxPeerByIds(ids);
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
        return glxPeerMapper.deleteGlxPeerById(id);
    }

    @Override
    public GlxPeer selectGlxPeerByPeerId(String peerId) {
        return glxPeerMapper.selectGlxPeerByPeerId(peerId);
    }

    @Override
    public List<GlxPeer> selectGlxPeerByInfoHash(String infoHash) {
        return glxPeerMapper.selectGlxPeerByInfoHash(infoHash);
    }

    @Override
    public int deleteGlxPeerByTime(Long seconds) {
        return glxPeerMapper.deleteGlxPeerByTime(seconds);
    }

    @Override
    public List<GlxPeer> selectGlxPeerByTime(Long seconds) {
        return glxPeerMapper.selectGlxPeerByTime(seconds);
    }

    @Override
    public List<GlxPeer> selectGlxPeerByTorrentId(Long torrentId) {
        return glxPeerMapper.selectGlxPeerByTorrentId(torrentId);
    }

    @Override
    public List<GlxPeer> selectGlxPeerByUserIdGroupByIP(Long userId) {
        return glxPeerMapper.selectGlxPeerByUserIdGroupByIP(userId);
    }

    @Override
    public GlxPeer selectGlxPeerByPeerIdAndInfoHash(String peerId, String infoHash) {
        return glxPeerMapper.selectGlxPeerByPeerIdAndInfoHash(new GlxPeer(){{
            setPeerId(peerId);
            setInfoHash(infoHash);
        }});
    }
}
