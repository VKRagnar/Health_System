package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * 检查组
 */
public interface CheckGroupDao {

    /**
     * 增加检查组
     * @param checkGroup
     */
    Integer add(CheckGroup checkGroup);

    /**
     * 将检查组和检查项的ID,插入到关联表
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 查询当前页数据
     * @param queryString
     * @return
     */
    List<CheckGroup> findPage(String queryString);

    /**
     * 删除关联表里的记录
     * @param id
     */
    void deleteCheckGroupAndCheckItem(int id);

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据ID查询检查组
     * @param groupId
     * @return
     */
    CheckGroup findById(Integer groupId);

    /**
     * 根据检查组ID查询关联的检查项
     * @param groupId
     * @return
     */
    List<Integer> findCheckitemIds(Integer groupId);

    /**
     * 修改检查组
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
