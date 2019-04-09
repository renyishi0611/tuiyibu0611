/** 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Bing
 * @date 2016年5月10日 上午9:44:38 
 */
package com.none.core.common.utils;


import org.apache.log4j.Logger;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;

/**
 * @Description: OSS操作工具类
 * @author Bing
 * @date 2016年5月10日 上午9:44:38
 */
public class OSSUtil implements Runnable{
	private static Logger logger = Logger.getLogger(OSSUtil.class);
	private OSSClient client;
	private String bucketName;
	private String diskName;
	private String key;
	
	public OSSClient getClient() {
		return client;
	}
	public void setClient(OSSClient client) {
		this.client = client;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
	public OSSUtil(OSSClient client, String bucketName,
			String diskName, String key) {
		super();
		this.client = client;
		this.bucketName = bucketName;
		this.diskName = diskName;
		this.key = key;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		OSSUtil.deleteFile(client, bucketName, diskName, key);	
	}
	/**
	 * 根据key删除OSS服务器上的文件
	 * 注：文件夹下只有一个文件，则文件夹也会一起删除；如果多个文件，只会删除指定文件名的文件  
	 * 
	 * @param client
	 *            OSS客户端
	 * @param bucketName
	 *            bucket名称
	 * @param diskName
	 *            文件路径
	 * @param key
	 *            Bucket下的文件的路径名+文件名
	 */
	public static void deleteFile(OSSClient client, String bucketName,
			String diskName, String key) {
		client.deleteObject(bucketName, diskName + key);
		logger.info(DateUtil.getYearMonthDayHMSCn()+":删除" + bucketName + "下的文件" + diskName + key + "成功");
	}

	/**
	 * 读取文件列表
	 * 
	 * @param client
	 * @param bucketName
	 * @throws OSSException
	 * @throws ClientException
	 */
	public static void listBucketFile(OSSClient client, String bucketName,String floder)
			throws OSSException, ClientException {
		/**
		 * 读取bucket下的文件，注意一次中能读出100条
		 */
		/*ObjectListing objectListing = client.listObjects(bucketName);
		List<OSSObjectSummary> listDeletes = objectListing.getObjectSummaries();
		for (int i = 0; i < listDeletes.size(); i++) {
			String objectName = listDeletes.get(i).getKey();
			System.out.println("objectName = " + objectName);
		}*/

		/**
		 * 递归列出指定目录下的所有文件
		 */
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// 递归列出指定目录下的所有文件
		listObjectsRequest.setPrefix(floder);

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object
		System.out.println("Objects:");
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			System.out.println(objectSummary.getKey());
		}

		// 遍历所有CommonPrefix
		System.out.println("\nCommonPrefixs:");
		for (String commonPrefix : listing.getCommonPrefixes()) {
			System.out.println(commonPrefix);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
