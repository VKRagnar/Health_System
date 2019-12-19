package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;


/**
 * 预约设置持久层
 */
public interface OrderSettingDao {
    /**
     * 根据日期查询预约设置
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 插入OrderSeting的记录
     * @param orderSetting
     */
    void insertOrderSetting(OrderSetting orderSetting);

    /**
     * 更新OrderSeting的记录
     * @param orderSetting
     */
    void updateOrderSetting(OrderSetting orderSetting);

    /**
     * 获取展示的预约设置信息
     * @param currentDate
     * @return
     */
    List<OrderSetting> findByCurrentDate(String currentDate);

    /**
     * 已预约人数 +1
     * @param orderDate
     */
    void updateReservations(Date orderDate);
}
