package com.none.core.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceUtil {
	/**
	 * 替换字符串
	 * @param oldStr  原字符串
	 * @param someStr 要替换掉的字符串
	 * @param resultStr  要替换成什么
	 * @return
	 */
	 
	public static String replaceSomeString(String oldStr,String someStr,String resultStr){
		return oldStr.replaceAll(someStr, resultStr);
	}
	/**
	 * 修改样式
	 * @param oldStr
	 * @return
	 */
	public static String addStyle(String oldStr){
		return oldStr.replaceAll("style=\"\"/>", "style=\"margin-top: 10px; margin-bottom: 5px;\" width=\"100%\"/>")
					 .replaceAll("width=\"98%\"","width=\"100%\" style=\"margin-top: 10px; margin-bottom: 5px;\"")
					 .replaceAll("style=\"\" title=\"", "style=\"margin-top: 10px; margin-bottom: 5px;\" width=\"100%\" title=\"")
					 .replaceAll("<p>", "<p style=\"margin-top: 10px; margin-bottom: 5px; line-height: 1.75em;\">");
	}
	/**
	 * 给Ima标签添加width属性：有width就不处理，没有则添加
	 * @param oldStr
	 * @return
	 */
	public static String addWidth(String content){
		String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*/>";
		String oldStr = content;
		//System.out.println(oldStr);
        Matcher matcher = Pattern.compile(regxpForImgTag).matcher(content);
        while (matcher.find()) {
        	String contentTemp  = matcher.group(1);
            if(!contentTemp.contains("width")){
            	String contentTemp_old = contentTemp;
            	contentTemp = "width=\"100%\" "+contentTemp;
            	oldStr = oldStr.replaceAll(contentTemp_old, contentTemp);
            }
        }
        matcher.appendTail(new StringBuffer(oldStr));
        //System.out.println(addStyle(oldStr));
        return addStyle(oldStr);
	}
	
	public static String replace(String oldStr){
		return addWidth(oldStr);
	} 
	
	
	
	public static void main(String[] args) {
		//System.out.println(replace(replace("<img > style=\"\"/> style=\"\" title=\" <p><img")));
		String content = "<p><span style=\"font-family: &#39;Open Sans&#39;, Arial, sans-serif; font-size: 14px; line-height: 20px; text-align: justify; background-color: rgb(255, 255, 255);\">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip e</span></p><p style=\"margin-top: 10px; margin-bottom: 5px; line-height: 1.75em;\"><img src=\"http://uatdownload.redgltc.com/upload/image/20160511/1462931715954065636.jpg\" title=\"1462931715954065636.jpg\"/><span style=\"font-family: &#39;Open Sans&#39;, Arial, sans-serif; font-size: 14px; line-height: 20px; text-align: justify; background-color: rgb(255, 255, 255);\"><br/></span></p><p style=\"margin-top: 10px; margin-bottom: 5px; line-height: 1.75em;\"><span style=\"font-family: &#39;Open Sans&#39;, Arial, sans-serif; font-size: 14px; line-height: 20px; text-align: justify; background-color: rgb(255, 255, 255);\">x ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat null</span></p><p style=\"margin-top: 10px; margin-bottom: 5px; line-height: 1.75em;\"><span style=\"font-family: &#39;Open Sans&#39;, Arial, sans-serif; font-size: 14px; line-height: 20px; text-align: justify; background-color: rgb(255, 255, 255);\"></span></p><p style=\"text-align: center\"><img width=\"98%\" src=\"http://uatdownload.redgltc.com/upload/image/20160511/1462931716170086580.jpg\"/></p><p style=\"margin-top: 10px; margin-bottom: 5px; line-height: 1.75em;\"><span style=\"font-family: &#39;Open Sans&#39;, Arial, sans-serif; font-size: 14px; line-height: 20px; text-align: justify; background-color: rgb(255, 255, 255);\">a pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</span></p>";
		 System.out.println(replace(content));
	}
	
}
