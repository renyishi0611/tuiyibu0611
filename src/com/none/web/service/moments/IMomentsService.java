package com.none.web.service.moments;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.none.core.model.Pager;
import com.none.web.po.CommentPO;
import com.none.web.po.MomentPO;
import com.none.web.po.ReqMomListPO;
import com.none.web.po.UserAppPO;

public interface IMomentsService {

//	/**
//	 * 上传图片，视频，pdf格式文件
//	 * 
//	 * @param multipartFile
//	 * @return
//	 * @throws Exception
//	 */
//	public String uploadMomentsFile(MultipartFile multipartFile) throws Exception;

	/**
	 * 获取返回前台上传文件的文件类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUploadFileType(MultipartFile multipartFile) throws Exception;

	/**
	 * 新增时publish、save功能直接插入数据库
	 * 
	 * @param moments
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Object addMoment(MomentPO momentPO,HttpServletRequest request) throws Exception;

	/**
	 * 新增后的moment做publish和save必须传id进行更改(通过id)
	 * 
	 * @param momentsPO
	 * @return
	 * @throws Exception
	 */
	public Object updateMoment(MomentPO momentPO) throws Exception;

	/**
	 * 编辑功能，实际是一个跳转查询(通过id)
	 * 
	 * @param momentPO
	 * @return
	 * @throws Exception
	 */
	public Object getMomentById(MomentPO momentPO) throws Exception;

	/**
	 * 删除功能，通过id删除moment
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object removeMoment(MomentPO momentPO) throws Exception;

	/**
	 * moments 审核接口
	 * 
	 * @param momId
	 *            该条moments的id
	 * @param state
	 *            审核状态 2:审核通过,3审核未通过
	 * @return 返回状态 :0:成功, -1失败
	 */
	int reviewMoments(String momId, String state) throws Exception;

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
	 * 查询momengt记录列表
	 */
	/*
	 * List<MomentsPO> getMomentsList(Pager pager, String userid) throws
	 * Exception;
	 * 
	 *//**
		 * 查询总记录条数
		 *//*
		 * Pager getTotalCount(Pager pager) throws Exception;
		 */

	/**
	 * 分页查询cms端moments的列表
	 * 
	 * @param page
	 *            分页实体类
	 * @param userName
	 *            用户名
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectMomList(ReqMomListPO po) throws Exception;

	/**
	 * CMS按照时间段导出moment的excel
	 * 
	 * @param reqMap
	 * @param request
	 * @param response
	 */
	void exportExcel(Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	/**
	 * APP端获取moments最新的5个列表 @Title: getAppMomentsList @Description:
	 * TODO(查询moments记录列表，分页显示) @param @param
	 * pager @param @return @param @throws Exception 参数 @return List
	 * <AppMomentsPO> 返回类型 @throws
	 */
	List<MomentPO> getAppMomentsList(Pager pager) throws Exception;// 需求：userId改成LogonUserId

	/**
	 * 查询moments总记录数，没有删除，且是published
	 * 
	 * @Title: getTotalCount
	 * @Description: TODO(查询总记录数)
	 * @param @param
	 *            pager
	 * @param @return
	 * @param @throws
	 *            Exception 参数
	 * @return Pager 返回类型
	 * @throwss
	 */
	Pager getTotalCount(Pager pager) throws Exception;

	/**
	 * 验证用户是否存在
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	// String validateUser(String userId) throws Exception;

	/**
	 * 验证用户是否存在cms端
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String validateCMSUser(String userId) throws Exception;
	/**
	 * 获取第1个帖子，第100个帖子，第200个帖子，第500个帖子
	 * @return
	 * @throws Exception
	 */
	public Object getLotteryMomentList(Pager pager) throws Exception;
	/**
	 * 获取'第1个帖子，第100个帖子，第200个帖子，第500个帖子'文章列表总数
	 * @param pager
	 * @return
	 */
	public Object getLotteryMomentCount(Pager pager) throws Exception;
	/**
	 * 上传文件到tomcat服务器
	 * @param multipartFile
	 * @param request
	 * @return
	 * @throws Exception
	 */
	String uploadMomentsFile(MultipartFile multipartFile, HttpServletRequest request) throws Exception;
	/**
	 * 获取'我的文章'列表，从session中获取userId查询当前登录用户名下的文章列表
	 * @param pager
	 * @return
	 */
	public Object getMyAppMomentsList(Pager pager) throws Exception;
	/**
	 * 获取'我的文章'列表文章总数
	 * @param pager
	 * @return
	 * @throws Exception
	 */
	public Pager getMyMomentTotalCount(Pager pager) throws Exception;
	/**
	 * 获取'我的收藏'列表，从session中获取userId查询当前登录用户名下收藏的文章列表
	 * @param pager
	 * @return
	 */
	public Object getMyCollectionMomentList(Pager pager) throws Exception;
	/**
	 * 获取'我的收藏'列表文章总数
	 * @param pager
	 * @return
	 */
	public Pager getMyCollectionCount(Pager pager) throws Exception;
	/**
	 * 获取'Top3文章'中奖人列表，
	 * @param pager
	 * @return
	 * @throws Exception
	 */
	public List<UserAppPO> getTopThreeUserInfoList() throws Exception;

	public int getCurrentMomentIndex() throws Exception;

	public int selectCurrentUserMomentCount(String logonUserId) throws Exception;

	public Object getMyPrizeByUserId(String logonUserId) throws Exception;

	public boolean isLunky(String logonUserId) throws Exception;

	public List<UserAppPO> getThunderUserInfoList() throws Exception;

	public List<String> getStaffList() throws Exception;
}
