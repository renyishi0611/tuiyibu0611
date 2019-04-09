package com.none.core.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportExcelUtil {
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
	public static void buildExcelDocument(List<String> titleList, List<String> content, String filename,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");

		sheet.setDefaultColumnWidth((short) 22);// 设置列宽
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeight((short) 300);
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
		font2.setFontHeight((short) 250);
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

		// 设置必答题*的颜色及属性
		HSSFFont redFont = (HSSFFont) workbook.createFont();
		redFont.setColor(HSSFColor.RED.index);// 红色
		redFont.setFontHeight((short) 300);
		redFont.setFontName("宋体");
		redFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

		// 设置必答题*后面其他内容的颜色及属性
		HSSFFont otherFont = (HSSFFont) workbook.createFont();
		otherFont.setFontHeight((short) 300);
		otherFont.setFontName("宋体");
		otherFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

		/**
		 * 写入表头
		 */
		for (String string : titles) {
			String value = string;
			String value_temp = EscapeBack.escapeBack(string.replace("##", "\r"));
			HSSFRichTextString text = new HSSFRichTextString(value_temp);
			if (value_temp.contains("*")) {
				text.applyFont(value_temp.indexOf("*"), value_temp.indexOf("*") + 1, redFont); // 设置
																								// *
																								// 的颜色属性
				text.applyFont(value_temp.indexOf("*") + 1, value_temp.length(), otherFont); // 设置
																								// *
																								// 后面其他内容的属性
			}
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
					tempStr = EscapeBack.escapeBack(tempStr.replace("null", "   "));
					cell.setCellValue(new HSSFRichTextString(tempStr));
				} else {
					HSSFCell cell = dataRow.createCell(cellIndex);
					cell.setCellStyle(style2);
					String str = EscapeBack.escapeBack(text.toString().replace("null", "   "));
					cell.setCellValue(str);
				}

				cellIndex++;
			}
		}

		// String filename = "";// 设置下载时客户端Excel的名称

		filename = "Survey_" + DateUtil.getYMDHMS();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 设置导出的excel某区域单元格为下拉选择方式
	 * 
	 * @param selectList
	 *            选择的select数据列表
	 * @param firstRow
	 *            起始行
	 * @param lastRow
	 *            终止行
	 * @param firstCol
	 *            起始列
	 * @param lastCol
	 *            终止列
	 * @return
	 */
	public static HSSFDataValidation validationData(String[] selectList, int firstRow, int lastRow, int firstCol,
			int lastCol) {
		DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(selectList);
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		return new HSSFDataValidation(addressList, dvConstraint);
	}

	/**
	 * 校验导出的excel某区域单元格为下拉选择方式所输入数据的有效性
	 * 
	 * @param selectList
	 *            选择的select数据列表
	 * @param firstRow
	 *            起始行
	 * @param lastRow
	 *            终止行
	 * @param firstCol
	 *            起始列
	 * @param lastCol
	 *            终止列
	 * @return
	 */
	public static HSSFDataValidation validationSelect(int firstRow, int lastRow, int firstCol, int lastCol) {
		DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("B1");
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		return new HSSFDataValidation(regions, constraint);
	}

	/**
	 * 校验导出的excel某区域单元格为日期方式所输入数据的有效性
	 * 
	 * @param selectList
	 *            选择的select数据列表
	 * @param firstRow
	 *            起始行
	 * @param lastRow
	 *            终止行
	 * @param firstCol
	 *            起始列
	 * @param lastCol
	 *            终止列
	 * @return
	 */
	public static HSSFDataValidation validationDate(int firstRow, int lastRow, int firstCol, int lastCol) {
		DVConstraint constraint = DVConstraint.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN,
				"1970-01-01", "2999-12-31", "yyyy-MM-dd");
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		return new HSSFDataValidation(regions, constraint);
	}

	/**
	 * 校验导出的excel某区域单元格为时间方式所输入数据的有效性
	 * 
	 * @param selectList
	 *            选择的select数据列表
	 * @param firstRow
	 *            起始行
	 * @param lastRow
	 *            终止行
	 * @param firstCol
	 *            起始列
	 * @param lastCol
	 *            终止列
	 * @return
	 */
	public static HSSFDataValidation validationTime(int firstRow, int lastRow, int firstCol, int lastCol) {
		DVConstraint constraint = DVConstraint.createTimeConstraint(OperatorType.BETWEEN, "00:00", "23:59");
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
		dataValidation.createErrorBox("", "要输入的值必须是一个时间介于00:00和23:59");
		return dataValidation;
	}

	/**
	 * 校验导出的excel某区域单元格为数字类型所输入数据的有效性
	 * 
	 * @param selectList
	 *            选择的select数据列表
	 * @param firstRow
	 *            起始行
	 * @param lastRow
	 *            终止行
	 * @param firstCol
	 *            起始列
	 * @param lastCol
	 *            终止列
	 * @return
	 */
	public static HSSFDataValidation validationNumber(int firstRow, int lastRow, int firstCol, int lastCol) {
		DVConstraint constraint = DVConstraint.createNumericConstraint(ValidationType.INTEGER,
				OperatorType.GREATER_OR_EQUAL, "0", null);
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
		return dataValidation;
	}

	/**
	 * 导出CA模板
	 * 
	 * @param filename
	 *            模板名称
	 * @param selectList
	 *            select选择的初始化数据
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void exportCaTemplate(String filename, String[] selectList, HttpServletResponse response)
			throws IOException {
		String[] titles = { "Location", "Assessment date", "Assessment time slots(Start time)",
				"Assessment time slots(End time)", "Capacity", "Registration date(Start day)",
				"Registration date(End day)" };
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建sheet页
		HSSFSheet sheet = workbook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(30);// 设置列宽
		// 创建第一行
		HSSFRow row = sheet.createRow(0);
		// 创建单元格
		HSSFCell cell = null;
		// style
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		// font.setFontHeight((short) 300);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		// 设置这些样式---标题
		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		sheet.setDefaultColumnStyle(0, style1);
		sheet.setColumnWidth(0, (short) 35.7 * 240);
		// 设置列名
		for (int i = 0; i < titles.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(titles[i]);
		}
		// 设置第一行为select下拉选择并初始化有效数据
		HSSFDataValidation initSelect = validationData(selectList, 1, 65532, 0, 0);
		// 校验select单元格的数据有效性
		HSSFDataValidation validationSelect = validationSelect(1, 65532, 0, 0);
		// 校验日期类型
		HSSFDataValidation validationDate = validationDate(1, 65532, 1, 1);
		HSSFDataValidation validationDate1 = validationDate(1, 65532, 5, 6);
		// 校验时间类型
		HSSFDataValidation validationTime = validationTime(1, 65532, 2, 3);
		// 校验数字类型
		HSSFDataValidation validationNumber = validationNumber(1, 65532, 4, 4);
		sheet.addValidationData(initSelect);
		sheet.addValidationData(validationSelect);
		sheet.addValidationData(validationDate);
		sheet.addValidationData(validationDate1);
		sheet.addValidationData(validationTime);
		sheet.addValidationData(validationNumber);
		CellRangeAddress c = CellRangeAddress.valueOf("A1:G1");
		sheet.setAutoFilter(c);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 解析excel
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static List<List<Object>> readExcel(File file) throws IOException {
		InputStream inputStream = FileUtils.openInputStream(file);
		// 获取需要读取的excel
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		// 获取sheet页
		// HSSFSheet sheet=workbook.getSheet(String sheetName);//通过sheet页名称获取
		HSSFSheet sheet = workbook.getSheet("sheet1");// 通过索引获取
		int lastRowIndex = sheet.getLastRowNum();// 获取最后一行的索
		List<List<Object>> list = new ArrayList<List<Object>>();// 用于存放读取的全部数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:ss");
		Calendar calendar = Calendar.getInstance();
		// 循环每一行读取数据
		for (int i = 1; i <= lastRowIndex; i++) {
			List<Object> rowList = new ArrayList<Object>();// 用于存放每一行的数据
			HSSFRow row = sheet.getRow(i);
			if (row == null || row.getCell(0) == null || "".equals(row.getCell(0).toString())) {
				continue;
			}
			int lastColumnIndex = row.getLastCellNum();// 获取每一个行的最后一个单元格索引
			for (int j = 0; j < lastColumnIndex; j++) {
				Object value = null;
				HSSFCell cell = row.getCell(j);
				// System.out.println("row:"+i+ "--------------cell:"+cell);
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {// 数值类型
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// 如果是date类型则 ，获取该cell的date值
						if (j == 1) {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						} else if (j == 2 || j == 3) {
							value = sdf1.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						} else if (j == 6) {
							calendar.setTimeInMillis(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).getTime());
							calendar.set(Calendar.HOUR, 23);
							calendar.set(Calendar.MINUTE, 59);
							calendar.set(Calendar.SECOND, 59);
							value = calendar.getTime();
						} else {
							value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
						}
					} else {
						// 数字类型
						value = (int) cell.getNumericCellValue();
					}
				} else {
					// 字符串类型 将字符串里的特殊字符转义
					value = EscapeBack.escape(cell.getStringCellValue());
				}
				rowList.add(value);
			}
			list.add(rowList);
		}
		inputStream.close();
		return list;
	}

	/**
	 * 解析excel
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Map<String, Object> readExcel(InputStream inputStream) throws Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
		
		// Workbook hssfWorkbook = new HSSFWorkbook(inputStream);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int numSheet = 0; numSheet <1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			HSSFRow xssfrow = hssfSheet.getRow(0);
			String[] header = new String[xssfrow.getLastCellNum()];
			for (int i = 0; i < xssfrow.getLastCellNum(); i++) {
				header[i] = getValue(xssfrow.getCell(i));
			}
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				Map<String, Object> map = new HashMap<String, Object>();

				if (hssfRow != null) {
					for (int i = 0; i < header.length; i++) {
						map.put(header[i], getValue(hssfRow.getCell(i)));
					}
					list.add(map);
				}
			}
			result.put("header", header);
			result.put("user", list);
			
		}
		return result;

	}

	/**
	 * 解析excel 2010
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Map<String, Object> readXlsx(InputStream inputStream) throws Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
		
		List<Map<String, Object>> userlist = new ArrayList<Map<String, Object>>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			
			XSSFRow xssfrow = xssfSheet.getRow(0);
			String[] header = new String[xssfrow.getLastCellNum()];
			for (int i = 0; i < xssfrow.getLastCellNum(); i++) {
				header[i]=getValue(xssfrow.getCell(i));
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				Map<String, Object> map = new HashMap<String, Object>();
				if (xssfRow != null && !StringUtils.isBlank(getValue(xssfRow.getCell(0)))) {
					for (int i = 0; i < header.length; i++) {
						map.put(header[i], getValue(xssfRow.getCell(i)));
					}
					userlist.add(map);

				}
			}
			
			result.put("header", header);
			result.put("user", userlist);
		}
		
		return result;
	}

	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
		// 用于返回结果
		String result = new String();

		try {
			// 如果单元格为空，返回null
			if (hssfCell == null) {
				result = "null";
			} else {
				// 判断单元格类型
				switch (hssfCell.getCellType()) {
				// 数字类型
				case HSSFCell.CELL_TYPE_NUMERIC:
					// 处理日期格式、时间格式
					if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
						SimpleDateFormat sdf = null;
						if (hssfCell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
							sdf = new SimpleDateFormat("HH:mm");
						} else {// 日期
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						Date date = hssfCell.getDateCellValue();
						result = sdf.format(date);
					} else if (hssfCell.getCellStyle().getDataFormat() == 58) {
						// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						double value = hssfCell.getNumericCellValue();
						Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
						result = sdf.format(date);
					} else {
						double value = hssfCell.getNumericCellValue();
						CellStyle style = hssfCell.getCellStyle();
						DecimalFormat format = new DecimalFormat();
						String temp = style.getDataFormatString();
						// 单元格设置成常规
						if (temp.equals("General")) {
							format.applyPattern("#");
						}
						result = format.format(value);
					}
					break;
				case HSSFCell.CELL_TYPE_STRING:// String类型
					result = hssfCell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					result = "";
				default:
					result = "";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}
}
