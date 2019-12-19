package com.itheima.test;


import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Setmeal;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestResultMap {
    /**
     * 测试方案一
     */
    @Test
    public void test1(){
       ApplicationContext applicationContext =new ClassPathXmlApplicationContext("applicationContext-dao.xml");
        SetmealDao bean = applicationContext.getBean(SetmealDao.class);

        Setmeal b = bean.a(15);
        System.out.println(b);
    }


    /**
     * 测试方案二
     */
    @Test
    public void test2(){
        ApplicationContext applicationContext =new ClassPathXmlApplicationContext("applicationContext-dao.xml");
        SetmealDao bean = applicationContext.getBean(SetmealDao.class);

        Setmeal b = bean.findSetmealById(15);
        System.out.println(b);
    }

    /**
     * 测试方案三
     */
    @Test
    public void test3(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext-dao.xml");
        SetmealDao bean = applicationContext.getBean(SetmealDao.class);
        Setmeal setmeal_byId = bean.findSetmeal_ById(15);
        System.out.println(setmeal_byId);
    }
}
