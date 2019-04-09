package com.none.core.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.none.core.model.Result;

/**
 * QUI页面所需相关JSON字符串生成
 * 
 * @author Season
 * @date 2014年2月23日21:37:56
 * 
 */
public class JSONUtil {

	/**
	 * 根据状态和提示信息生成JSON
	 * 
	 * @param status
	 *            状态
	 * @param message
	 *            提示消息
	 * @return
	 */
	public static String getResult(int code, String message) {
		String resultJSON = "";
		if (StringUtils.isNotBlank(message)) {
			resultJSON = JSONUtil.toJSONString(new Result(code, message));
		}
		return resultJSON;
	}

	/**
	 * 根据状态和提示信息生成JSON
	 * 
	 * @param status
	 *            状态
	 * @param message
	 *            提示消息
	 * @return
	 */
	public static String getResult(int code, String message, Object data, Object[] obj) {
		String resultJSON = "";
		if (StringUtils.isNotBlank(message)) {
			resultJSON = JSONUtil.toJSONString(new Result(code, message, data, obj));
		}
		return resultJSON;
	}
	
	
	/**
	 * 根据状态和提示信息生成JSON
	 * 
	 * @param status
	 *            状态
	 * @param message
	 *            提示消息
	 * @return
	 */
	public static String getResult(int code, String message, Object data) {
		String resultJSON = "";
		if (StringUtils.isNotBlank(message)) {
			resultJSON = JSONUtil.toJSONString(new Result(code, message, data));
		}
		return resultJSON;
	}

	/**
	 * 根据状态生成JSON
	 * 
	 * @param status
	 *            状态
	 * @return
	 */
	public static String getResult(boolean status) {
		String resultJSON = "";
		if (status) {
			resultJSON = JSONUtil.toJSONString(new Result(0, "操作成功！"));
		} else {
			resultJSON = JSONUtil.toJSONString(new Result(1, "操作失败！"));
		}
		return resultJSON;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param arg0
	 * @return JSONString
	 */
	public static String toJSONString(Object arg0) {
		String resultJSON = XSSFilter.encode(JSON.toJSONString(arg0));
		Log.debug("JSON：" + resultJSON);
		return resultJSON;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param arg0
	 * @param arg1
	 * @return JSONString
	 */
	public static String toJSONString(Object arg0, SerializeFilter arg1) {
		String resultJSON = XSSFilter.encode(JSON.toJSONString(arg0, arg1));
		Log.debug("JSON：" + resultJSON);
		return resultJSON;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param arg0
	 * @param arg1
	 * @return JSONString
	 */
	public static Object toJSON(Object arg0) {
		JsonConfig jsonConfig = new JsonConfig();
		String[] newStr = new String[]{"hibernateLazyInitializer","handler"};
		jsonConfig.setExcludes(newStr);

		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {

			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg2 == null)
					return true;
				return false;
			}
		});
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new TimestampJsonValueProcessor());
		JSONObject json = JSONObject.fromObject(arg0, jsonConfig);
		Log.debug("JSON：" + json);
		return json;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param arg0
	 * @param arg1
	 *            排除对象的数组
	 * @return JSONString
	 */
	public static Object toJSON(Object arg0, String[] arg1) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arg1.length; i++) {
			list.add(arg1[i]);
		}
		list.add("hibernateLazyInitializer");
		list.add("handler");

		String[] newStr = list.toArray(new String[1]);
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setExcludes(newStr);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
 				if (arg2 == null) {
					return true;
				} else {
					return false;
				}
			}
		});
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new TimestampJsonValueProcessor());
		JSONObject json = JSONObject.fromObject(arg0, jsonConfig);
		Log.debug("JSON：" + json);
		return json;
	}

	/**
	 * 将对象转换为JSON对象，要排除
	 * 
	 * @param arg0
	 * @param classModel
	 *            过滤排除的class
	 * @param arg1
	 *            要显示的数组
	 * @return JSONString
	 */
	public static Object toJSON(Object arg0, Object classModel, String[] arg1) {
		// 得到需要排除的字段
		String[] arrResult = arrContrast(getClassItemName(classModel), arg1);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setExcludes(arrResult);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg2 == null)
					return true;
				return false;
			}
		});
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new TimestampJsonValueProcessor());
		JSONObject json = JSONObject.fromObject(arg0, jsonConfig);
		Log.debug("JSON：" + json);
		return json;
	}

	/**
	 * 将对象转换为JSON对象
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return JSONString
	 */
	public static String toJSONString(Object arg0, SerializeFilter arg1, SerializerFeature... arg2) {
		String resultJSON = XSSFilter.encode(JSON.toJSONString(arg0, arg1, arg2));
		Log.debug("JSON：" + resultJSON);
		return resultJSON;
	}

	/**
	 * 将对象转换为JSON对象(对时间进行格式化处理)
	 * 
	 * @author Season
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return String
	 */
	public static String toJSONStringWithDateFormat(Object arg0, String arg1, SerializerFeature... arg2) {
		String resultJSON = XSSFilter.encode(JSON.toJSONStringWithDateFormat(arg0, arg1, arg2));
		Log.debug("JSON：" + resultJSON);
		return resultJSON;
	}

	private static String[] getClassItemName(Object classModel) {
		// 获取实体类的所有属性，返回Field数组
		Field[] field = classModel.getClass().getDeclaredFields();
		// 获取属性的名字
		ArrayList<String> modelName = new ArrayList<String>();
		Field.setAccessible(field, true);
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			modelName.add(field[i].getName());
			if ((field[i].getGenericType().toString()).contains("Set")) {
				ParameterizedType pType = (ParameterizedType) field[i].getGenericType();
				Field[] fieldSets = ((Class<?>) pType.getActualTypeArguments()[0]).getDeclaredFields();
				for (int j = 0; j < fieldSets.length; j++) {
					// 获取属性的名字
					modelName.add(fieldSets[j].getName());
				}
			}
		}
		String[] array = (String[]) modelName.toArray(new String[modelName.size()]);
		return array;

	}

	/**
	 * 移除两个数组相同项
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private static String[] arrContrast(String[] arr1, String[] arr2) {
		List<String> list = new LinkedList<String>();
		for (String str : arr1) { // 处理第一个数组,list里面的值为1,2,3,4
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : arr2) { // 如果第二个数组存在和第一个数组相同的值，就删除
			if (list.contains(str)) {
				list.remove(str);
			}
		}
		String[] result = {}; // 创建空数组
		return list.toArray(result); // List to Array
	}
}
