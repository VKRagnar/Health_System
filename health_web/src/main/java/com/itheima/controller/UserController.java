package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.pojo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/getUsername",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public Result getUsername(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
