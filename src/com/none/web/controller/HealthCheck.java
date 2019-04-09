package com.none.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthCheck {
	
	@RequestMapping(value="/hc",method=RequestMethod.GET)
	@ResponseBody
	public String healthCheck() {
		return "server is running!!";
	}
	
}
