package com.none.web.service.voice;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.none.web.po.CommentPO;
import com.none.web.po.ReqVoiceListPO;
import com.none.web.po.ReqVoiceOfComListPO;
import com.none.web.po.VoiceOfStaffPO;

/**
 * 员工心声service接口
 * 
 * @author winter
 *
 */
public interface IVoiceOfStaffService {

	/**
	 * 新增员工心声,新增成功返回true,失败返回false
	 * 
	 * @param content
	 *            内容
	 * @param status
	 *            新增状态
	 * @param submit_user
	 *            提交人
	 * @return
	 * @throws Exception
	 */
	void insertVoiceOfStaff(VoiceOfStaffPO po) throws Exception;

	/**
	 * 修改员工心声
	 * 
	 * @param po
	 * @throws Exception
	 */
	void updateVoice(VoiceOfStaffPO po) throws Exception;

	/**
	 * 员工心声列表
	 * 
	 * @param po
	 *            列表条件查询
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectVoiceList(ReqVoiceListPO po) throws Exception;

	/**
	 * 获取voice详情
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	VoiceOfStaffPO selectVoiceDetail(ReqVoiceListPO po) throws Exception;

	/**
	 * 查询评论列表
	 * 
	 * @param po
	 *            评论列表入参
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectVoiceOfComList(ReqVoiceOfComListPO po, HttpServletRequest request) throws Exception;

	/**
	 * 评论接口
	 * 
	 * @param commentPo
	 *            评论实体类
	 * @return 返回状态:0:成功,-1:失败
	 * @throws Exception
	 */
	int saveComment(CommentPO commentPo) throws Exception;

	/**
	 * 删除评论
	 * 
	 * @param comId
	 *            评论id
	 * @throws Exception
	 */
	void delComment(int comId) throws Exception;

	/**
	 * CMS按照时间段导出voice以及评论的excel
	 * 
	 * @param reqMap
	 * @param request
	 * @param response
	 */
	void exportExcel(Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

}
