package com.none.web.common.bdPush;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.none.core.common.utils.JSONUtil;

public class BaiduPushUtil {
	/**
	 * 推送通知给客户端
	 * title:推送的标题
	 * description：推送的描述
	 * deviceType：设备类型 ios／android
	 * push_id:百度推送server给的userId
	 * staff_type:类型  比如是news、activity、book等
	 * id:新闻ID或activity的ID或book的ID
	 */
	public static void push_bd(String  title,String description,String deviceType,String push_id,String staff_type,String id){
		
		if(title.length()>105){
		   	title=title.substring(0,105);
		}
		if(description.length()>160){
		   	description=description.substring(0,160);
		}
		   
		Map<String,Object> mapobj=new HashMap<String,Object>();
		String json=null;
		if("ios".equals(deviceType)){
			Map<String,String> aps=new HashMap<String,String>();
			Map<String,String> content=new HashMap<String,String>();
			aps.put("alert", title);
			mapobj.put("aps", aps);
			content.put("staff_type", staff_type);
			content.put("staff_id", id);
			mapobj.put("content", content);
			json=JSONUtil.toJSON(mapobj).toString();
			if(push_id!=null&&!"".equals(push_id)){
				Thread thread=new Thread(new IosBaiduPush(push_id,json),"Thread_"+new Date().getTime());
               	thread.start();
			}else{
				Thread thread=new Thread(new IosBaiduPush(null,json),"Thread_"+new Date().getTime());
               	thread.start();
			}
		}else{
			mapobj.put("title", title);
			mapobj.put("description", description);
			Map<String,String> custom_content=new HashMap<String,String>();
			custom_content.put("staff_type", staff_type);
			custom_content.put("staff_id", id);
			mapobj.put("custom_content", custom_content);
			json=JSONUtil.toJSON(mapobj).toString();
            if(push_id!=null&&!"".equals(push_id)){
               	Thread thread=new Thread(new AndroidBaiduPush(push_id,json),"Thread_"+new Date().getTime());
               	thread.start();
			}else{
				Thread thread=new Thread(new AndroidBaiduPush(null,json),"Thread_"+new Date().getTime());
               	thread.start();
			}
		}
	}
}
