package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@Controller
@Controller
public class WebSpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username==null){return null; }

/*        //1、根据用户名查询，获得User user	--判断是否有此用户
        User user=userService.findUserByUsername(username);

        if(user==null){return null;}

        //2、根据用户名查询，获得 List<GrantedAuthority> list
        List<GrantedAuthority> list=userService.findGrantedAuthorityByUsername(username);

        //3、返回 UserDetails对象（username,user.getPassword(),list)
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);*/

        User user = userService.findUserByUsername(username);
        if(user==null){return null;}

        List<GrantedAuthority> grantedAuthorityList=
                new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                grantedAuthorityList.add(
                  new SimpleGrantedAuthority(permission.getKeyword())
                );
            }
        }

        return new org.springframework.security.core.userdetails.User(
                username,user.getPassword(),grantedAuthorityList
        );
    }
}
