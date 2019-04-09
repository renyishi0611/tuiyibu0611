package com.none.web.service.moments;

import com.none.web.po.MomentPO;

public interface IMomsForwardService {

	/**
	 * 插入转发记录
	 * @param momentPO 转发所属的momentsId
	 * @throws Exception 抛出异常
	 */
	void insertMomsForward (MomentPO momentPO) throws Exception;
	/**
	 * 查询转发数
	 * @param momentPO
	 * @return
	 * @throws Exception
	 */
	int selectForwardNum (MomentPO momentPO) throws Exception;
	
}
