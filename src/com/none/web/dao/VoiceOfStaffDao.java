package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.po.ReqVoiceListPO;
import com.none.web.po.UserAppPO;
import com.none.web.po.VoiceExcelPO;
import com.none.web.po.VoiceOfStaffPO;
import com.none.web.utils.BaseDao;

/**
 * 员工心声dao
 * 
 * @author winter
 *
 */
@Repository
public interface VoiceOfStaffDao extends BaseDao<UserAppPO> {

	/**
	 * 查询voice
	 */
	VoiceOfStaffPO selectById(String id) throws Exception;

	/**
	 * 查询voice的列表
	 * 
	 * @return
	 * @throws Exception
	 */
	List<VoiceOfStaffPO> selectVoiceList(ReqVoiceListPO po) throws Exception;

	/**
	 * 查询分页总数
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	int selectVoiceListCount(ReqVoiceListPO po) throws Exception;

	/**
	 * 查询该条voice的评论数量
	 * 
	 * @param voiceId
	 * @return
	 * @throws Exception
	 */
	int selectCountOfVoice(Integer voiceId) throws Exception;

	/**
	 * 新增voice
	 * 
	 * @param content
	 *            内容
	 * @param status
	 *            状态0:发布状态,1:隐藏
	 * @param submit_user
	 *            提交人
	 * @return
	 * @throws Exception
	 */
	int insertVoiceOfStaff(VoiceOfStaffPO po) throws Exception;

	/**
	 * 修改voiceId修改voice
	 * 
	 * @param po
	 * @throws Exception
	 */
	void updateVoiceById(VoiceOfStaffPO po) throws Exception;

	/**
	 * 按时间查询导出excel所需的数据
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<VoiceExcelPO> selectVoiceExcel(Map<String, Object> map) throws Exception;

}
