package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * 套餐接口
 */
public interface SetmealService {
    /**
     * 添加套餐
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据ID查询
     */
    Setmeal findById(int id);

    /**
     * 查询套餐关联的检查组
     */
    List findCheckgroupIds(int id);

    /**
     * 查询套餐列表
     */
    List<Setmeal> getSetmeal();

    Setmeal b(int id);

    /**
     * 编辑套餐
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐
     */
    void delete(int id);
}
