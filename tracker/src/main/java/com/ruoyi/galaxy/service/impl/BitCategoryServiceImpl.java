package com.ruoyi.galaxy.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.galaxy.mapper.BitCategoryMapper;
import com.ruoyi.galaxy.domain.BitCategory;
import com.ruoyi.galaxy.service.IBitCategoryService;

/**
 * 分享类别Service业务层处理
 * 
 * @author Hex
 * @date 2021-03-17
 */
@Service
public class BitCategoryServiceImpl implements IBitCategoryService
{
    @Autowired
    private BitCategoryMapper bitCategoryMapper;

    /**
     * 查询分享类别
     * 
     * @param id 分享类别ID
     * @return 分享类别
     */
    @Override
    public BitCategory selectGlxCategoryById(Long id)
    {
        return bitCategoryMapper.selectGlxCategoryById(id);
    }

    /**
     * 查询分享类别列表
     * 
     * @param bitCategory 分享类别
     * @return 分享类别
     */
    @Override
    public List<BitCategory> selectGlxCategoryList(BitCategory bitCategory)
    {
        return bitCategoryMapper.selectGlxCategoryList(bitCategory);
    }

    /**
     * 新增分享类别
     * 
     * @param bitCategory 分享类别
     * @return 结果
     */
    @Override
    public int insertGlxCategory(BitCategory bitCategory)
    {
        bitCategory.setCreateTime(DateUtils.getNowDate());
        return bitCategoryMapper.insertGlxCategory(bitCategory);
    }

    /**
     * 修改分享类别
     * 
     * @param bitCategory 分享类别
     * @return 结果
     */
    @Override
    public int updateGlxCategory(BitCategory bitCategory)
    {
        bitCategory.setUpdateTime(DateUtils.getNowDate());
        return bitCategoryMapper.updateGlxCategory(bitCategory);
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
        return bitCategoryMapper.deleteGlxCategoryByIds(ids);
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
        return bitCategoryMapper.deleteGlxCategoryById(id);
    }
}
