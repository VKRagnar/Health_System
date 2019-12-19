package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置接口
 */
public interface OrderSettingService {
    /**
     * 上传批量设置
     * @param list
     */
    void upload(List<OrderSetting> list);

    /**
     * 单个预约设置
     * @param orderSetting
     */
    void setOrderSetting(OrderSetting orderSetting);

    /**
     * 获取展示的预约设置信息
     * @param currentDate
     * @return
     */
    List<Map> initDate(String currentDate);
}
