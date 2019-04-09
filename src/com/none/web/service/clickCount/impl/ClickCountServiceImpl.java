package com.none.web.service.clickCount.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.none.core.common.utils.DateUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.service.BaseServiceSupport;
import com.none.web.model.ClickCountVO;
import com.none.web.model.TClickCount;
import com.none.web.service.clickCount.IClickCountService;

@Service
public class ClickCountServiceImpl extends BaseServiceSupport implements IClickCountService {

	@Override
	public int save(TClickCount clickCount) throws Exception {
		clickCount.setCreateTime(new Timestamp(new Date().getTime()));
		return (Integer) this.saveEntity(clickCount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean export(HttpServletResponse response) throws Exception {
		// 创建titelList
		List<String> titelList = new ArrayList<String>();
		titelList.add("banner ID");
		titelList.add("date");
		titelList.add("total click rate");

		// StringBuffer t = new StringBuffer("");// 需要导出的数据
		String sql = "SELECT c.module,m.banner_group AS 'no', COUNT(c.id) AS 'clicks' FROM t_click_count c, t_main_banner m WHERE c.item_id = m.id AND LEFT (c.create_time, 10) = LEFT (NOW(), 10) AND NOW() <= m.end_time AND m.`status` = 0 AND c.module = 'mainbanner' GROUP BY item_id ORDER BY m.banner_group";
		List<ClickCountVO> clickCountList = getNamedJdbcTemplate().query(sql,
				ParameterizedBeanPropertyRowMapper.newInstance(ClickCountVO.class));

		// 声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 声明一个表格
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth((short) 22);// 设置列宽
		// 表格标题行
		HSSFRow row = sheet.createRow(0);

		HSSFCell cellHeader;
		// 插入标题
		for (int i = 0; i < titelList.size(); i++) {
			cellHeader = row.createCell(i);
			cellHeader.setCellValue(titelList.get(i));
		}
		for (int i = 1; i < 6; i++) {
			boolean flag = true;
			String date = DateUtil.formatCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			for (ClickCountVO clickCountVO : clickCountList) {
				String no = clickCountVO.getNo();
				if (StringUtils.equals(no, i + "")) {
					row = sheet.createRow(sheet.getLastRowNum() + 1);
					row.createCell(0).setCellValue(clickCountVO.getNo());
					row.createCell(1).setCellValue(date);
					int clicks = clickCountVO.getClicks();
					row.createCell(2).setCellValue(clicks);
					flag = false;
				}
			}
			if (flag) {
				row = sheet.createRow(sheet.getLastRowNum() + 1);
				row.createCell(0).setCellValue(i);
				row.createCell(1).setCellValue(date);
				row.createCell(2).setCellValue(0);
			}
		}
		String filename = "banner-clicks.xls";
		// 设置头信息
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
		return true;
	}

	@SuppressWarnings("unused")
	private boolean exportCsv(HttpServletResponse response, String dataStr) {
		response.setContentType("text/plain");// 一下两行关键的设置
		response.addHeader("Content-Disposition", "attachment;filename=banner-clicks.csv");// filename指定默认的名字
		BufferedOutputStream buff = null;
		StringBuffer write = new StringBuffer();
		ServletOutputStream outSTr = null;
		try {
			outSTr = response.getOutputStream();// 建立
			buff = new BufferedOutputStream(outSTr);
			write.append(dataStr);
			buff.write(write.toString().getBytes("UTF-8"));
			buff.flush();
		} catch (Exception e) {
			return false;
		} finally {
			if (buff != null) {
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outSTr != null) {
				try {
					outSTr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public String getBrowseTimes(String item_id, String module) throws Exception {
		String times = "0";
		String sql = "select COUNT(id) as 'clicks' from t_click_count t where t.module='" + module + "' and t.item_id='"
				+ item_id + "' AND LEFT (t.create_time, 10) = LEFT (NOW(), 10)";
		List<Map<String, Object>> listMap = this.jdbcTemplate.queryForList(sql);
		if (listMap != null && listMap.size() > 0) {
			times = StringUtil.toStringNumber(listMap.get(0).get("clicks"));
		}
		return times;
	}

}