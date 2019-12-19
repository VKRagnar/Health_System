package com.itheima;

import com.itheima.pojo.User;
import com.itheima.service.Impl.UserServiceImpl;
import com.itheima.service.ReportService;
import com.itheima.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public class UserServiceTest {
    @Test
    public void findUserByUsername(){
        ApplicationContext applicationContext=
                new ClassPathXmlApplicationContext("applicationContext-service.xml");
        UserService bean = applicationContext.getBean(UserService.class);
        User admin = bean.findUserByUsername("admin");
        System.out.println(admin);

        List<GrantedAuthority> admin1 = bean.findGrantedAuthorityByUsername("admin");
        for (GrantedAuthority grantedAuthority : admin1) {
            System.out.println(grantedAuthority);
        }
    }

    @Test
    public void re(){
        ApplicationContext applicationContext=
                new ClassPathXmlApplicationContext("applicationContext-service.xml");
        ReportService reportService = applicationContext.getBean(ReportService.class);
        List<Map<String, Object>> setmealOrder = reportService.findSetmealOrder();
        System.out.println(setmealOrder);
    }
}
