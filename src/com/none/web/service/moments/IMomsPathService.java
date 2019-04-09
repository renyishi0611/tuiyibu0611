package com.none.web.service.moments;

import java.util.List;
import java.util.Map;

import com.none.core.model.Pager;
import com.none.web.po.MomsPathPO;

public interface IMomsPathService {
	/**
	 * 
	* @Title: getAppMomsPathList 
	* @Description: TODO(根据momentsid获取文件图片列表) 
	* @param @param appmomentsId
	* @param @return
	* @param @throws Exception 设定文件 
	* @return List<AppMomsPathPO> 返回类型 
	* @throws
	 */
	List<MomsPathPO> getAppMomsPathList(int appmomentsId)throws Exception;
	/**
	 * 
	* @Title: getAppMomsPathCount 
	* @Description: TODO(根据momentsid获取文件地址列表总数) 
	* @param @param appmomentsId
	* @param @return
	* @param @throws Exception 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	int getAppMomsPathCount(int appmomentsId) throws Exception;
	/**
	 * 
	 * @Title: getSliderMoments
	 * @Description: TODO(获取含有文件的slidermoments列表)
	 * @param @return
	 * @param @throws Exception    参数
	 * @return List<AppMomsPathPO>    返回类型
	 * @throws
	 */
	//不要分页
	List<MomsPathPO> getSliderMoments()throws Exception;
	/**
	 * 
	 * @Title: getTotalCount
	 * @Description: TODO(查询总记录数)
	 * @param @param pager
	 * @param @return
	 * @param @throws Exception    参数
	 * @return Pager    返回类型
	 * @throwss
	 */
	Pager getTotalCount(Pager pager) throws Exception;
	/**
	 * 查询查询所有除PDF文件以外的所有文件的列表并按照momentsId分组
	 * @param 分页对象
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getTransform(Pager pager) throws Exception;
}
