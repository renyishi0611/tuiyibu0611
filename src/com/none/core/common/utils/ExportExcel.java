package com.none.core.common.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

public class ExportExcel {
	/**
	 * 创建Excel文件
	 * 
	 * @param titleList
	 *            标题集合
	 * @param content
	 *            内容 每一列的值用&&连接，同一单元格多个值用@@连接
	 * @param workbook
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void buildExcelDocument(List<String> titleList,
			List<String> content, String filename, HttpServletRequest request,
			HttpServletResponse response, String type) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");

		sheet.setDefaultColumnWidth((short) 22);// 设置列宽
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeight((short) 220);
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
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font2.setFontHeight((short) 200);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		String titles[] = new String[titleList.size()];

		int max = 0;// 记录标题的最大行数
		for (int i = 0; i < titleList.size(); i++) {
			int num = 0;
			String title = titleList.get(i);
			if (title.contains("##")) {
				num = title.split("##").length;
			}
			titles[i] = title;
			if (num > max) {
				max = num;
			}
		}
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) ((max + 1) * 370));// 根据最大行数设置行高
		int index = 0;

		/**
		 * 写入表头
		 */
		for (String string : titles) {
			String value = string;
			String value_temp = EscapeBack.escapeBack(string
					.replace("##", "\r"));
			HSSFRichTextString text = new HSSFRichTextString(value_temp);

			Cell cell = titleRow.createCell(index);
			if (value.contains("##")) {
				style.setWrapText(true);
			}
			cell.setCellStyle(style);
			cell.setCellValue(text);
			index++;
		}

		List<String[]> listData = new ArrayList<String[]>();
		if (content != null && content.size() > 0) {
			for (int k = 0; k < content.size(); k++) {
				String eachContent = content.get(k);
				String contents[] = eachContent.split("&&");
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
				if (string.contains("@@")) {
					int tempmaxLen = string.split("@@").length;
					if (tempmaxLen > maxLen) {
						maxLen = tempmaxLen;
					}
				}
			}

			dataRow.setHeight((short) (maxLen * 300));
			for (String string : strings) {
				HSSFRichTextString text = new HSSFRichTextString(string);
				if (text.toString().contains("@@")) {
					String temp[] = string.split("@@");

					HSSFCell cell = dataRow.createCell(cellIndex);
					style2.setWrapText(true);
					cell.setCellStyle(style2);
					String tempStr = "";
					for (int kk = 0; kk <= temp.length - 1; kk++) {
						if (kk < temp.length - 1) {
							tempStr = tempStr + temp[kk] + "\r";// 换行
						} else {
							tempStr = tempStr + temp[kk];
						}

					}
					// System.out.println("tempStr ==="+tempStr);
					tempStr = EscapeBack.escapeBack(tempStr.replace("null",
							"   "));
					cell.setCellValue(new HSSFRichTextString(tempStr));
				} else {
					HSSFCell cell = dataRow.createCell(cellIndex);
					cell.setCellStyle(style2);
					String str = EscapeBack.escapeBack(text.toString().replace(
							"null", "   "));
					cell.setCellValue(str);
				}

				cellIndex++;
			}
		}

		// String filename = "";// 设置下载时客户端Excel的名称

		filename = type + "_" + DateUtil.getYMDHMS();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="
				+ filename + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	@SuppressWarnings("deprecation")
	public static void ExportExcelDocument(Map<String, Object> excelCustomer,
			List<Map<String, Object>> redCustomer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("activeUserInfo");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeight((short) 220);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		String[] headers = (String[]) excelCustomer.get("header");
		List<Map<String, Object>> exceluser = (List<Map<String, Object>>) excelCustomer
				.get("user");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		Map map = new HashMap();
		// 创建内容
		for (int i = 0; i < exceluser.size(); i++) {
			row = sheet.createRow(i + 1);
			Map<String, Object> info = exceluser.get(i);
			if (null == map.get(info.get("staffId"))) {
				map.put(info.get("staffId"), "1");
				createRow(row, info, null, headers);
			} else {
				createRow(row, info, style2, headers);
			}
		}
		for (int j = 0; j < redCustomer.size(); j++) {
			int rowNum = exceluser.size() + j + 1;
			row = sheet.createRow(rowNum);
			Map<String, Object> info = redCustomer.get(j);
			if (null == map.get(info.get("staffId"))) {
				map.put(info.get("staffId"), "1");
				createRow(row, info, null, headers);
			} else {
				createRow(row, info, style2, headers);
			}
		}

		String filename = "Active" + DateUtil.getYMDHMS();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="
				+ filename + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
	}

