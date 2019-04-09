package com.none.core.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

public class BookExportExcel {
	/**
	 * 创建Excel文件
	 * @param titleList  标题集合
	 * @param content 内容   每一行的值用&&连接，同一单元格多个值用@@连接
	 * @param workbook
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void buildExcelDocument(List<String> titleList,List<String> content, String filename,
			HttpServletRequest request, HttpServletResponse response,String type)
			throws Exception {
		HSSFWorkbook workbook =new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Record");
		
		//sheet.setDefaultColumnWidth((short) 22);//设置列宽
		//35.7为一个像素
		if("book".equals(type)){
			sheet.setColumnWidth(0, (short) 35.7*200);
			sheet.setColumnWidth(1, (short) 35.7*150);
			sheet.setColumnWidth(2, (short) 35.7*100);
			sheet.setColumnWidth(3, (short) 35.7*500);
			sheet.setColumnWidth(4, (short) 35.7*110);
			sheet.setColumnWidth(5, (short) 35.7*110);
			sheet.setColumnWidth(6, (short) 35.7*110);
			sheet.setColumnWidth(7, (short) 35.7*200);
		}
		
		if("ca".equals(type)){
			sheet.setColumnWidth(0, (short) 35.7*150);
			sheet.setColumnWidth(1, (short) 35.7*100);
			sheet.setColumnWidth(2, (short) 35.7*200);
			sheet.setColumnWidth(3, (short) 35.7*110);
			sheet.setColumnWidth(4, (short) 35.7*300);
			sheet.setColumnWidth(5, (short) 35.7*300);
			sheet.setColumnWidth(6, (short) 35.7*300);
			sheet.setColumnWidth(7, (short) 35.7*200);
			sheet.setColumnWidth(8, (short) 35.7*250);
			sheet.setColumnWidth(9, (short) 35.7*200);
			sheet.setColumnWidth(10, (short) 35.7*200);
		}
		
		
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeight((short) 250);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		// 设置这些样式---标题
		style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		

		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style2.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		//font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font2.setFontHeight((short) 220);
		font2.setFontName("宋体");
		// 把字体应用到当前的样式
		style2.setFont(font2);


		String titles[] = new String[titleList.size()];
		
		
		int max = 0;//记录标题的最大行数
		for(int i=0;i<titleList.size();i++){
			int num = 0;
			String title = titleList.get(i);
			if(title.contains("##")){
				num = title.split("##").length;
			}
			titles[i] = title;
			if(num > max){
				max = num;
			}
		}
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) ((max+1) * 370));//根据最大行数设置行高
		int index = 0;
		
		
		
		/**
		 * 写入表头
		 */
		for (String string : titles) {
			String value = string;
			String value_temp = EscapeBack.escapeBack(string.replace("##", "\r"));
			HSSFRichTextString text = new HSSFRichTextString(value_temp);
			
			Cell cell = titleRow.createCell(index);
			if(value.contains("##")){
				style.setWrapText(true);
			}
			cell.setCellStyle(style);
			cell.setCellValue(text);
			index++;
		}

		List<String[]> listData = new ArrayList<String[]>();
		if(content!=null && content.size()>0){
			for (int k=0;k<content.size();k++) {
				String eachContent = content.get(k);
				String contents [] = eachContent.split("&&");
				listData.add(contents);
			}
		}
		
		/**
		 * 添加数据部分
		 */
		int i = 1;
		
		for (String[] strings : listData) {
			HSSFRow dataRow = sheet.createRow(i++);
			int maxLen = 1;
			int cellIndex = 0;
			for (String string : strings) {
				if(string.contains("@@")){
					int tempmaxLen = string.split("@@").length;
					if(tempmaxLen > maxLen){
						maxLen = tempmaxLen;
					}
				}
			}
			
			dataRow.setHeight((short) (maxLen*300));
			for (String string : strings) {
				HSSFRichTextString text = new HSSFRichTextString(string);
				if(text.toString().contains("@@")){
					String temp [] = string.split("@@");
					
					HSSFCell cell = dataRow.createCell(cellIndex);
					
					
		            
					String tempStr = temp[1];
					
					//System.out.println("tempStr   ==="+tempStr);
					tempStr = EscapeBack.escapeBack(tempStr.replace("null", "   "));
					if(tempStr.trim().length()==0){
						cell.setCellValue(new HSSFRichTextString(tempStr));
					}else{
						String edArray[] = tempStr.split("/");
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.YEAR, Integer.valueOf(edArray[2]));
						calendar.set(Calendar.MONTH, Integer.valueOf(edArray[0])-1);
						calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(edArray[1]));
						Date date = calendar.getTime();
						cell.setCellValue(DateUtil.formatDate(date,"MM-dd-yyyy"));
					}
					
					
					HSSFCellStyle cellStyle = workbook.createCellStyle();  
					cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
					cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
					cellStyle.setFont(font2);
		            HSSFDataFormat format= workbook.createDataFormat();  
		            cellStyle.setDataFormat(format.getFormat("MM-dd-yyyy"));  
		            cell.setCellStyle(cellStyle); 
		            
				}else{
					HSSFCell cell = dataRow.createCell(cellIndex);
					cell.setCellStyle(style2);
					String str = EscapeBack.escapeBack(text.toString().replace("null", "   "));
					cell.setCellValue(str);
				}
				
				cellIndex++;
			}
		}

		//String filename = "";// 设置下载时客户端Excel的名称

		filename = filename+DateUtil.getYMDHMS();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+ filename+".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
}
