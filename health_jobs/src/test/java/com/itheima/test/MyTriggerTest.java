package com.itheima.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTriggerTest {
    public static void main(String[] args) {
        //创建spring对象
        new ClassPathXmlApplicationContext("applicationContext-job.xml");
    }

}
