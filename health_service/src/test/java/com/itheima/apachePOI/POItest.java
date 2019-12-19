package com.itheima.apachePOI;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class POItest {
    @Test
    public void read1() throws IOException {
        //1 创建工作簿对象
        XSSFWorkbook workbook=new XSSFWorkbook("D:/read.xlsx");
        //2 获得工作表对象
        XSSFSheet sheet=workbook.getSheetAt(0);
        int i=0;
        //3 遍历工作表 获得行对象
        for (Row row : sheet) {
            //4 遍历行对象 获得列对象
            for (Cell cell : row) {
                //5 获得列里面的内容

/*                Object s = null;
                if(i>0){
                    s=cell.getNumericCellValue();
                }else {
                    s=cell.getStringCellValue();
                }
                System.out.println(s);*/
            }
            i++;
            System.out.println("==========");
        }

        //6 关闭
        workbook.close();
    }

    @Test
    public void read2() throws IOException {
        //1 创建工作簿对象
        XSSFWorkbook workbook=new XSSFWorkbook("D:/read.xlsx");
        //2 获得工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3 获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        //4 遍历行
        for(int i=0;i<=1;i++){
            XSSFRow row = sheet.getRow(i);
            //5 获得最后一列的列号
            short lastCellNum = row.getLastCellNum();
            //6 遍历列
            for(int j=0;j<lastCellNum;j++){
                //7 取出数据
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }

        //8 关闭
        workbook.close();
    }

    @Test
    public void write1() throws IOException {
        //1 创建工作簿对象
        XSSFWorkbook workbook=new XSSFWorkbook();
        //2 创建工作表对象
        XSSFSheet sheet = workbook.createSheet();
        //3 创建行
        XSSFRow row1 = sheet.createRow(0);
        //4 创建列，设置内容
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("性别");
        row1.createCell(2).setCellValue("地址");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("张三");
        row2.createCell(1).setCellValue("男");
        row2.createCell(2).setCellValue("深圳");

        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("李四");
        row3.createCell(1).setCellValue("男");
        row3.createCell(2).setCellValue("深圳");

        //5 通过输出流对象写到磁盘
        FileOutputStream os=new FileOutputStream("D:/myexcel.xlsx");
        workbook.write(os);
        os.flush();
        os.close();

        workbook.close();
    }
}
