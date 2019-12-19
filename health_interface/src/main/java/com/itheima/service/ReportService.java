package com.itheima.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 报表服务层
 */
public interface ReportService {
    /**
     * 通月份查找会员人数
     * @param listMonth
     * @return
     */
    List<Integer> findCountByMonth(List<String> listMonth);

    /**
     * 套餐预约占比饼形图数据
     * @return
     */
    List<Map<String,Object>> findSetmealOrder();

    /**
     * 运营数据统计数据
     * @return
     */
    Map<String,Object> getBusinessReportData() throws Exception;

    /**
     * 导出运营数据统计（excel）
     * @param request
     * @param response
     */
    void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
