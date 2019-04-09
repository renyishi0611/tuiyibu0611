package com.none.web.service.exhibition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILivePollExportAPIService {
	/**
	 * 导出livePoll数据到Excel文件
	 * @param reqMap 请求条件参数
	 * @param request 请求对象request
	 * @param response 响应对象response
	 * @throws Exception
	 */
	void exportLivePollReports(String startTime,String endTime,HttpServletRequest request,HttpServletResponse response) throws Exception;
}
