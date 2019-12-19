package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Result;
import com.itheima.service.OrderService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约实现类
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;//预约设置DAO

    @Autowired
    private MemberDao memberDao;//会员DAO

    @Autowired
    private OrderDao orderDao;//预约DAO

    /**
     * 体检预约
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result submit(Map map) throws Exception {
        String orderDate = (String) map.get("orderDate");//预约日期
        String orderType = (String) map.get("orderType");//预约方式
        String telephone = (String) map.get("telephone");//手机号码
        String name = (String) map.get("name");//姓名
        String idCard = (String) map.get("idCard");//身份证号
        String sex = (String) map.get("sex");//性别
        String setmealId = (String) map.get("setmealId");//套餐ID





        Date date=null;
//        try {
            date = DateUtils.parseString2Date(orderDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false, MessageConstant.SYSTEM_ERROR);
//        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);

        //校验日期是否可预约-
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //校验预约人数是否已满
        if(orderSetting.getReservations()>=orderSetting.getNumber()){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //校验是否是会员
        Member member = memberDao.findByTelephone(telephone);
        if(member==null){//即不是会员
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setName(name);
            member.setIdCard(idCard);
            member.setSex(sex);
            member.setRegTime(new Date());
            memberDao.add(member);
        }else {
            //校验是否重复预约
            Order order=new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt(setmealId));
            List<Order> byCondition = orderDao.findByCondition(order);
            if(byCondition!=null&&byCondition.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }

        //添加预约,当天预约人数+1
            Order order=new Order();
            order.setSetmealId(Integer.parseInt(setmealId));
            order.setOrderStatus("未到诊");
            order.setOrderType(orderType);
            order.setOrderDate(date);
            order.setMemberId(member.getId());
            orderDao.add(order);//添加到预约表
            orderSettingDao.updateReservations(date);//当天预约人数+1
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order);
    }

    /**
     * 回显预约成功信息
     * @param id
     * @return
     */
    @Override
    public Map<String, String> findById(int id) {
        //体检人：{{orderInfo.member}}
        //体检套餐：{{orderInfo.setmeal}}
        //体检日期：{{orderInfo.orderDate}}
        //预约类型：{{orderInfo.orderType}}
        Map byId4Detail = orderDao.findById4Detail(id);
        return byId4Detail;
    }
}
