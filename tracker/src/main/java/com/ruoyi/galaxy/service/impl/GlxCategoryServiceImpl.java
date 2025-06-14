package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.GlxCategoryMapper;
import com.ruoyi.galaxy.domain.GlxCategory;
import com.ruoyi.galaxy.service.IGlxCategoryService;

/**
 * 分享类别Service业务层处理
 * 
 * @author Hex
 * @date 2021-03-17
 */
@Service
public class GlxCategoryServiceImpl implements IGlxCategoryService 
{
    @Autowired
    private GlxCategoryMapper glxCategoryMapper;

    /**
     * 查询分享类别
     * 
     * @param id 分享类别ID
     * @return 分享类别
     */
    @Override
    public GlxCategory selectGlxCategoryById(Long id)
    {
        return glxCategoryMapper.selectGlxCategoryById(id);
    }

    /**
     * 查询分享类别列表
     * 
     * @param glxCategory 分享类别
     * @return 分享类别
     */
    @Override
    public List<GlxCategory> selectGlxCategoryList(GlxCategory glxCategory)
    {
        return glxCategoryMapper.selectGlxCategoryList(glxCategory);
    }

    /**
     * 新增分享类别
     * 
     * @param glxCategory 分享类别
     * @return 结果
     */
    @Override
    public int insertGlxCategory(GlxCategory glxCategory)
    {
        glxCategory.setCreateTime(DateUtils.getNowDate());
        return glxCategoryMapper.insertGlxCategory(glxCategory);
    }

    /**
     * 修改分享类别
     * 
     * @param glxCategory 分享类别
     * @return 结果
     */
    @Override
    public int updateGlxCategory(GlxCategory glxCategory)
    {
        glxCategory.setUpdateTime(DateUtils.getNowDate());
        return glxCategoryMapper.updateGlxCategory(glxCategory);
    }

    /**
     * 批量删除分享类别
     * 
     * @param ids 需要删除的分享类别ID
     * @return 结果
     */
    @Override
    public int deleteGlxCategoryByIds(Long[] ids)
    {
        return glxCategoryMapper.deleteGlxCategoryByIds(ids);
    }

    /**
     * 删除分享类别信息
     * 
     * @param id 分享类别ID
     * @return 结果
     */
    @Override
    public int deleteGlxCategoryById(Long id)
    {
        return glxCategoryMapper.deleteGlxCategoryById(id);
    }
}
