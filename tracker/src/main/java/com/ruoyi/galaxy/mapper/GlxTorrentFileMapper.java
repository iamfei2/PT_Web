package com.ruoyi.galaxy.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.galaxy.domain.GlxTorrentFile;

/**
 * 种子文件Mapper接口
 * 
 * @author HexPang
 * @date 2021-03-19
 */
public interface GlxTorrentFileMapper 
{
    /**
     * 查询种子文件
     * 
     * @param id 种子文件ID
     * @return 种子文件
     */
    public GlxTorrentFile selectGlxTorrentFileById(Long id);

    /**
     * 查询种子文件列表
     * 
     * @param glxTorrentFile 种子文件
     * @return 种子文件集合
     */
    public List<GlxTorrentFile> selectGlxTorrentFileList(GlxTorrentFile glxTorrentFile);

    /**
     * 新增种子文件
     * 
     * @param glxTorrentFile 种子文件
     * @return 结果
     */
    public int insertGlxTorrentFile(GlxTorrentFile glxTorrentFile);

    /**
     * 修改种子文件
     * 
     * @param glxTorrentFile 种子文件
     * @return 结果
     */
    public int updateGlxTorrentFile(GlxTorrentFile glxTorrentFile);

    /**
     * 删除种子文件
     * 
     * @param id 种子文件ID
     * @return 结果
     */
    public int deleteGlxTorrentFileById(Long id);

    /**
     * 批量删除种子文件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxTorrentFileByIds(Long[] ids);

    public List<GlxTorrentFile> selectGlxTorrentFileByFile(Map<String, List<Object>> data);
}
