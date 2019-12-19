package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * 角色持久层
 */
public interface RoleDao {
    /**
     * 根据用户名查找角色
     * @param username
     * @return
     */
    Role findRoleByUsername(String username);

    /**
     * 根据用户ID查找角色
     * @param id
     * @return
     */
    Set<Role> findByUid(Integer id);
}
