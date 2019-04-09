package com.none.web.service.userApp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.none.web.model.TUserApp;
import com.none.web.po.ControlActivePO;
import com.none.web.po.LotteryPO;
import com.none.web.po.ReqAppUserListPO;
import com.none.web.po.UserAppPO;

/**
 * 用户管理Service
 */
public interface IUserAppService {

	/**
	 * 激活二维码
	 */
	Object activeQRCode(String code, String udid, HttpServletRequest request) throws Exception;

	/**
	 * 用户登录
	 */
	TUserApp mergeLoginUser(String userName, String udid, String isFirstLogin) throws Exception;

	/**
	 * change device QR code
	 */
	Object activeChangeDeviceQRCode(String code, String udid, HttpServletRequest request) throws Exception;

	/**
	 * CMS处分页查询在职的app端的员工
	 * 
	 * @param appUserPO
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectAppUserList(ReqAppUserListPO appUserPO) throws Exception;

	UserAppPO searchUserAppById(String userId, HttpServletRequest request) throws Exception;

	/**
	 * 导出appuser
	 * 
	 * @throws Exception
	 */
	void exportAppUserExcel(HttpServletResponse response) throws Exception;

	/**
	 * 导入appuser
	 * 
	 * @throws Exception
	 */
	void importAppUserExcel(MultipartFile file, HttpSession httpSession) throws Exception;

	// String validateAppUser(String userId, String udid) throws Exception;

	Integer changeAcceptTerms(String userId, String accept) throws Exception;

	/**
	 * 查询MyAccount信息
	 * 
	 */
	UserAppPO selectMyAccount() throws Exception;

	/**
	 * 上传MyAccount头像信息
	 */
	public String uploadImgToMyAccount(Map<String, String> map) throws Exception;

	/**
	 * saveMyAccount
	 * 
	 * @return
	 */
	UserAppPO saveMyAccount(UserAppPO userAppPO) throws Exception;

	UserAppPO selectUserByUserIdAndUdid(String userId, String udid) throws Exception;

	/**
	 * 用户登录中软App
	 * 
	 * @param userAppPO
	 * @return
	 * @throws Exception
	 */
	UserAppPO insertOrUpdateUser(UserAppPO userAppPO) throws Exception;

	String addLotteryNum(final LotteryPO po) throws Exception;

	String updatetTurnOff(ControlActivePO po) throws Exception;

	Map<String, Object> selectIfLucky(String staffId, String activeId) throws Exception;

	String selectActiveStatus(String staffId) throws Exception;

	void updateMissList() throws Exception;

	Integer selectPeoNum() throws Exception;

	int selectUserCountByUserId(String userid) throws Exception;

	String selectStaffIdByUserId(String logonUserId) throws Exception;

	void addPrize(String staffId,String activeid,Integer prizeLevel,String bu) throws Exception;

	List<Entry<String, Float>> selectResultOneByOne() throws Exception;

	void updateKey(String key) throws Exception;

	List<UserAppPO> getTopThreePrizePeople() throws Exception;

	void removeTop3PrizePeoples() throws Exception;

}
