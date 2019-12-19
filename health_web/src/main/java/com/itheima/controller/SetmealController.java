package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.QueryPageBean;
import com.itheima.pojo.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐管理层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传图片
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){
        try {
            //获取文件后缀
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //生成新文件名
            String fileName=UUID.randomUUID().toString()+suffix;
            //上传文件到七牛云
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),fileName);

            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 增加套餐
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 编辑套餐
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('SETMEAL_EDIT')")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('SETMEAL_DELETE')")
    public Result delete(int id){
        try {
            setmealService.delete(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 根据ID查询
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public Result findById(int id){
        try {
            Setmeal list=setmealService.findById(id);
            return new Result(true,"",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * 查询套餐关联的检查组
     */
    @RequestMapping(value = "/findCheckgroupIds",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public Result findCheckgroupIds(int id){
        try {
            List list=setmealService.findCheckgroupIds(id);
            return new Result(true,"",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }
    }
}
