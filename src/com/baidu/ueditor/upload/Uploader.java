package com.baidu.ueditor.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.Bucket;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.baidu.ueditor.define.State;
import com.qikemi.packages.alibaba.aliyun.oss.BucketService;
import com.qikemi.packages.alibaba.aliyun.oss.OSSClientFactory;
import com.qikemi.packages.alibaba.aliyun.oss.ObjectService;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;
import com.qikemi.packages.baidu.ueditor.upload.AsynUploaderThreader;
import com.qikemi.packages.utils.SystemUtil;

public class Uploader {
	private static Logger logger = Logger.getLogger(Uploader.class);

	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = BinaryUploader.save(this.request, this.conf);
			JSONObject stateJson = new JSONObject(state.toJSONString());
			// 判别云同步
			if (OSSClientProperties.useStatus) {
				// AsynUploaderThreader asynThreader = new AsynUploaderThreader();
				// asynThreader.init(stateJson);
				// Thread uploadThreader = new Thread(asynThreader);
				// uploadThreader.start();
				
				OSSClient client = OSSClientFactory.createOSSClient();

				Bucket bucket = BucketService.create(client, OSSClientProperties.bucketName);
				// 获取key，即文件的上传路径
				String key = stateJson.getString("url").replaceFirst("/", "");
				try {
					FileInputStream fileInputStream = new FileInputStream(new File(
							SystemUtil.getProjectRootPath() + key));
					PutObjectResult result = ObjectService.putObject(client,
							bucket.getName(), key, fileInputStream);
					System.out.println(result.getETag());
					logger.debug("upload image[" + stateJson.getString("url") + "] to aliyun OSS success.");
				} catch (FileNotFoundException e) {
					logger.error("upload to aliyun OSS error, FileNotFoundException。");
				} catch (NumberFormatException e) {
					logger.error("upload to aliyun OSS error, NumberFormatException。");
				} catch (IOException e) {
					logger.error("upload to aliyun OSS error, IOException。");
				}
				
				state.putInfo("url", OSSClientProperties.downloadDomain + stateJson.getString("url"));
			} else {
				state.putInfo("url", "/" + SystemUtil.getProjectName() + stateJson.getString("url"));
			}
		}
		/*
		 * { "state": "SUCCESS", "title": "1415236747300087471.jpg", "original":
		 * "a.jpg", "type": ".jpg", "url":
		 * "/upload/image/20141106/1415236747300087471.jpg", "size": "18827" }
		 */
		logger.debug(state.toJSONString());
		return state;
	}
}
