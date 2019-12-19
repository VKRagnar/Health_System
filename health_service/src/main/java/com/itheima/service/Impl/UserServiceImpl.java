package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@Service
@Service(interfaceClass = UserService.class)
//@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public User findUserByUsername(String username) {
//        return userDao.findUserByUsername(username);

        User user = userDao.findUserByUsername(username);

        if(user!=null){
            Set<Role> roles=roleDao.findByUid(user.getId());
            if(roles!=null&&roles.size()>0){
                for (Role role : roles) {
                    Integer roleId = role.getId();
                    Set<Permission> permissions=permissionDao.findByRoleId(roleId);
                    role.setPermissions(permissions);
                }
                user.setRoles(roles);
            }
        }
        return user;
    }

    @Override
    public List<GrantedAuthority> findGrantedAuthorityByUsername(String username) {
        //根据用户名查找到用户的角色
        Role role=roleDao.findRoleByUsername(username);
        //根据用户角色查找到用户的所有权限
        List<String> permissions=permissionDao.findPermissionByRoleId(role.getId());

        //把查询到的信息封装到List<GrantedAuthority>中
        List<GrantedAuthority> list=new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role.getKeyword()));
        for (String permission : permissions) {
            list.add(new SimpleGrantedAuthority(permission));
        }
        return list;
    }
}
