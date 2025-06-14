package com.ruoyi.galaxy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitTorrentFileMapper;
import com.ruoyi.galaxy.domain.BitTorrentFile;
import com.ruoyi.galaxy.service.IBitTorrentFileService;

/**
 * 种子文件Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-19
 */
@Service
public class BitTorrentFileServiceImpl implements IBitTorrentFileService
{
    @Autowired
    private BitTorrentFileMapper bitTorrentFileMapper;

    /**
     * 查询种子文件
     * 
     * @param id 种子文件ID
     * @return 种子文件
     */
    @Override
    public BitTorrentFile selectGlxTorrentFileById(Long id)
    {
        return bitTorrentFileMapper.selectGlxTorrentFileById(id);
    }

    /**
     * 查询种子文件列表
     * 
     * @param bitTorrentFile 种子文件
     * @return 种子文件
     */
    @Override
    public List<BitTorrentFile> selectGlxTorrentFileList(BitTorrentFile bitTorrentFile)
    {
        return bitTorrentFileMapper.selectGlxTorrentFileList(bitTorrentFile);
    }

    /**
     * 新增种子文件
     * 
     * @param bitTorrentFile 种子文件
     * @return 结果
     */
    @Override
    public int insertGlxTorrentFile(BitTorrentFile bitTorrentFile)
    {
        return bitTorrentFileMapper.insertGlxTorrentFile(bitTorrentFile);
    }

    /**
     * 修改种子文件
     * 
     * @param bitTorrentFile 种子文件
     * @return 结果
     */
    @Override
    public int updateGlxTorrentFile(BitTorrentFile bitTorrentFile)
    {
        return bitTorrentFileMapper.updateGlxTorrentFile(bitTorrentFile);
    }

    /**
     * 批量删除种子文件
     * 
     * @param ids 需要删除的种子文件ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentFileByIds(Long[] ids)
    {
        return bitTorrentFileMapper.deleteGlxTorrentFileByIds(ids);
    }

    /**
     * 删除种子文件信息
     * 
     * @param id 种子文件ID
     * @return 结果
     */
    @Override
    public int deleteGlxTorrentFileById(Long id)
    {
        return bitTorrentFileMapper.deleteGlxTorrentFileById(id);
    }

    @Override
    public List<BitTorrentFile> selectGlxTorrentFileByFile(Map<String, List<Object>> data) {
        return bitTorrentFileMapper.selectGlxTorrentFileByFile(data);
    }
}
