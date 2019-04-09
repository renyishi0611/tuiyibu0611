package com.none.web.common.bdPush;

import java.io.UnsupportedEncodingException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.SetTagRequest;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.none.core.common.utils.DateUtil;

@SuppressWarnings("unused")
public class IosBaiduPush implements Runnable {
	public static Logger logger = Logger.getLogger(IosBaiduPush.class);
	// 设置developer平台的ApiKey/SecretKey
	static String apiKey = "";
	static String secretKey = "";
	static String deployStatus = "";
	private String userId = null;
	private String jsonValueStr = null;
	
//	static String apiKey = "59bC7D1sr4swenGEvm4EP4Ul";
//	static String secretKey = "RRNRfOvPO3p00ZGlTQaamEMGK8NgCMyb";
//	static String deployStatus = "1";

	/**
	 * 1.推送通知消息（给所有客户端推送） 通知request.setMessageType(1);
	 */
	public static void pushToAllClient(String jsonValueStr) {
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
			request.setDeviceType(4); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp

			// 若要通知，
			request.setMessageType(1);
			request.setDeployStatus(Integer.valueOf(deployStatus));// DeployStatus => 1: Developer 2:
										// Production
			request.setMessage(jsonValueStr);

			// 5. 调用pushMessage接口
			PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);

			// 6. 认证推送成功
			System.out.println("IOS push amount : " + response.getSuccessAmount());
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
					e.getErrorCode(), e.getErrorMsg()));
		}
	}

	/**
	 * 2.推送通知消息（根据channelId和userId给指定客户端推送）
	 * 指定给某个用户发送消息，由channelId和userId决定了客户端的唯一性 通知request.setMessageType(1);
	 */
	public static void pushToclientByUserId(String userId, Long channelId, String jsonValueStr) {
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(4); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp
			if (channelId != null && (!"".equals(channelId))) {
				request.setChannelId(channelId);
			}
			// 若要通知，
			request.setMessageType(1);
			request.setDeployStatus(Integer.valueOf(deployStatus));// DeployStatus => 1: Developer 2:
										// Production
			request.setMessage(jsonValueStr);
			request.setUserId(userId);
			// 5. 调用pushMessage接口
			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);

			// 6. 认证推送成功
			System.out.println("push amount : " + response.getSuccessAmount());
			logger.info("推送成功："+DateUtil.getDateTime()+" "+jsonValueStr);
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
			logger.info("推送失败："+DateUtil.getDateTime()+"推送内容‘"+jsonValueStr+"’,失败原因："+e.getMessage());
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
					e.getErrorCode(), e.getErrorMsg()));
			logger.info("推送失败："+DateUtil.getDateTime()+"推送内容‘"+jsonValueStr+"’,失败原因："+e.getMessage());
		}
	}
	
	
	/**
	 * 3.分组推送
	 */
//	public static void PushBatchUniMsg(String jsonValueStr) {
//		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
//
//		// 2. 创建BaiduChannelClient对象实例
//		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//
//		// 3. 若要了解交互细节，请注册YunLogHandler类
//		channelClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				System.out.println(event.getMessage());
//			}
//		});
//
//        try {
//        	SetTagRequest  res = new SetTagRequest();
//    		res.setTag("testGroup");
//    		res.setUserId("698145464602090006");
//    		channelClient.setTag(res);
//        	// 4. 创建请求类对象
//        	// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
//        	PushTagMessageRequest request = new PushTagMessageRequest();
//        	request.setDeviceType(4); // device_type => 1: web 2: pc 3:android
//        	request.setTagName("testGroup");
//        	// 若要通知，
//        	request.setMessageType(1);
//        	request.setDeployStatus(Integer.valueOf(deployStatus));// DeployStatus => 1: Developer 2:// Production
//        	request.setMessage(jsonValueStr);
//        	// 5. 调用pushMessage接口
//        	PushTagMessageResponse response = channelClient.pushTagMessage(request);
//
//        	// 6. 认证推送成功
//        	System.out.println("push amount : " + response.getSuccessAmount());
//        } catch (ChannelClientException e) {
//			// 处理客户端错误异常
//			e.printStackTrace();
//		} catch (ChannelServerException e) {
//			// 处理服务端错误异常
//			System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
//					e.getErrorCode(), e.getErrorMsg()));
//		}
//	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Long channelId = 5670539607064627831L;
		String userId = "600251026375159951";
		String jsonValueStr = "{\"aps\":{\"alert\":\"6666555666\"},\"content\":{\"staff_type\":\"video\",\"staff_id\":\"3\"}}";
		IosBaiduPush.pushToAllClient(jsonValueStr);
		//IosBaiduPush.pushToclientByUserId(userId, null, jsonValueStr);
		//String [] channelIds ={"600251026375159951"};
		//IosBaiduPush.PushBatchUniMsg(jsonValueStr);;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the jsonValueStr
	 */
	public String getJsonValueStr() {
		return jsonValueStr;
	}

	/**
	 * @param jsonValueStr
	 *            the jsonValueStr to set
	 */
	public void setJsonValueStr(String jsonValueStr) {
		this.jsonValueStr = jsonValueStr;
	}

	public IosBaiduPush(String userId, String jsonValueStr) {
		super();
		this.userId = userId;
		this.jsonValueStr = jsonValueStr;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (this.userId != null) {
			IosBaiduPush.pushToclientByUserId(this.userId, null, this.jsonValueStr);
		} else {
			IosBaiduPush.pushToAllClient(this.jsonValueStr);
		}
	}

	static {
		try {
			// use JNDI config
			Context ctx;
			ctx = new InitialContext();
			PushConf conf = (PushConf) ctx.lookup("java:comp/env/bean/pushConf");
			apiKey = conf.getIos_apiKey();
			secretKey = conf.getIos_secretKey();
			deployStatus=conf.getIos_deployStatus();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
