package com.none.web.service.banner.impl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.openservices.oss.OSSClient;
import com.none.core.common.utils.DateUtil;
import com.none.core.exception.ValidateException;
import com.none.core.service.BaseServiceSupport;
import com.none.web.dao.ILivePollDao;
import com.none.web.dao.MainBannerDao;
import com.none.web.dao.MomentsDao;
import com.none.web.dao.VoiceOfStaffDao;
import com.none.web.model.TMainBanner;
import com.none.web.po.MainBannerPO;
import com.none.web.po.MomentPO;
import com.none.web.po.VoiceOfStaffPO;
import com.none.web.service.banner.IMainBannerService;
import com.none.web.service.clickCount.IClickCountService;
import com.none.web.utils.UploadToOSSUtil;
import com.qikemi.packages.alibaba.aliyun.oss.ObjectService;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;

@Service
@SuppressWarnings("serial")
public class MainBannerServiceImpl extends BaseServiceSupport implements IMainBannerService {

	private static Logger log = Logger.getLogger(MainBannerServiceImpl.class);
	private static final String bannerSaveFolder = "/freemarker/image/";

	@Autowired
	private MainBannerDao bannerDao;
	@Autowired
	private IClickCountService iClickCountService;
	@Autowired
	private MomentsDao momDao;
	@Autowired
	private ILivePollDao livePollDaoImpl;
	@Autowired
	private VoiceOfStaffDao voiceDao;

	@Override
	public String uploadBanner(MultipartFile multipartFile) throws Exception {
		validateFile(multipartFile);
		return UploadToOSSUtil.uploadAliCloudOss(multipartFile, "image", "head");
	}

	@Override
	public TMainBanner saveMainBanner(TMainBanner bannerObj, HttpServletRequest request) throws Exception {

		Integer bannerGroup = bannerObj.getBannerGroup();
		Integer bannerOrder = bannerObj.getBannerOrder();
		// 拼接接口link
		// String linkType = bannerObj.getLinkType();
		// String linkId = bannerObj.getLinkId();
		// String requestURL = request.getRequestURL().toString();
		// 协议://IP:端口/项目部署名/
		// String link = requestURL.substring(0, requestURL.length() - 25);
		// if (StringUtils.isNotBlank(linkType)) {
		// DictionaryPO dictionaryPO = new DictionaryPO();
		// dictionaryPO.setKeys(linkType);
		// dictionaryPO.setType("link");
		// String valueByKey = dictionaryDao.getValueByKey(dictionaryPO);
		// if (StringUtils.endsWith(linkType, "l")) {// 功能列表页面,默认为第一页
		//// link = link + valueByKey + "?pager.pageSize=5&pager.pageNo=1";
		// } else if (StringUtils.endsWith(linkType, "d")) {// 详情页面
		// if (StringUtils.isBlank(linkId)) {
		// throw new ValidateException("param.error");
		// }
		//// link = link + valueByKey + "?numPerPage=10&current=1&momentsId=" +
		// linkId;
		// }
		// }
		// bannerObj.setLinkType(linkType);
		if (bannerObj.getId() == null) {// 添加
			TMainBanner findByGroupAndOrder = findByGroupAndOrder(bannerGroup.toString(), bannerOrder.toString());
			if (findByGroupAndOrder != null) {
				throw new ValidateException("base.objectExist");
			}
			// if (StringUtils.isNotBlank(linkType)) {
			// // bannerObj.setLink(link);
			// }
			this.saveEntity(bannerObj);
		} else {// 修改
			Integer id = bannerObj.getId();
			TMainBanner tMainBanner = this.findById(id);
			if (tMainBanner != null) {
				Integer bannerGroup2 = tMainBanner.getBannerGroup();
				Integer bannerOrder2 = tMainBanner.getBannerOrder();
				// 修改current的结束时间时不能大于backup的开始时间
				if (bannerOrder2 == 1) {
					TMainBanner currentBanner = findByGroupAndOrder(bannerGroup2.toString(), "2");
					// 存在对应的backup banner
					if (currentBanner != null) {
						Timestamp endTime = bannerObj.getEndTime();
						Timestamp startTime = currentBanner.getStartTime();
						if (endTime.after(startTime)) {// 比较时间
							throw new ValidateException("banner.timeError");
						}
					}
				}
				// String linkIn = bannerObj.getLink();
				// if (StringUtils.isBlank(linkIn)) {
				// bannerObj.setLink(null);
				// }
				// if (StringUtils.isNotBlank(linkType)) {
				// // bannerObj.setLink(link);
				// }
				updateEntity(bannerObj);
			}
		}
		return bannerObj;
	}

