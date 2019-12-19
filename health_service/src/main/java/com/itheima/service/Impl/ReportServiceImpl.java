package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 报表实现类
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 通过月份查找会员人数
     * @param listMonth
     * @return
     */
    @Override
    public List<Integer> findCountByMonth(List<String> listMonth) {
        if(listMonth==null){return null;}
        List<Integer> listCount=new ArrayList<>();
        for (String month : listMonth) {
            Integer count = memberDao.findMemberCountBeforeDate(month+"-31");
            listCount.add(count);
        }
        return listCount;
    }

    /**
     * 查询各套餐总预约人次
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealOrder() {
/*        //查询所有套餐
        List<Setmeal> setmeals=setmealDao.findAll();
        if(setmeals==null){return null;}
        //根据套餐ID得到套餐总预约人数
        //每个套餐以名字&其预约人数封装成一个Map，放到List集合中
        List<Map<String,Object>> list=new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            Map<String,Object> map=new HashMap<>();
            map.put("name",setmeal.getName());
            map.put("value",orderDao.fin)
        }*/
        return setmealDao.findSetmealCount();
    }

    /**
     * 运营数据统计数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        String today =DateUtils.parseDate2String(DateUtils.getToday());//当前日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//本周一日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//本月1号日期

        Map<String,Object> map=new HashMap<>();//封装数据集合
        //reportDate
        map.put("reportDate", today);//今日日期
        //todayNewMember
        map.put("todayNewMember",memberDao.findMemberCountByDate(today));//今日新增会员人数
        //totalMember
        map.put("totalMember",memberDao.findMemberTotalCount());//总会员人数
        //thisWeekNewMember
        map.put("thisWeekNewMember",memberDao.findMemberCountAfterDate(thisWeekMonday));//本周新增会员人数
        //thisMonthNewMember
        map.put("thisMonthNewMember",memberDao.findMemberCountAfterDate(firstDay4ThisMonth));//本月新增会员人数


        //todayOrderNumber
        map.put("todayOrderNumber",orderDao.findOrderCountByDate(today));//今日预约人数
        //todayVisitsNumber
        map.put("todayVisitsNumber",orderDao.findVisitsCountByDate(today));//今日到诊人数
        //thisWeekOrderNumber
        map.put("thisWeekOrderNumber",orderDao.findOrderCountAfterDate(thisWeekMonday));//本周预约人数
        //thisWeekVisitsNumber
        map.put("thisWeekVisitsNumber",orderDao.findVisitsCountAfterDate(thisWeekMonday));//本周到诊人数
        //thisMonthOrderNumber
        map.put("thisMonthOrderNumber",orderDao.findOrderCountAfterDate(firstDay4ThisMonth));//本月预约人数
        //thisMonthVisitsNumber
        map.put("thisMonthVisitsNumber",orderDao.findVisitsCountAfterDate(firstDay4ThisMonth));//本月到诊人数
        //hotSetmeal
        map.put("hotSetmeal", orderDao.findHotSetmeal());//热门套餐
        return map;
    }

    /**
     * 导出运营数据统计（excel）
     * @param request
     * @param response
     */
    @Override
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*        //获取运营数据统计数据
        Map<String, Object> businessReportData = this.getBusinessReportData();

        //获取excel模板对象，通过request
//        InputStream resourceAsStream = request.getSession().getServletContext().getResourceAsStream("/template/report_template.xlsx");
//        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(resourceAsStream);

        String temlateRealPath=request.getSession().getServletContext().getRealPath("template")+ File.separator+ "report_template.xlsx";
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
        //写数据到模板对象
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);

        //日期
        XSSFRow dateRow = sheetAt.getRow(2);
        XSSFCell dateRowCell = dateRow.getCell(6);
        dateRowCell.setCellValue((String) businessReportData.get("reportDate"));

        //新增会员数
        sheetAt.getRow(4).getCell(6).setCellValue((String) businessReportData.get("todayNewMember"));

        //总会员数
        sheetAt.getRow(4).getCell(8).setCellValue((String) businessReportData.get("totalMember"));

        //本周新增会员人数
        sheetAt.getRow(5).getCell(6).setCellValue((String) businessReportData.get("thisWeekNewMember"));

        //本月新增会员人数
        sheetAt.getRow(5).getCell(8).setCellValue((String) businessReportData.get("thisMonthNewMember"));

        //今日预约人数
        sheetAt.getRow(7).getCell(6).setCellValue((String) businessReportData.get("todayOrderNumber"));

        //今日到诊人数
        sheetAt.getRow(7).getCell(8).setCellValue((String) businessReportData.get("todayVisitsNumber"));

        //本周预约人数
        sheetAt.getRow(8).getCell(6).setCellValue((String) businessReportData.get("thisWeekOrderNumber"));

        //本周到诊人数
        sheetAt.getRow(8).getCell(8).setCellValue((String) businessReportData.get("thisWeekVisitsNumber"));

        //本月预约人数
        sheetAt.getRow(9).getCell(6).setCellValue((String) businessReportData.get("thisMonthOrderNumber"));

        //本月到诊人数
        sheetAt.getRow(9).getCell(8).setCellValue((String) businessReportData.get("thisMonthVisitsNumber"));

        //热门套餐
        List<Map> hotSetmeal = (List<Map>) businessReportData.get("hotSetmeal");
        int row=12;
        for(int i=0;i<hotSetmeal.size();i++){
            XSSFRow row1 = sheetAt.getRow(row++);
            row1.getCell(5).setCellValue((String) hotSetmeal.get(i).get("name"));
            row1.getCell(6).setCellValue((String) hotSetmeal.get(i).get("setmeal_count"));
            row1.getCell(7).setCellValue((Double) hotSetmeal.get(i).get("proportion"));
            row1.getCell(8).setCellValue((String) hotSetmeal.get(i).get("remark"));
        }

        //获取文件输出流，通过response
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition","attachment;filename=report.xlsx");
        xssfWorkbook.write(outputStream);
        //关闭资源
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();*/
    }
}
