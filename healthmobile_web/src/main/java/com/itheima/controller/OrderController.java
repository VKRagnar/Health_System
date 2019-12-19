package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.pojo.Result;
import com.itheima.service.OrderService;
import com.itheima.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 预约体检
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        map.put("orderType","微信预约");

        //校验验证码
        String validateCode = (String) map.get("validateCode");//验证码
        String telephone = (String) map.get("telephone");//手机号
        String orderDate = (String) map.get("orderDate");//预约日期

        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);

        if(validateCode.equals(code)){
            try {
                //调用service层
                Result result=orderService.submit(map);
                //发送预约成功短信
                if(result.isFlag()){
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
                }
                //返回结果
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.SYSTEM_ERROR);
            }
        }
        return new Result(false,MessageConstant.VALIDATECODE_ERROR);
    }

    /**
     * 回显预约成功信息
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Map<String,String> map=orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
