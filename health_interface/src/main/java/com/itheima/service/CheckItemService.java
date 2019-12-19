package com.itheima.service;

import com.itheima.pojo.CheckItem;
import com.itheima.pojo.PageResult;

import java.util.List;

/**
 * 体检项
 */
public interface CheckItemService {

    /**
     * 增加体检项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 查询当前页体检项
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除体检项
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据ID查找体检项
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 编辑体检项
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 查询所有体检项
     * @return
     */
    List<CheckItem> findAll();
}
