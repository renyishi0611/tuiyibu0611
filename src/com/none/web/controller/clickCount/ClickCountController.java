package com.none.web.controller.clickCount;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.model.TClickCount;
import com.none.web.service.clickCount.IClickCountService;

import spring.mvc.bind.annotation.FormModel;

@Controller
@RequestMapping("clickCount")
public class ClickCountController extends BaseControllerSupport {

	@Autowired
	private IClickCountService iClickCountService;
	public static Logger logger = Logger.getLogger(ClickCountController.class);

	@ResponseBody
	@RequestMapping("save")
	public Object save(@FormModel("clickCount") final TClickCount clickCount) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("clickCount", iClickCountService.save(clickCount));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms统计浏览数量当天点击量：App端 banner slider的点击量
	 */
	@ResponseBody
	@RequestMapping("getBrowseTimes")
	public Object getBrowseTimes(String contentId, String type) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		String times = iClickCountService.getBrowseTimes(contentId, type);
		data.put("times", times);
		Result result = new Result(SysConstant.STATE_SUCCESS, "golbal.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms导出当天点击量：App端 banner slider的点击量
	 */
	@ResponseBody
	@RequestMapping("export")
	public Object export(HttpServletResponse response) throws Exception {
		String[] filter = new String[] {};
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("export", iClickCountService.export(response));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result, filter);
	}

}
