package com.gionee.myapplication.newpkg;


import com.gionee.myapplication.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 创建表格，添加字段
 *
 * @author pbl
 *
 */
public class JxlUtil {

    private static String[] mCheckBoxState = null;
    /**
     *
     * @param file 要写文档的文件
     * @param logPath log路径
     */
    public static void writeAppinfoData(File file, String logPath) {
        WritableCellFormat    wcfN = null;
        try {
            file.createNewFile();
            // 打开文件
            WritableWorkbook  book  = Workbook.createWorkbook(file);
                WritableSheet sheet = book.createSheet("测试结果", 0);
                // sheet = workbook.createSheet("touchResult", 0);
                WritableFont bold = new WritableFont(WritableFont.ARIAL,
                                                     12,
                                                     WritableFont.BOLD);// 设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
                WritableCellFormat contentFormate = new WritableCellFormat(bold);// 生成一个单元格样式控制对象
                contentFormate.setAlignment(jxl.format.Alignment.CENTRE);// 单元格中的内容水平方向居中
                contentFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
                contentFormate.setBorder(Border.ALL, BorderLineStyle.THIN);
                contentFormate.setBackground(Colour.RED);

                WritableCellFormat titleFormate = new WritableCellFormat(bold);// 生成一个单元格样式控制对象
                titleFormate.setAlignment(jxl.format.Alignment.CENTRE);// 单元格中的内容水平方向居中
                titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
                titleFormate.setBorder(Border.ALL, BorderLineStyle.THIN);


                WritableFont nf = new WritableFont(WritableFont.createFont("宋体"), 10);
                wcfN = new jxl.write.WritableCellFormat(nf);
                wcfN.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
                wcfN.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
                wcfN.setAlignment(Alignment.CENTRE);

                // 水平对齐
                wcfN.setWrap(true);
                // 生成名为“第一页”的工作表，参数0表示这是第一页
                // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
                // 以及单元格内容为test

            for (int i = 0; i < Constants.EXCEL_HEAD.length; i++) {
                Label applabel = new Label(i, 0, Constants.EXCEL_HEAD[i], titleFormate);
                sheet.addCell(applabel);
                sheet.setColumnView(i, 26);// 设置第0列的宽度为2600
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(logPath)));
            String line;
            int i = 0;
            while ((line =bufferedReader.readLine())!=null){
                i++;
                String[] strs = line.split(",");
                for (int j = 0; j < strs.length; j++) {
                    Label label = new Label(j, i, strs[j], titleFormate);
                    sheet.addCell(label);
                }
            }


            // 写入数据并关闭文件
            book.write();
            book.close();

        } catch (Exception e) {
            Log.i(e.getMessage());
        }
    }

}
