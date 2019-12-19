package com.itheima.service;

import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 用户服务层
 */
public interface UserService {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 根据用户名查找用户权限
     * @param username
     * @return
     */
    List<GrantedAuthority> findGrantedAuthorityByUsername(String username);
}