	@SuppressWarnings("deprecation")
	public ExportExcel(String[] headers, List<Map<String, Object>> content,
			HttpServletRequest request, HttpServletResponse response, String flg)
			throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("activeUserInfo");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);

		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 生成另一个样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		HSSFFont font1 = workbook.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);// 设置字体大小
		cellStyle.setFont(font1);// 选择需要用到的字体格式

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 创建内容
		for (int i = 0; i < content.size(); i++) {
			row = sheet.createRow(i + 1);
			Map<String, Object> map = content.get(i);

			createRow(row, map, cellStyle, flg);

		}

		String filename = "Active" + DateUtil.getYMDHMS();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="
				+ filename + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
	}

	public static void createRow(HSSFRow row, Map map, HSSFCellStyle style,
			String flg) {

		if (flg.equalsIgnoreCase("game")) {
			row.createCell(0).setCellStyle(style);
			row.createCell(1).setCellStyle(style);
			row.createCell(2).setCellStyle(style);
			row.createCell(3).setCellStyle(style);
			row.createCell(4).setCellStyle(style);
			row.createCell(0).setCellValue(
					null != map.get("staffId") ? map.get("staffId").toString()
							: null);
			row.createCell(1).setCellValue(
					null != map.get("chinese_name") ? map.get("chinese_name")
							.toString() : null);
			row.createCell(2).setCellValue(
					null != map.get("english_name") ? map.get("english_name")
							.toString() : null);
			row.createCell(3).setCellValue(
					null != map.get("giftId") ? map.get("giftId").toString()
							: null);
			row.createCell(4).setCellValue(
					null != map.get("giftName") ? map.get("giftName")
							.toString() : null);
		}

	}

	public static void createRow(HSSFRow row, Map<String, Object> info,
			HSSFCellStyle style, String[] headers) {

		for (int i = 0; i < headers.length; i++) {
			if (null != style) {
				row.createCell(i).setCellStyle(style);
				row.getCell(i).setCellValue(
						null == info.get(headers[i]) ? "" : info
								.get(headers[i]).toString());
			} else {
				row.createCell(i).setCellValue(
						null == info.get(headers[i]) ? "" : info
								.get(headers[i]).toString());
			}

		}

	}

	/**
	 * acticity 报名员工信息导出,高亮已选择的
	 * 
	 * @param titleList
	 * @param content
	 * @param filename
	 * @param request
	 * @param response
	 * @param type
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void buildExcel(String limitNum, String subject,
			List<String> titleList, List<String> contentList,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("报名员工信息");
		sheet.setDefaultColumnWidth((short) 22);// 设置列宽
		HSSFCellStyle style = workbook.createCellStyle();// 样式
		HSSFFont font = workbook.createFont();// 字体
		font.setFontHeight((short) 220);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		style.setLocked(false);
		// 表头样式
		style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 内容样式1
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setLocked(false);
		// 高亮显示
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style2.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 内容样式2
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setLocked(false);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style3.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 内容字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font2.setFontHeight((short) 200);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 370);// 根据最大行数设置行高
		int index = 0;
		/**
		 * 写入表头
		 */
		for (String title : titleList) {
			HSSFRichTextString text = new HSSFRichTextString(title);
			Cell cell = titleRow.createCell(index);
			cell.setCellStyle(style);
			cell.setCellValue(text);
			index++;
		}
		/**
		 * 添加数据部分
		 */
		for (int j = 0; j < contentList.size(); j++) {

			HSSFRow dataRow = sheet.createRow(j + 1);
			int cellIndex = 0;
			dataRow.setHeight((short) 300);
			String contents = contentList.get(j);
			String content[] = contents.split("&&");

			for (String string : content) {
				HSSFRichTextString text = new HSSFRichTextString(string);
				HSSFCell cell = dataRow.createCell(cellIndex);
				if (j < Integer.valueOf(limitNum)) {
					// 高亮显示
					cell.setCellStyle(style2);
				} else {
					cell.setCellStyle(style3);
				}
				String str = EscapeBack.escapeBack(text.toString().replace(
						"null", "   "));
				cell.setCellValue(str);
				cellIndex++;
			}

		}
		String filename = "Activity_" + subject;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="
				+ new String(filename.getBytes(), "iso-8859-1") + ".xls");
		// response.setContentType("application/octet-stream;charset=utf-8");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
}
