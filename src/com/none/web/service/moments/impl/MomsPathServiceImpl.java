package com.none.web.service.moments.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.core.model.Pager;
import com.none.web.dao.MomsPathDao;
import com.none.web.po.MomsPathPO;
import com.none.web.service.moments.IMomsPathService;
import com.none.web.service.user.IUserService;

@Service
public class MomsPathServiceImpl implements IMomsPathService {
	@Autowired
	private MomsPathDao appMomsPathDao;
	@Autowired
	private IUserService iUserService;
	/**
	 * 根据momentsid查询文件图片列表
	 */
	@Override
	public List<MomsPathPO> getAppMomsPathList(int appmomentsId) throws Exception {
		return appMomsPathDao.selectPathListByMomentsId(appmomentsId);
	}
	/**
	 * 获取文件图片地址列表总数
	 */
	@Override
	public int getAppMomsPathCount(int appmomentsId) throws Exception {
		return appMomsPathDao.selectPathCountByMomentsId(appmomentsId);
	}
	/**
	 * 获取slidermoments列表，并set进去user信息
	 */
	@Override
	public List<MomsPathPO> getSliderMoments() throws Exception {
		List<MomsPathPO> list=appMomsPathDao.selectSliderMomentsList();
		for (MomsPathPO appMomsPathPO : list) {
			String userId=appMomsPathPO.getUserId();
			Map<String, Object> map=iUserService.geUsernameAndImgAndIsVIP(userId);
			appMomsPathPO.setUserInfo(map);
		}
		return list;
	}
	/**
	 * 分页
	 */
	@Override
	public Pager getTotalCount(Pager pager) throws Exception {
		Pager pager2 = new Pager();
		Integer totalRows = appMomsPathDao.selectCountSliderMoments(pager2);
		Integer totalPages = totalRows % pager.getPageSize() > 0 ? pager.getTotalRows() / pager.getPageSize() + 1
				: pager.getTotalRows() / pager.getPageSize();
		pager2.setTotalRows(totalRows);

		pager2.setTotalPages(totalPages);
		return pager2;
	}
	
	@Override
	public Map<String, Object> getTransform(Pager pager) throws Exception {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		//查询transformList
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startRow", pager.getStartRow());
		map.put("pageSize", pager.getPageSize());
		List<MomsPathPO> transformList = appMomsPathDao.getTransformList(map);
		//获取transformList的数量count.用于分页对象设置属性值
		int totalRows = appMomsPathDao.getTransformCount();
		Pager cPage = new Pager();
		cPage.setPageNo(pager.getPageNo());
		cPage.setStartRow(pager.getStartRow());
		cPage.setPageSize(pager.getPageSize());
		cPage.setTotalRows(totalRows);
		cPage.setTotalPages(totalRows%(pager.getPageSize()) == 0?totalRows/(pager.getPageSize()):totalRows/(pager.getPageSize()) + 1);
		//返回一个map,将transformList和分页对象放入map.
		resultMap.put("transformList", transformList);
		resultMap.put("pager", cPage);
		return resultMap;
	}
}
