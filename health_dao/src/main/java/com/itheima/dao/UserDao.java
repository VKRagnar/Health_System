package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户持久层
 */
public interface UserDao {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);
}
