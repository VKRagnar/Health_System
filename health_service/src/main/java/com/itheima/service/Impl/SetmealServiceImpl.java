package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务层
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加套餐
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //保存套餐信息
        setmealDao.add(setmeal);
        //保存套餐与检查组的关联信息 t_setmeal_checkgroup
        settSetmealAndCheckgroup(setmeal.getId(),checkgroupIds);

        //把上传成功套餐的图片名保存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }


    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page=setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据ID查询
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 查询套餐关联的检查组
     */
    @Override
    public List findCheckgroupIds(int id) {
        return setmealDao.findCheckgroupIds(id);
    }

    /**
     * 查询套餐列表
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     * 通过ID查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal b(int id) {
        return setmealDao.a(id);
    }

    /**
     * 编辑套餐
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        if(setmeal!=null){
            setmealDao.update(setmeal);//更新套餐信息
            if(checkgroupIds!=null){
                setmealDao.deleteSetmeltCheckGroup(setmeal.getId());//删除关联信息
                settSetmealAndCheckgroup(setmeal.getId(),checkgroupIds);//保存关联
            }
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        }
    }

    /**
     * 删除套餐
     */
    @Override
    public void delete(int id) {
        //删除图片
        Setmeal setmeal = this.findById(id);
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
/*        String img = this.findById(id).getImg();
        QiniuUtils.deleteFileFromQiniu(img);//删除七牛云上的图片 --会造成redis的数据沉余*/
        setmealDao.deleteSetmeltCheckGroup(id);//删除关联信息
        setmealDao.delete(id);
    }


    /**
     * 保存套餐与检查组的关联信息
     */
    private void settSetmealAndCheckgroup(Integer id,Integer[] checkgroupIds) {
        if(checkgroupIds!=null && checkgroupIds.length>0){
            Map map=new HashMap();
            for (Integer checkgroupId : checkgroupIds) {//遍历保存关联信息
                map.put("setmeal_id",id);
                map.put("checkgroup_id",checkgroupId);
                setmealDao.settSetmealAndCheckgroup(map);
            }
        }
    }


}
