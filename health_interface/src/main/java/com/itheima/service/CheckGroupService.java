package com.itheima.service;

import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.PageResult;

import java.util.List;


/**
 * 检查组接口
 */
public interface CheckGroupService {
    /**
     * 增加检查组
     * @param checkGroup
     * @param integers
     */
    void add(CheckGroup checkGroup, Integer[] integers);

    /**
     * 查询当前页数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除检查组，通过ID
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
     * @param integers
     */
    void edit(CheckGroup checkGroup, Integer[] integers);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
