package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * 权限持久层
 */
public interface PermissionDao {
    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    List<String> findPermissionByRoleId(Integer roleId);

    /**
     * 根据角色ID查找权限
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