	private void validateFile(MultipartFile multipartFile) throws Exception {
		if (multipartFile == null) {
			throw new ValidateException("banner.parameter.error");
		}
		HashSet<String> set = new HashSet<String>() {
			{
				add(".jpg");
				add(".png");
				add(".gif");
				add(".jpeg");
			}
		};
		String fileName = multipartFile.getOriginalFilename();
		String suffix = "";
		if (StringUtils.isNotBlank(fileName)) {
			suffix = fileName.substring(fileName.lastIndexOf("."));
		}
		if (!set.contains(suffix)) {
			log.error("Illegal file with" + suffix);
			throw new ValidateException("Illegal file with type with" + suffix);
		}
	}

	public String uploadAliCloudOss(MultipartFile multipartFile) throws Exception {
		String endPoint = OSSClientProperties.endPoint;
		OSSClient client = new OSSClient(endPoint, OSSClientProperties.key, OSSClientProperties.secret);
		String bucketName = OSSClientProperties.bucketName;
		InputStream inputStream = multipartFile.getInputStream();
		String savePath = bannerSaveFolder + multipartFile.getOriginalFilename();
		ObjectService.putObject(client, bucketName, savePath, inputStream);
		endPoint = endPoint.substring(0, endPoint.indexOf("//") + 2) + OSSClientProperties.bucketName + "."
				+ endPoint.substring(endPoint.indexOf("//") + 2, endPoint.length());
		return endPoint + "/" + savePath;
	}

	/**
	 * admin backup轮播图列表
	 */
	@Override
	public List<TMainBanner> showMainBannerByCms() throws Exception {
		// 所有非删除banner
		List<TMainBanner> allBanner = getAllBanner();
		List<Integer> ids = new ArrayList<Integer>();
		Map<Integer, List<TMainBanner>> groupMap = new HashMap<Integer, List<TMainBanner>>();
		if (allBanner != null && allBanner.size() > 0) {
			for (TMainBanner tMainBanner : allBanner) {
				Integer id = tMainBanner.getId();
				String browseTimes = iClickCountService.getBrowseTimes(id.toString(), "mainbanner");
				tMainBanner.setBrowseTimes(browseTimes);
				Timestamp endTime = tMainBanner.getEndTime();
				Timestamp now = new Timestamp(new Date().getTime());
				if (endTime.before(now)) {// 过期-删除
					ids.add(id);
				} else {// 未过期-按组存储
					groupMap = setOfGroup(tMainBanner, groupMap);
				}
			}
		}
		// 删除已过期的
		if (ids != null && ids.size() > 0) {
			deleteByIds(ids);
		}
		// 第一张已经过期，第二张没过期，把第二张更新为第一张
		List<TMainBanner> resList = new ArrayList<TMainBanner>();
		if (groupMap != null && groupMap.size() > 0) {
			for (Map.Entry<Integer, List<TMainBanner>> entry : groupMap.entrySet()) {
				List<TMainBanner> groupList = entry.getValue();
				for (TMainBanner tMainBanner : groupList) {
					tMainBanner.setFlag("0");
				}
				// 只有一张backup的banner改为current
				if (groupList != null && groupList.size() == 1) {
					TMainBanner tMainBanner = groupList.get(0);
					if (tMainBanner.getBannerOrder() == 2) {
						tMainBanner.setBannerOrder(1);
						this.updateEntity(tMainBanner);
					}
				}
				resList.addAll(groupList);
			}
		}
		// 没有的话给默认图片
		for (int i = 1; i < 6; i++) {
			if (groupMap.get(Integer.valueOf(i)) == null) {// backup和current都没有
				TMainBanner mainBanner1 = new TMainBanner();
				mainBanner1.setBannerGroup(i);
				mainBanner1.setBannerOrder(1);
				mainBanner1.setPath(
						"http://hasetestupload.oss-cn-hongkong.aliyuncs.com/freemarker/image/banner/defaultImp.png");
				mainBanner1.setFlag("1");
				TMainBanner mainBanner2 = new TMainBanner();
				mainBanner2.setBannerGroup(i);
				mainBanner2.setBannerOrder(2);
				mainBanner2.setPath(
						"http://hasetestupload.oss-cn-hongkong.aliyuncs.com/freemarker/image/banner/defaultImp.png");
				mainBanner2.setFlag("1");
				resList.add(mainBanner1);
				resList.add(mainBanner2);
			} else if (groupMap.get(Integer.valueOf(i)).size() == 1) {// 1个
				TMainBanner tMainBanner = new TMainBanner();
				tMainBanner.setBannerGroup(i);
				if (groupMap.get(Integer.valueOf(i)).get(0).getBannerOrder() == 1) {
					tMainBanner.setBannerOrder(2);
				} else {
					tMainBanner.setBannerOrder(1);
				}
				tMainBanner.setPath(
						"http://hasetestupload.oss-cn-hongkong.aliyuncs.com/freemarker/image/banner/defaultImp.png");
				tMainBanner.setFlag("1");
				resList.add(tMainBanner);
			}
		}
		return resList;
	}

