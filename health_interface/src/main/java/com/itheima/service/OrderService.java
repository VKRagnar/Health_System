package com.itheima.service;

import com.itheima.pojo.Result;

import java.util.Map;

/**
 * 预约服务层
 */
public interface OrderService {
    /**
     * 预约体检
     * @param map
     * @return
     */
    Result submit(Map map) throws Exception;

    /**
     * 回显预约成功信息
     * @param id
     * @return
     */
    Map<String,String> findById(int id);
}
