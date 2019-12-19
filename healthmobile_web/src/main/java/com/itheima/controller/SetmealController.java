package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 查询套餐列表
     * @return
     */
    @RequestMapping(value="/getSetmeal",method = RequestMethod.POST)
    public Result getSetmeal(){
        try {
            List<Setmeal> list=setmealService.getSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 根据ID查找套餐
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(int id){
        try {
//            Setmeal setmeal=setmealService.findById(id);
            Setmeal setmeal=setmealService.b(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
