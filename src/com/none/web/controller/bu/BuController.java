package com.none.web.controller.bu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.po.BuPO;
import com.none.web.service.bu.IBuService;

/**
 * 
 * @ClassName: BuController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jan
 * @date Dec 14, 2018 4:40:35 PM
 */
@Controller
@RequestMapping("bu")
public class BuController extends BaseControllerSupport {
	// private static Logger logger = Logger.getLogger(BuController.class);
	@Autowired
	private IBuService buService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @Title: createBu
	 * @Description: TODO(创建bu)
	 * @param buPO
	 * @param request
	 * @throws Exception
	 * @return Object 返回类型
	 */
	@ResponseBody
	@RequestMapping("createBu")
	public Object createBu(@RequestBody final BuPO buPO, HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		int i = buService.save(buPO, request);
		Result result;
		if (i != 0) {
			result = new Result(SysConstant.STATE_SUCCESS, "bu.createBuSuccess", data);
		} else {
			result = new Result(SysConstant.STATE_FAILURE, "bu.createBufailure", data);
		}
		return JSONUtil.toJSON(result);
	}

	/**
	 * 
	 * @Title: updateBu
	 * @Description: TODO(修改bu)
	 * @param buPO
	 * @throws Exception
	 * @return Object 返回类型
	 */
	@ResponseBody
	@RequestMapping("updateBu")
	public Object updateBu(@RequestBody final BuPO buPO) throws Exception {

		Result result;
		Map<String, Object> data = new HashMap<String, Object>();

		int updateResultNum = buService.update(buPO);

		// 修改t_user表的 branch
		String groupId = buPO.getId();
		String sql = "SELECT user_id,groupId, branch from t_user where groupId = :groupId ";
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupId", groupId);
		List<Map<String, Object>> userList = namedJdbcTemplate.queryForList(sql, paramMap);

		if (userList != null && userList.size() > 0) {
			String updateSql = "update t_user SET branch = ?  where  groupId = ?";
			this.jdbcTemplate.update(updateSql, buPO.getBu(), buPO.getId());
		}

		if (updateResultNum != 0) {
			result = new Result(SysConstant.STATE_SUCCESS, "bu.createBuSuccess", data);
		} else {
			result = new Result(SysConstant.STATE_FAILURE, "bu.createBuError", data);
		}
		return result;
	}

	/**
	 * 
	 * @Title: deleteBu
	 * @Description: TODO(删除bu，逻辑删除，改变isDelete状态，0：正常，1：删除)
	 * @param buPO
	 * @throws Exception
	 * @return Object 返回类型
	 */
	@ResponseBody
	@RequestMapping("deleteBu")
	public Object deleteBu(@RequestBody final BuPO buPO) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();

		Result result;
		int updateResultNum = buService.deleteBu(buPO);
		if (updateResultNum != 0) {
			result = new Result(SysConstant.STATE_SUCCESS, "bu.createBuSuccess", data);
		} else {
			result = new Result(SysConstant.STATE_FAILURE, "bu.createBuError", data);
		}
		return result;
	}

	/**
	 * 
	 * @Title: searchBu
	 * @Description: TODO(搜索bu)
	 * @param buPO
	 * @throws Exception
	 * @return Object 返回类型
	 */
	@ResponseBody
	@RequestMapping("searchBu")
	public Object searchBu(@RequestBody final BuPO buPO) throws Exception {

		Map<String, Object> resMap = buService.seachBuListByPage(buPO);
		Result result = new Result(SysConstant.STATE_SUCCESS, "bu.createBuSuccess", resMap);
		return JSONUtil.toJSON(result);

	}

	/**
	 * 
	 * @Title: detailBu
	 * @Description: TODO(bu详情)
	 * @param buPO
	 * @throws Exception
	 * @return Object 返回类型
	 */
	@ResponseBody
	@RequestMapping("detailBu")
	public Object detailBu(@RequestBody final BuPO buPO) throws Exception {

		// 校验id
		Integer isExist = buService.selectBuById(buPO.getId());
		Result result;
		if (isExist == 0) {
			result = new Result(SysConstant.REQUEST_ERROR, "bu.validateIdExist");
			return JSONUtil.toJSON(result);
		}
		result = new Result(SysConstant.STATE_SUCCESS, "bu.createBuSuccess", buService.detailBu(buPO));
		return JSONUtil.toJSON(result);

	}

}
