package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐持久层
 */
public interface SetmealDao {
    /**
     * 增加套餐
     */
    void add(Setmeal setmeal);

    /**
     * 保存套餐与检查组的关联信息
     */
    void settSetmealAndCheckgroup(Map map);

    /**
     * 分页查询
     */
    Page<Setmeal> selectByCondition(String queryString);

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
     * @return
     */
    List<Setmeal> getSetmeal();

    /**
     * 通过ID查询套餐详情
     * 方案一
     * @param id
     * @return
     */
    Setmeal a(int id);

    /**
     * 通过ID查询套餐详情
     * 方案二
     * @param id
     * @return
     */
    Setmeal findSetmealById(int id);

    /**
     * 通过ID查询套餐详情
     * 方案三
     * @param id
     * @return
     */
    Setmeal findSetmeal_ById(int id);

    /**
     * 编辑套餐
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 根据删除套餐与检查组的关联信息
     * @param id
     */
    void deleteSetmeltCheckGroup(int id);

    /**
     * 删除套餐
     * @param id
     */
    void delete(int id);

    /**
     * 查询各套餐总预约人次
     * @return
     */
    List<Map<String,Object>> findSetmealCount();
}
