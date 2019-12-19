package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.PageResult;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 体检项实现类
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 增加体检项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.insert(checkItem);
    }

    /**
     * 查询当前页体检项x
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page=checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除体检项
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //查找目标体检项是否与其它表有关联
        Long count=checkItemDao.findCountByCheckItemId(id);
        if(count>0){
            //如果有关联就抛出异常信息：与体检组有关联，不能直接删除
            throw new RuntimeException("与体检组有关联，不能直接删除");
        }else {
            //如果没有关联就直接删除
            checkItemDao.deleteById(id);
        }
    }

    /**
     * 根据ID查找体检项
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.selectById(id);
    }

    /**
     * 编辑体检项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.updateCheckItem(checkItem);
    }

    /**
     * 查询所有体检项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.selectAll();
    }
}
