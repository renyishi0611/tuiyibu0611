package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.core.model.Pager;
import com.none.web.po.MomentDetailPathPO;
import com.none.web.po.MomsPathPO;
import com.none.web.utils.BaseDao;

@Repository
public interface MomsPathDao extends BaseDao<MomsPathPO> {
	/**
	 * 
	* @Title: selectPathListByMomentsId 
	* @Description: TODO(根据momentsId查询文件图片列表) 
	* @param @param appmomentsId
	* @param @return
	* @param @throws Exception 设定文件 
	* @return List<AppMomsPathPO> 返回类型 
	* @throws
	 */
	List<MomsPathPO> selectPathListByMomentsId(int appmomentsId) throws Exception;
	/**
	 * CMS端查询当前momentsId下的资源文件列表
	 * @param momentsId 当前momentsId
	 * @return 返回一个momsPaths路径列表
	 * @throws Exception 抛出异常
	 */
	List<MomentDetailPathPO> selectCMSPathListByMomentsId(int momentsId) throws Exception;
	/**
	 * 
	* @Title: selectPathCountByMomentsId 
	* @Description: TODO(根据momentsId获取文件图片地址列表总数) 
	* @param @param appmomentsId
	* @param @return
	* @param @throws Exception 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	int selectPathCountByMomentsId(int appmomentsId)throws Exception;
	/**
	 * 
	 * @Title: selectSliderMomentsList
	 * @Description: TODO(查询slidermoments列表)
	 * @param @return
	 * @param @throws Exception    参数
	 * @return List<AppMomsPathPO>    返回类型
	 * @throws
	 */
	//不要分页
	List<MomsPathPO> selectSliderMomentsList()throws Exception;
	/**
	 * 
	 * @Title: selectTotalCount
	 * @Description: TODO(查询总记录数，这里是根据momentsid分组后的总记录数，就是含有图片、pdf等文件的moments的条数)
	 * @param @param pager
	 * @param @return
	 * @param @throws Exception    参数
	 * @return int    返回类型
	 * @throws
	 */
	int selectCountSliderMoments(Pager pager) throws Exception;
	/**
	 * 插入上传的文件路径信息
	 * @param momsPathPO
	 * @return
	 * @throws Exception
	 */
	void insertMomPath(MomentDetailPathPO momsPathPO) throws Exception;
	/**
	 * 通过momid删除它下面的path列表
	 * @param MomId
	 * @throws Exception
	 */
	void deletePathByMomId(int id) throws Exception;
	/**
	 * 查询所有除PDF文件的所有文件的列表并且按照momentsId分组
	 * @return
	 * @throws Exception
	 */
	List<MomsPathPO> getTransformList(Map<String, Object> map) throws Exception;
	/**
	 * 查询所有除PDF文件的所有文件的列表元素的总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int getTransformCount() throws Exception;
}