	/**
	 * app backup轮播图列表
	 */
	@Override
	public List<TMainBanner> showMainBannerByApp() throws Exception {
		List<TMainBanner> mainBannerList = showMainBannerByCms();
		if (mainBannerList != null && mainBannerList.size() > 0) {
			Iterator<TMainBanner> sListIterator = mainBannerList.iterator();
			while (sListIterator.hasNext()) {
				TMainBanner banner = sListIterator.next();
				Timestamp startTime = banner.getStartTime();
				Timestamp now = new Timestamp(new Date().getTime());
				String flag = banner.getFlag();
				// 去除backup的banner图或还未开始的current的banner图或默认图片的banner
				if (banner.getBannerOrder() == 2 || (startTime != null && startTime.after(now))
						|| StringUtils.equals(flag, "1")) {
					sListIterator.remove();
				}
			}
		}
		return mainBannerList;
	}

	@Override
	public int deleteByIds(List<Integer> ids) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		String sq = "update t_main_banner set status = 1 where id in (:ids)";
		return getNamedJdbcTemplate().update(sq, map);
	}

	/**
	 * 查询所有正在用的banner
	 */
	@Override
	public List<TMainBanner> getAllBanner() throws Exception {
		String sql = "select id,path,banner_order,banner_group,`status`,start_time,end_time,last_update_time,last_update_user,link,link_type,link_id from t_main_banner  WHERE status = 0";
		return getNamedJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(TMainBanner.class));
	}

	/**
	 * 分组存储
	 */
	private Map<Integer, List<TMainBanner>> setOfGroup(TMainBanner tMainBanner,
			Map<Integer, List<TMainBanner>> groupMap) throws Exception {
		// 按组存储
		Integer group = tMainBanner.getBannerGroup();
		if (!groupMap.containsKey(group)) {
			List<TMainBanner> newList = new ArrayList<TMainBanner>();
			newList.add(tMainBanner);
			groupMap.put(group, newList);
		} else {
			groupMap.get(group).add(tMainBanner);
		}
		return groupMap;
	}

	@Override
	public int save(TMainBanner tMainBanner) throws Exception {
		String sql = "insert into t_main_banner(path,banner_order,banner_group,status,start_time,end_time,last_update_time,last_update_user) values(?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql, tMainBanner.getPath(), tMainBanner.getBannerOrder(),
				tMainBanner.getBannerGroup(), tMainBanner.getStatus(), tMainBanner.getStartTime(),
				tMainBanner.getEndTime(), tMainBanner.getLastUpdateTime(), tMainBanner.getLastUpdateUser());
	}

	@Override
	public int update(TMainBanner tMainBanner) throws Exception {
		String sql = "update t_main_banner set  path=?,banner_order=?,banner_group=?,start_time=?,end_time=?,last_update_time=?,last_update_user=? where id = ?";
		return this.jdbcTemplate.update(sql, tMainBanner.getPath(), tMainBanner.getBannerOrder(),
				tMainBanner.getBannerGroup(), tMainBanner.getStartTime(), tMainBanner.getEndTime(),
				tMainBanner.getLastUpdateTime(), tMainBanner.getLastUpdateUser(), tMainBanner.getId());
	}

	@Override
	public TMainBanner findById(Integer id) throws Exception {
		String sql = "select id,path,link,link_type,link_id,banner_order,banner_group,`status`,start_time,end_time,last_update_time,last_update_user from t_main_banner  WHERE id = :id and  status = 0";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		List<TMainBanner> list = getNamedJdbcTemplate().query(sql, map,
				ParameterizedBeanPropertyRowMapper.newInstance(TMainBanner.class));
		if (list != null && list.size() > 0) {
			TMainBanner tMainBanner = list.get(0);
			Timestamp startTimes = tMainBanner.getStartTime();
			if (startTimes != null) {
				String startTime = startTimes.toString();
				tMainBanner.setStartDate(startTime.substring(0, 10));
				tMainBanner.setStartHour(startTime.substring(11, 13));
				tMainBanner.setStartMinute(startTime.substring(14, 16));
			}
			Timestamp endTimes = tMainBanner.getEndTime();
			if (endTimes != null) {
				String endTime = endTimes.toString();
				tMainBanner.setEndDate(endTime.substring(0, 10));
				tMainBanner.setEndHour(endTime.substring(11, 13));
				tMainBanner.setEndMinute(endTime.substring(14, 16));
			}
			// backup的banner要返回对应current banner的endTime加一分钟
			if (tMainBanner.getBannerOrder() == 2) {
				Integer bannerGroup = tMainBanner.getBannerGroup();
				TMainBanner findByGroupAndOrder = findByGroupAndOrder(bannerGroup.toString(), "1");
				if (findByGroupAndOrder != null) {
					String currentEndtime = findByGroupAndOrder.getEndTime().toString();
					if (currentEndtime != null) {
						currentEndtime = DateUtil.getBeforeSomeMinute(currentEndtime, 1);
						tMainBanner.setCurrentEndDate(currentEndtime.substring(0, 10));
						tMainBanner.setCurrentEndHour(currentEndtime.substring(11, 13));
						tMainBanner.setCurrentEndMinute(currentEndtime.substring(14, 16));
						tMainBanner.setCurrentEndtime(currentEndtime);
					}
				}
			}
			String linkId = tMainBanner.getLinkId();
			String linkType = tMainBanner.getLinkType();
			if (StringUtils.isNotBlank(linkType)) {
				String content = null;
				if (StringUtils.equalsIgnoreCase(linkType, "md")) {
					MomentPO selectById = momDao.selectById(linkId);
					if (selectById != null) {
						content = selectById.getContent();
					}
				} else if (StringUtils.equalsIgnoreCase(linkType, "ld")) {
					Map<String, Object> livePoll = livePollDaoImpl.queryLivePollById(Integer.valueOf(linkId));
					if (livePoll != null) {
						content = livePoll.get("content") == null ? "" : livePoll.get("content").toString();
					}
				} else if (StringUtils.equalsIgnoreCase(linkType, "vd")) {
					VoiceOfStaffPO voice = voiceDao.selectById(linkId);
					if (voice != null) {
						content = voice.getContent();
					}
				}
				if (content != null && content.length() > 20) {
					tMainBanner.setLinkTitle(content.substring(0, 20));
				} else {
					tMainBanner.setLinkTitle(content);
				}
				tMainBanner.setLinkFlag("Y");
			} else {
				tMainBanner.setLinkFlag("N");
			}
			return tMainBanner;
		}
		return null;
	}

	@Override
	public TMainBanner findByGroupAndOrder(String bannerGroup, String bannerOrder) throws Exception {
		String sql = "select id,path,banner_order,banner_group,`status`,start_time,end_time,last_update_time,last_update_user from t_main_banner  WHERE status = 0 and banner_order = :bannerOrder and  banner_group = :bannerGroup ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bannerGroup", bannerGroup);
		map.put("bannerOrder", bannerOrder);
		List<TMainBanner> list = getNamedJdbcTemplate().query(sql, map,
				ParameterizedBeanPropertyRowMapper.newInstance(TMainBanner.class));
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public MainBannerPO getPath() throws Exception {
		MainBannerPO bannerPO = bannerDao.selectByName();
		return bannerPO;
	}

}
