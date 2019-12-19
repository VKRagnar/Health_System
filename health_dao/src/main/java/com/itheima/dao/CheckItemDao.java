package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.QueryPageBean;

import java.util.List;

/**
 * 体检项
 */
public interface CheckItemDao {

    /**
     * 增加体检项
     * @param checkItem
     */
    void insert(CheckItem checkItem);

    /**
     * 查询当前页体检项
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

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
    CheckItem selectById(int id);

    /**
     * 根据ID查找目标体检项与体检组的关联数
     * @param id
     * @return
     */
    Long findCountByCheckItemId(int id);

    /**
     * 编辑体检项
     * @param checkItem
     */
    void updateCheckItem(CheckItem checkItem);

    /**
     * 查询所有体检项
     * @return
     */
    List<CheckItem> selectAll();
}
