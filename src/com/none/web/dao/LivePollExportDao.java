package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.po.LivePollReportPO;
import com.none.web.utils.BaseDao;
@Repository
public interface LivePollExportDao extends BaseDao<LivePollExportDao>{
	/**
	 * 获取livePoll导出数据
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	List<LivePollReportPO> getLivePollReports(Map<String,Object> reqMap) throws Exception;
}
