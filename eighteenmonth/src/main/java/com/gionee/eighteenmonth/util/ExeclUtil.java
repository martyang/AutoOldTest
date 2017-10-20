package com.gionee.eighteenmonth.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



public class ExeclUtil {

	 public static void writeCpuloadData(File file,String[] tabHead) {
	        WritableCellFormat wcfN;
	        try {
	            file.createNewFile();
	            // 打开文件
	            WritableWorkbook book  = Workbook.createWorkbook(file);
	            WritableSheet    sheet = book.createSheet("应用启动时间", 0);
	            // sheet = workbook.createSheet("touchResult", 0);
	            WritableFont bold = new WritableFont(WritableFont.ARIAL,
	                                                 12,
	                                                 WritableFont.BOLD);// 设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
	            WritableFont bold2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
	            bold2.setColour(Colour.BLACK);
	            WritableFont bold3 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
	            bold3.setColour(Colour.DARK_BLUE2);
	            WritableCellFormat contentFormate = new WritableCellFormat(bold2);// 生成一个单元格样式控制对象
	            contentFormate.setAlignment(jxl.format.Alignment.CENTRE);// 单元格中的内容水平方向居中
	            contentFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
	            contentFormate.setBorder(Border.ALL, BorderLineStyle.THIN);
	                            contentFormate.setBackground(Colour.GRAY_25);

	            WritableCellFormat contentFormate2 = new WritableCellFormat(bold3);// 生成一个单元格样式控制对象
	            contentFormate2.setAlignment(jxl.format.Alignment.CENTRE);// 单元格中的内容水平方向居中
	            contentFormate2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
	            contentFormate2.setBorder(Border.ALL, BorderLineStyle.THIN);
	            contentFormate2.setBackground(Colour.LIGHT_GREEN);

	            WritableCellFormat titleFormate = new WritableCellFormat(bold);// 生成一个单元格样式控制对象
	            titleFormate.setAlignment(jxl.format.Alignment.LEFT);// 单元格中的内容水平方向居中
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
	            
	            for (int i = 0; i < tabHead.length; i++) {
	            	 Label applabel    = new Label(i, 0, tabHead[i], titleFormate);
	            	 sheet.addCell(applabel);
	            	 sheet.setColumnView(i, 15);
				}

	            book.write();
	            book.close();

	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println(e.toString());
	        } catch (RowsExceededException e) {
				e.printStackTrace();
				 System.out.println(e.toString());
			} catch (WriteException e) {
				e.printStackTrace();
				 System.out.println(e.toString());
			}
	    }

	 /**
	  * 追加数据
	  * @param file
	  */
	public static void insertData(File file, List<String> list){
		 try {
			 WritableFont bold2 = new WritableFont(WritableFont.ARIAL, 12,
						WritableFont.NO_BOLD);
				bold2.setColour(Colour.BLACK);
				WritableFont bold3 = new WritableFont(WritableFont.ARIAL, 12,
						WritableFont.NO_BOLD);
				bold3.setColour(Colour.BLACK);
			 WritableCellFormat contentFormate = new WritableCellFormat(bold2);// 生成一个单元格样式控制对象
				contentFormate.setAlignment(jxl.format.Alignment.CENTRE);// 单元格中的内容水平方向居中
				contentFormate
						.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
				contentFormate.setBorder(Border.ALL, BorderLineStyle.THIN);
				contentFormate.setBackground(Colour.GRAY_25);

				WritableCellFormat contentFormate2 = new WritableCellFormat(bold3);// 生成一个单元格样式控制对象
				contentFormate2.setAlignment(jxl.format.Alignment.LEFT);// 单元格中的内容水平方向居中
				contentFormate2
						.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 单元格的内容垂直方向居中
				contentFormate2.setBorder(Border.ALL, BorderLineStyle.THIN);
				contentFormate2.setBackground(Colour.LIGHT_GREEN);
				
				
			Workbook book = Workbook.getWorkbook(file);  
			    Sheet sheet = book.getSheet(0);  
			    // 获取行  
			    int length = sheet.getRows();
			    WritableWorkbook wbook = Workbook.createWorkbook(file, book); // 根据book创建一个操作对象  
			    WritableSheet sh = wbook.getSheet(0);// 得到一个工作对象  
			    List<String> cpuload = null;
			    WritableCellFormat wcf = contentFormate2;
			    Label label1 = new Label(0, length, Utils.getTime(),wcf);
				sh.addCell(label1);
			    //横向排列
				for (int j = 1; j < 1+list.size(); j++) {
					Label label = new Label(j, length,list.get(j-1) ,wcf);
					sh.addCell(label);
				}

			    wbook.write();  
			    wbook.close();
		} catch (RowsExceededException e) {
			e.printStackTrace();
			 System.out.println(e.toString());
		} catch (BiffException e) {
			e.printStackTrace();
			 System.out.println(e.toString());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			 System.out.println(e.toString());
		} catch (WriteException e) {
			e.printStackTrace();
			 System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			 System.out.println(e.toString());
		}  
	}
}