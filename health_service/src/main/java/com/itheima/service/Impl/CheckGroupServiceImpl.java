package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.PageResult;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 增加检查组
     * @param checkGroup
     * @param integers
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] integers) {
        //插入新的体检组
        Integer id = checkGroupDao.add(checkGroup);
        //获取新体检组的ID
        //将体检组和体检项的ID,插入到关联表
        setCheckGroupAndCheckItem(checkGroup.getId(),integers);
//        checkGroupDao.addToAssociation(id,integers);
    }

    /**
     * 查询当前页数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<CheckGroup> list=checkGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),list);
    }

    /**
     * 删除检查组，通过ID
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //先删除关联表的记录
        deleteCheckGroupAndCheckItem(id);
        //再删除体检组表的记录
        checkGroupDao.deleteById(id);
    }


    /**
     * 根据ID查询检查组
     * @param groupId
     * @return
     */
    @Override
    public CheckGroup findById(Integer groupId) {
        return checkGroupDao.findById(groupId);
    }

    /**
     * 根据检查组ID查询关联的检查项
     * @param groupId
     * @return
     */
    @Override
    public List<Integer> findCheckitemIds(Integer groupId) {
        return checkGroupDao.findCheckitemIds(groupId);
    }

    /**
     * 修改检查组
     * @param checkGroup
     * @param integers
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] integers) {
        //修改检查组
        checkGroupDao.edit(checkGroup);
        Integer id=checkGroup.getId();
        //根据检查组ID删除关联表的记录
        deleteCheckGroupAndCheckItem(id);
        //根据检查组ID和检查项数组来添加关联表的记录
        this.setCheckGroupAndCheckItem(id,integers);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    /**
     * 删除关联表里的记录
     * @param id
     */
    private void deleteCheckGroupAndCheckItem(int id) {
        checkGroupDao.deleteCheckGroupAndCheckItem(id);
    }

    /**
     * 将检查组和体查项的ID,插入到关联表
     * @param id
     * @param integers
     */
    private void setCheckGroupAndCheckItem(Integer id, Integer[] integers) {
        if (integers!=null&&integers.length>0) {
            Map<String,Integer> map=new HashMap<>();
            for (Integer integer : integers) {
                map.put("checkgroup_id",id);
                map.put("checkitem_id",integer);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
