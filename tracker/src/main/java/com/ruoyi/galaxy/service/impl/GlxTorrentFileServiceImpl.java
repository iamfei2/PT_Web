package com.ruoyi.galaxy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxTorrentFileMapper;
import com.ruoyi.galaxy.domain.GlxTorrentFile;
import com.ruoyi.galaxy.service.IGlxTorrentFileService;

/**
 * 种子文件Service业务层处理
 * 
 * @author HexPang
 * @date 2021-03-19
 */
@Service
public class GlxTorrentFileServiceImpl implements IGlxTorrentFileService 
{
    @Autowired
    private GlxTorrentFileMapper glxTorrentFileMapper;

    /**
     * 查询种子文件
     * 
     * @param id 种子文件ID
     * @return 种子文件
     */
    @Override
    public GlxTorrentFile selectGlxTorrentFileById(Long id)
    {
        return glxTorrentFileMapper.selectGlxTorrentFileById(id);
    }

    /**
     * 查询种子文件列表
     * 
     * @param glxTorrentFile 种子文件
     * @return 种子文件
     */
    @Override
    public List<GlxTorrentFile> selectGlxTorrentFileList(GlxTorrentFile glxTorrentFile)
    {
        return glxTorrentFileMapper.selectGlxTorrentFileList(glxTorrentFile);
    }

    /**
     * 新增种子文件
     * 
     * @param glxTorrentFile 种子文件
     * @return 结果
     */
    @Override
    public int insertGlxTorrentFile(GlxTorrentFile glxTorrentFile)
    {
        return glxTorrentFileMapper.insertGlxTorrentFile(glxTorrentFile);
    }

    /**
     * 修改种子文件
     * 
     * @param glxTorrentFile 种子文件
     * @return 结果
     */
    @Override
    public int updateGlxTorrentFile(GlxTorrentFile glxTorrentFile)
    {
        return glxTorrentFileMapper.updateGlxTorrentFile(glxTorrentFile);
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
        return glxTorrentFileMapper.deleteGlxTorrentFileByIds(ids);
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
        return glxTorrentFileMapper.deleteGlxTorrentFileById(id);
    }

    @Override
    public List<GlxTorrentFile> selectGlxTorrentFileByFile(Map<String, List<Object>> data) {
        return glxTorrentFileMapper.selectGlxTorrentFileByFile(data);
    }
}
