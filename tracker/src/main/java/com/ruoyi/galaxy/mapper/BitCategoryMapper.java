package com.ruoyi.galaxy.mapper;

import java.util.List;
import com.ruoyi.galaxy.domain.BitCategory;


public interface BitCategoryMapper
{
    /**
     * 查询分享类别
     * 
     * @param id 分享类别ID
     * @return 分享类别
     */
    public BitCategory selectGlxCategoryById(Long id);

    /**
     * 查询分享类别列表
     * 
     * @param bitCategory 分享类别
     * @return 分享类别集合
     */
    public List<BitCategory> selectGlxCategoryList(BitCategory bitCategory);

    /**
     * 新增分享类别
     * 
     * @param bitCategory 分享类别
     * @return 结果
     */
    public int insertGlxCategory(BitCategory bitCategory);

    /**
     * 修改分享类别
     * 
     * @param bitCategory 分享类别
     * @return 结果
     */
    public int updateGlxCategory(BitCategory bitCategory);

    /**
     * 删除分享类别
     * 
     * @param id 分享类别ID
     * @return 结果
     */
    public int deleteGlxCategoryById(Long id);

    /**
     * 批量删除分享类别
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGlxCategoryByIds(Long[] ids);
}
