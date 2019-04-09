package com.none.web.service.moments.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.web.dao.MomsForwardDao;
import com.none.web.po.MomentPO;
import com.none.web.service.moments.IMomsForwardService;
@Service
public class MomsForwardServiceImpl implements IMomsForwardService{
	
	@Autowired
	private MomsForwardDao momsForwardDao;

	@Override
	public void insertMomsForward(MomentPO momentPO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("momentsId", momentPO.getId());
		map.put("userId", momentPO.getSubmitUser());
		momsForwardDao.insertMomsForward(map);
	}
	
	public int selectForwardNum(MomentPO momentPO) throws Exception {
		int forwardNum = 0;
		forwardNum = momsForwardDao.insertMomsForwardByMomentsId(momentPO.getId());
		return forwardNum;
	}
}
