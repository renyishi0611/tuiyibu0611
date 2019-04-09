package com.none.core.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;

public class PathUtil {

	/**
	 * 获取tomcat Webapps路径
	 */
	public static String getServerWebappPath(HttpServletRequest request) {
		String tomcatRoot = request.getSession().getServletContext().getRealPath("/");
		String[] foo = tomcatRoot.split("/");
		StringBuilder tomcatWebAppsBuilder = new StringBuilder();
		int i = 0;
		for (String paths : foo) {
			++i;
			if (i != foo.length) {
				tomcatWebAppsBuilder.append(paths);
				tomcatWebAppsBuilder.append("/");
			}
		}
		return tomcatWebAppsBuilder.toString();
	}

	public static String getServerPath(HttpServletRequest request) {

		String addr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		return addr;
	}

	public static String readFile(String path) throws IOException {

		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader bfReader = new BufferedReader(reader);
			String tmpContent = null;
			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent);
			}
			bfReader.close();
		} catch (UnsupportedEncodingException e) {
			// 忽略
		}
		return PathUtil.filter(builder.toString());
	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private static String filter(String input) {
		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");

	}

	public static String readFileNotFilter(String path) throws IOException {

		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader bfReader = new BufferedReader(reader);
			String tmpContent = null;
			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent);
			}
			bfReader.close();
		} catch (UnsupportedEncodingException e) {
			// 忽略
		}
		return builder.toString();
	}

	/**
	 * 获取oss文件地址
	 * 
	 * @param fileName
	 *            文件名
	 * @param folderPath
	 *            文件路径
	 * @return oss上可以访问的全路径
	 * @throws IOException
	 */
	public static String getOssPath(String fileName, String folderPath) throws IOException {

		String endPoint = OSSClientProperties.endPoint;
		endPoint = endPoint.substring(0, endPoint.indexOf("//") + 2) + OSSClientProperties.bucketName + "."
				+ endPoint.substring(endPoint.indexOf("//") + 2, endPoint.length());
		return endPoint + "/" + folderPath + fileName;
	}
	
	public static String getUploadServerPath(HttpServletRequest request,String fileName,String folderPath) throws IOException {
		String endPoint = getServerWebappPath(request);
		return endPoint + File.separatorChar + folderPath + fileName;
	}

}
