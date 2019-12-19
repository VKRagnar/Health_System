package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Result;
import com.itheima.service.OrderSettingService;
import com.itheima.util.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;


/**
 * 预约设置控制层
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量预约设置
     * @param excelFile
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result upload(MultipartFile excelFile){
        try {
            //接收文件解析成 List<String[]>，并封装成 List<OrderSeting> 对象
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> list=new ArrayList<>();

            long nowTime = new Date().getTime();//获取当前时间的毫秒值
            for (String[] string : strings) {
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(string[0]);
                if(date.getTime()>nowTime) {//预约设置的日期的毫值必须大于等于当前日期
                    list.add(new OrderSetting(date, Integer.parseInt(string[1])));
                }
            }
            //调用service
            orderSettingService.upload(list);
            //返回结果
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 单个预约设置
     * @param orderSetting
     * @return
     */
    @RequestMapping(value = "/setOrderSetting",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result setOrderSetting(@RequestBody OrderSetting orderSetting){
        try {
            if(orderSetting.getOrderDate().getTime()>new Date().getTime()) {
                orderSettingService.setOrderSetting(orderSetting);
                return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
            }else {
                return new Result(false,"当前设置日期，无效");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

    /**
     * 获取展示的预约设置信息
     */
    @RequestMapping(value = "/initDate",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result initDate(String currentDate){
        try {
            List<Map> list=orderSettingService.initDate(currentDate);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTION_FAIL);
        }
    }
}
