package com.none.web.service.exhibition.impl;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.core.common.utils.DateUtil;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;
import com.none.web.dao.LivePollExportDao;
import com.none.web.po.LivePollReportPO;
import com.none.web.service.exhibition.ILivePollExportAPIService;
import com.none.web.utils.ExcelUtil;
@Service
public class LivePollExportAPIServiceImpl implements ILivePollExportAPIService{
	private Logger logger = Logger.getLogger(LivePollExportAPIServiceImpl.class);
	@Autowired
	private LivePollExportDao livePollExportDao;
	@Override
	public void exportLivePollReports(String startTime,String endTime,HttpServletRequest request,HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(startTime)) {
			map.put("startTime", DateUtil.parseDateTime(startTime));
		}
		if (StringUtils.isNoneBlank(endTime)) {
			map.put("endTime", DateUtil.parseDateTime(endTime));
		}
		//获取数据
		List<LivePollReportPO> livePollReports = livePollExportDao.getLivePollReports(map);
		//excel标题
		String[] title = {"live poll question","answer","staff id"};
		//excel文件名
		String fileName = formatDateString(startTime) + "-" + formatDateString(endTime) + "_livePoll.xls";
		//sheet名
		String sheetName = "live poll report";
		String[][] content = new String[livePollReports.size()][title.length];
		for (int i = 0; i < livePollReports.size(); i++) {
			content[i] = new String[title.length];
			LivePollReportPO livePollReportPO = livePollReports.get(i);
			content[i][0] = livePollReportPO.getLivePollQuestion();
			content[i][1] = livePollReportPO.getAnswer();
			// 从数据库取到的staffId不能直接放入到Excel中，要解密，方便客户看懂.
			content[i][2] = AESUtil.decrypt(livePollReportPO.getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY);
			
		}
		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
		try {
			//响应到客户端
			fileName = new String(fileName.getBytes(),"ISO8859-1");
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		logger.info("耗时:===" + (end - start) + "ms");
	}
	
	private String formatDateString(String dateString) {
		return dateString.replace("-", "").replace(":", "").replace(" ", "");
	}
}
