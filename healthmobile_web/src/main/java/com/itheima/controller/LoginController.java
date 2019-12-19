package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.pojo.Member;
import com.itheima.pojo.Result;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 登录校验
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result checkJ(@RequestBody Map map,HttpServletResponse response){
        String validateCode=(String)map.get("validateCode");//验证码
        String telephone=(String)map.get("telephone");//手机号
        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);

        if(telephone==null||code==null){//手机号和验证码不能为空
            return new Result(false,MessageConstant.TELEPHONE_VALIDATECOODE_NOTNULL);
        }
        //校验验证码
        if(validateCode.equals(code)) {//用户输入的验证码和发送的验证码是否一样
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
            //调用Service
            try {
                //判断是否是会员
                Member member = memberService.findByTelephone(telephone);
                if(member==null){
                    member=new Member();
                    member.setPhoneNumber(telephone);
                    memberService.add(member);
                }
                //保存电话码到Cookie,有效期30天
                Cookie cookie=new Cookie("telephone",telephone);
                cookie.setPath("/**");//设置路径
                cookie.setMaxAge(60*60*24*30);//有效期30天
                response.addCookie(cookie);
                //返回结果
                return new Result(true, MessageConstant.LOGIN_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.SYSTEM_ERROR);
            }
    }
}
