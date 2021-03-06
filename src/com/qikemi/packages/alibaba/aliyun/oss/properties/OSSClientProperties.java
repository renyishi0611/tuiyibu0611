package com.qikemi.packages.alibaba.aliyun.oss.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.qikemi.packages.utils.SystemUtil;

public class OSSClientProperties {

	private static Logger logger = Logger.getLogger(OSSClientProperties.class);

	private static Properties OSSKeyProperties = new Properties();
	// 阿里云是否启用配置
	public static String bucketName = "";
	public static boolean useStatus = false;
	public static String key = "";
	public static String secret = "";
	public static String endPoint = "";
	public static String downloadDomain = "";

	static {
		// String OSSKeyPath = SystemUtil.getProjectClassesPath()
		// + "OSSKey.properties";
		// 生成文件输入流
		// FileInputStream inpf = null;
		try {
			// inpf = new FileInputStream(new File(OSSKeyPath));
			// OSSKeyProperties.load(inpf);
			// useStatus = "true".equalsIgnoreCase((String)
			// OSSKeyProperties.get("useStatus")) ? true : false;
			// bucketName = (String) OSSKeyProperties.get("bucketName");
			// key = (String) OSSKeyProperties.get("key");
			// secret = (String) OSSKeyProperties.get("secret");
			// endPoint = (String) OSSKeyProperties.get("endPoint");

			// use JNDI config
			Context ctx;
			ctx = new InitialContext();
			OSSClientConf conf = (OSSClientConf) ctx.lookup("java:comp/env/bean/OSSKey");
			useStatus = conf.isUseStatus();
			bucketName = (String) conf.getBucketName();
			key = (String) conf.getKey();
			secret = (String) conf.getSecret();
			endPoint = (String) conf.getEndPoint();
			downloadDomain = (String) conf.getDownloadDomain();

		} catch (Exception e) {
			logger.warn("系统未找到指定文件：OSSKey.properties --> 系统按照ueditor默认配置执行。");
		}
	}

}
