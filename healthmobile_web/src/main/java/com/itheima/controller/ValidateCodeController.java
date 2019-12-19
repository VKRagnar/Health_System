package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.pojo.Result;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        if(telephone!=null) {
            try {
                //生成验证码
                String code = ValidateCodeUtils.generateValidateCode(4).toString();
                System.out.println(telephone+"：："+code);
                //发送短信
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                //把验证码保存到redis中，保存5分钟
                jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        if(telephone!=null) {
            try {
                //生成验证码
                String code = ValidateCodeUtils.generateValidateCode(6).toString();
                System.out.println(telephone+"：："+code);
                //发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                //把验证码保存到redis中，保存5分钟
                jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone, 5 * 60, code);
                return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.SYSTEM_ERROR);
            }
        }else {
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


    @Test
//    public void re(){
//        Result result = send4Order("13289337226");
//    }
    public void re(){
        Result result = send4Order("18814729577");
    }
}
