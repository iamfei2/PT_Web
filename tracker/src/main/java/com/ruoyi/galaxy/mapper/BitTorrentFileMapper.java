package com.ruoyi.galaxy.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.galaxy.domain.BitTorrentFile;

public interface BitTorrentFileMapper
{
    /**
     * 查询种子文件
     * 
     * @param id 种子文件ID
     * @return 种子文件
     */
    public BitTorrentFile selectGlxTorrentFileById(Long id);

    /**
     * 查询种子文件列表
     * 
     * @param bitTorrentFile 种子文件
     * @return 种子文件集合
     */
    public List<BitTorrentFile> selectGlxTorrentFileList(BitTorrentFile bitTorrentFile);

    /**
     * 新增种子文件
     * 
     * @param bitTorrentFile 种子文件
     * @return 结果
     */
    public int insertGlxTorrentFile(BitTorrentFile bitTorrentFile);

    /**
     * 修改种子文件
     * 
     * @param bitTorrentFile 种子文件
     * @return 结果
     */
    public int updateGlxTorrentFile(BitTorrentFile bitTorrentFile);

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

    public List<BitTorrentFile> selectGlxTorrentFileByFile(Map<String, List<Object>> data);
}
