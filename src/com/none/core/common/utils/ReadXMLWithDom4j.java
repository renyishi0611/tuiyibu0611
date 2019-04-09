package com.none.core.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.none.web.model.MenuVo;

public class ReadXMLWithDom4j {
	public static Logger logger = Logger.getLogger(ReadXMLWithDom4j.class);
	public static void main(String[] args) {
		//readXML();
	}

	@SuppressWarnings("rawtypes")
	public static List<MenuVo> readXML() {
		logger.info("准备读取菜单开始："+DateUtil.getDateTime());
		
		SAXReader reader = new SAXReader();
		List<MenuVo> list = new ArrayList<MenuVo>();
		MenuVo menu = null;
		Document document = null;
		try {
			logger.info("读取菜单开始："+DateUtil.getDateTime());
			document = reader.read("http://120.24.70.17:8080/download/menu.xml");
			logger.info("读取菜单完成："+DateUtil.getDateTime());
		} catch (DocumentException e) {
			logger.info("读取菜单出错："+DateUtil.getDateTime()+" 原因："+e.getMessage()+" *****"+e.getCause());
			e.printStackTrace();
		}
		Element root = document.getRootElement();

		Iterator it = root.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();

			// 未知属性名称情况下
			/*
			 * Iterator attrIt = element.attributeIterator(); while
			 * (attrIt.hasNext()) { Attribute a = (Attribute) attrIt.next();
			 * System.out.println(a.getValue()); }
			 */

			// 已知属性名称情况下
			/* System.out.println("id: " + element.attributeValue("id")); */

			// 未知元素名情况下
			/*
			 * Iterator eleIt = element.elementIterator(); while
			 * (eleIt.hasNext()) { Element e = (Element) eleIt.next();
			 * System.out.println(e.getName() + ": " + e.getText()); }
			 * System.out.println();
			 */

			// 已知元素名情况下
			menu = new MenuVo();
			
			menu.setId(element.elementText("id"));
			menu.setName(element.elementText("name"));
			menu.setImgicon(element.elementText("imgicon"));
			menu.setEntity(element.elementText("entity"));
			
			list.add(menu);
			
//			System.out.println("name: " + element.elementText("name"));
//			System.out.println("id: " + element.elementText("id"));
//			System.out.println("imgicon: " + element.elementText("imgicon"));
//			System.out.println();
		}
		logger.info("读取菜单结束："+DateUtil.getDateTime()+" ****"+list);
		return list;
	}
}
