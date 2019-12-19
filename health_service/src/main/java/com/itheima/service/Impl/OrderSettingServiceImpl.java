package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 预约设置服务层
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 上传批量设置
     * 遍历 List<OrderSeting> ，并调用 dao
       逻辑：
         （数量不能低于已预约数）
         （如果未设置，则插入）
         （已设置，则更新）
     * @param list
     */
    @Override
    public void upload(List<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
            this.setOrderSetting(orderSetting);
        }
    }

    /**
     * 单个预约设置
     * @param orderSetting
     */
    @Override
    public void setOrderSetting(OrderSetting orderSetting) {
        //查询表中记录
        OrderSetting tableOrderSetting=orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(tableOrderSetting==null){//如果未设置，则插入
            orderSettingDao.insertOrderSetting(orderSetting);
        }else {//已设置，则更新
            int number = orderSetting.getNumber();//设置的人数
            int tableNumber = tableOrderSetting.getNumber();//原设置的人数
            int reservations = tableOrderSetting.getReservations();//已预约的人数
            if(number>=reservations && number!=tableNumber){//人数不能低于已预人数 与 人数不能与原人数一样
                orderSettingDao.updateOrderSetting(orderSetting);
            }
        }
    }

    /**
     * 获取展示的预约设置信息
     * @param currentDate
     * @return
     */
    @Override
    public List<Map> initDate(String currentDate) {
        //查询获得List<OrderSetting>，模糊查询
        List<OrderSetting> list=orderSettingDao.findByCurrentDate(currentDate+"%");

        List<Map> newList=new ArrayList<>();
        //遍历集合
        for (OrderSetting orderSetting : list) {
            //封装成Map,添加到新的List集合中 { date: 1, number: 120, reservations: 1 },
            Map map=new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            newList.add(map);
        }
        return newList;
    }
}
