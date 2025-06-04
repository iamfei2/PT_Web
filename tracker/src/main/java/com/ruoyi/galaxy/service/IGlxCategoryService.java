package com.ruoyi.galaxy.service;

import java.util.List;
import com.ruoyi.galaxy.domain.GlxCategory;

/**
 * 分享类别Service接口
 * 
 * @author Hex
 * @date 2021-03-17
 */
public interface IGlxCategoryService 
{
    /**
     * 查询分享类别
     * 
     * @param id 分享类别ID
     * @return 分享类别
     */
    public GlxCategory selectGlxCategoryById(Long id);

    /**
     * 查询分享类别列表
     * 
     * @param glxCategory 分享类别
     * @return 分享类别集合
     */
    public List<GlxCategory> selectGlxCategoryList(GlxCategory glxCategory);

    /**
     * 新增分享类别
     * 
     * @param glxCategory 分享类别
     * @return 结果
     */
    public int insertGlxCategory(GlxCategory glxCategory);

    /**
     * 修改分享类别
     * 
     * @param glxCategory 分享类别
     * @return 结果
     */
    public int updateGlxCategory(GlxCategory glxCategory);

    /**
     * 批量删除分享类别
     * 
     * @param ids 需要删除的分享类别ID
     * @return 结果
     */
    public int deleteGlxCategoryByIds(Long[] ids);

    /**
     * 删除分享类别信息
     * 
     * @param id 分享类别ID
     * @return 结果
     */
    public int deleteGlxCategoryById(Long id);
}
