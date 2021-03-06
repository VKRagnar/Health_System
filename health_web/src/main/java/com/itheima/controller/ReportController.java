package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;

    /**
     * 会员数量折线图数据
     * @return
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getMemberReport(){
        try {
            //准备一年的月份集合
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);
            List<String> listMonth=new ArrayList<>();//一年月份的集合
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1);
                listMonth.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }

            //准备各月份的会员人数
            List<Integer> listCount=reportService.findCountByMonth(listMonth);
            //返回一个Map集合，封装月份和会员人数的数据
            Map<String,List> map=new HashMap<>();
            map.put("months",listMonth);
            map.put("memberCount",listCount);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐预约占比饼形图数据
     * @return
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getSetmealReport(){
        List<Map<String,Object>> list=reportService.findSetmealOrder();
        //准备所有的套餐名
        List<String> setmealNames=new ArrayList<>();
        for (Map<String, Object> map : list) {
            setmealNames.add((String)map.get("name"));
        }
        //准备各套餐的预约人数,与套餐名一起封装成Map
        Map<String,List> map=new HashMap<>();
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",list);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    /**
     * 运营数据统计数据
     * @return
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map=reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据统计（excel）
     */
    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            toExcel(request, response);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }

    private void toExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = reportService.getBusinessReportData();

        //取出返回结果数据，准备将报表数据写入到Excel文件中
        String  reportDate = (String) result.get("reportDate");
        Integer  todayNewMember = (Integer) result.get("todayNewMember");
        Integer  totalMember = (Integer) result.get("totalMember");
        Integer  thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer  thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer  todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer  todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer  thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer  thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer  thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer  thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        //获得Excel模板文件绝对路径
        String temlateRealPath=request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";

        //获得模板文件创建Excel表格对象
        XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow row = sheet.getRow(2);
        row.getCell(5).setCellValue(reportDate);//日期

        row=sheet.getRow(4);
        row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
        row.getCell(7).setCellValue(totalMember);//总会员数

        row=sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
        row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

        row=sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
        row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

        row=sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
        row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

        row=sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
        row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

        int rowNum=12;
        for (Map map : hotSetmeal) {//热门套餐
            String name = (String) map.get("name");
            Long setmeal_count = (Long) map.get("setmeal_count");
            BigDecimal proportion = (BigDecimal) map.get("proportion");
            String remark = (String) map.get("remark");
            row =sheet.getRow(rowNum++);
            row.getCell(4).setCellValue(name);//套餐名称
            row.getCell(5).setCellValue(setmeal_count);//套餐名称
            row.getCell(6).setCellValue(proportion.doubleValue());//套餐名称
            row.getCell(7).setCellValue(remark);//套餐名称
        }

        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition","attachment;filename=report.xlsx");
        workbook.write(outputStream);

        outputStream.flush();
        outputStream.close();
        workbook.close();
    }

}
