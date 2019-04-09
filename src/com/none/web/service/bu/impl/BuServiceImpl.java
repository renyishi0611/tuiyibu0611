package com.none.web.service.bu.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.core.common.utils.PageUtil;
import com.none.core.common.utils.PathUtil;
import com.none.core.common.utils.PropertyUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.exception.ValidateException;
import com.none.web.dao.BuDao;
import com.none.web.po.BuPO;
import com.none.web.service.bu.IBuService;

/**
 * 
 * @ClassName: BuServiceImpl
 * @Description: TODO(buservice的实现类)
 * @author Jan
 * @date Dec 17, 2018 5:25:29 PM
 */
@Service
public class BuServiceImpl implements IBuService {

	@Autowired
	private BuDao buDao;

	/**
	 * 创建bu
	 */
	@Override
	public int save(BuPO buPO, HttpServletRequest request) throws Exception {
		// 查询bu名称是否被占用
		int number = buDao.selectBuName(buPO.getBu());

		if (number != 0) {
			return 0;
		}

		buPO.setId(UUID.randomUUID().toString().replace("-", ""));
		// 校验头像是否为空
		String photo = buPO.getBuPhoto();
		if (StringUtil.isEmpty(photo)) { // 头像信息如果为空,设置默认头像
			String fileName = PropertyUtil.getProperty("SystemConfig.properties", "user.DefaultHeadPortrait");
			String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
			photo = PathUtil.getUploadServerPath(request, fileName, folderPath); // 设置默认图像
			// String serverPath = PathUtil.getServerPath(request);
			// photo = serverPath + File.separator + "freemarker" +
			// File.separator + "defaultImg" + File.separator
			// + "userAppDefaultHeadPortrait.jpg";
			buPO.setBuPhoto(photo);
		}
		// 没有占用，就直接插入
		return buDao.createBu(buPO);

	}

	/**
	 * 修改，修改bu的状态值
	 */
	@Override
	public int update(BuPO buPO) throws Exception {
		int resultNum = buDao.updateBu(buPO);
		return resultNum;
	}

	/**
	 * 分页查询bu列表信息
	 */
	@Override
	public Map<String, Object> seachBuListByPage(BuPO po) throws Exception {

		po.setStartPage((po.getPageNo() - 1) * po.getPageSize());

		List<BuPO> buList = buDao.searchBu(po);

		int count = buDao.searchCount(po);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("buList", buList);
		resultMap.put("totalRows", count);
		resultMap.put("pageSize", po.getPageSize());
		resultMap.put("pageNum", po.getPageNo());

		int totalPage;
		totalPage = PageUtil.reckonTotalPage(count, po.getPageSize());

		resultMap.put("totalPages", totalPage);

		return resultMap;
	}

	@Override
	public BuPO detailBu(BuPO buPO) throws Exception {
		BuPO buPOList = buDao.detailBu(buPO);
		String time = buPOList.getCreateTime();
		buPOList.setCreateTime(time.substring(0, time.length() - 2));
		return buPOList;
	}

	/**
	 * 查询传入id是否存在，0不存在，1就是存在
	 */
	@Override
	public Integer selectBuById(String id) throws Exception {
		return buDao.selectBuById(id);
	}

	/**
	 * delete逻辑删除
	 */
	@Override
	public Integer deleteBu(BuPO buPO) throws Exception {
		// 查询该id下面的用户数量
		int num = buDao.selectCountFromUserByGroupId(buPO.getId());
		if (num > 0) {
			throw new ValidateException("bu.deleteError");
		}
		// 更改他的状态
		int resultNum = buDao.updateBu(buPO);
		return resultNum;
	}

}
