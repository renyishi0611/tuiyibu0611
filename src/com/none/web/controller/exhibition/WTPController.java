package com.none.web.controller.exhibition;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Pager;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.service.moments.IMomsPathService;
/**
 * workplaceTransfermation管理Controller
 * 
 * @author King
 * 
 */
@Controller
@RequestMapping("wtp")
public class WTPController extends BaseControllerSupport {
	private static Logger logger = Logger.getLogger(WTPController.class);
	@Autowired
	private IMomsPathService momsPathService;
	/**
	 * 查询WTP列表，按momentsId分组,文件对象，统计该momentsId下的资源文件数量
	 * @param pager 分页对象
	 * @return 返回一个wtp对象列表，momentsId,对象属性包含文件缩略图路径,文件数量，文件类型
	 * @throws Exception 异常抛出
	 */
	@ResponseBody
	@RequestMapping("getTransformList")
	public Object getTransformList(@RequestBody Pager pager) throws Exception {
		String[] filter = new String[] {};
		Map<String, Object> data = null;
		int code = SysConstant.STATE_SUCCESS;
		String msg = "wtp.success";

		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		try {
			data = momsPathService.getTransform(pager);
		} catch (Exception e) {
			logger.error("query is fail, please check again", e);
			e.printStackTrace();
			code = SysConstant.STATE_FAILURE;
			msg = "wtp.fail";
		}
		logger.info(data);
		Result result = new Result(code, msg, data);
		return JSONUtil.toJSON(result, filter);
	}

}
