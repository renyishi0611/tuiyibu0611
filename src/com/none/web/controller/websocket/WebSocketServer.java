package com.none.web.controller.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.none.core.common.utils.JSONUtil;
import com.none.web.po.LotteryPO;

@Component
@ServerEndpoint(value = "/websocket/webserver")
public class WebSocketServer {

	// public static final long MAX_TIME_OUT = 2 * 60 * 1000;
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		logger.info("------execute onPen......");
		// session.setMaxIdleTimeout(MAX_TIME_OUT); //session存活时长
		this.session = session;
		webSocketSet.add(this); // 加入set中
		addOnlineCount(); // 在线数加1
		logger.info("有新连接(对象：" + this + ")加入！当前在线人数为" + getOnlineCount());
		/*
		 * try { sendMessage("1");// 1表示连接成功 } catch (Exception e) {
		 * logger.error("websocket IO异常"); }
		 */
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		logger.info("------execute onClose......");
		if (webSocketSet.contains(this)) {
			webSocketSet.remove(this); // 从set中删除
			subOnlineCount(); // 在线数减1
		}
		logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	public void OnlineExist() {
		onClose();
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("来自客户端的消息:" + message);
		System.out.println("------onMessage......");
		// 群发消息
		for (WebSocketServer item : webSocketSet) {
			try {
				// item.sendMessage(message);
			} catch (Exception e) {
				logger.error(item.session.getId() + " send message fail");
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		if (webSocketSet.contains(this)) {
			webSocketSet.remove(this); // 从set中删除
			subOnlineCount(); // 在线数减1
		}
		logger.info("error...发生错误,有对象：" + this + "异常！当前在线人数为" + getOnlineCount());
		// error.printStackTrace();
	}

	public void sendMessage(String message) throws Exception {
		/*
		 * UserAppDao userAppDao = (UserAppDao)
		 * ApplicationContextRegister.getApplicationContext()
		 * .getBean(UserAppDao.class); List<Map<String, Object>> list =
		 * userAppDao.selectRankingList(); //
		 * this.session.getBasicRemote().sendText("99999999999999999");
		 * System.out.println(JSONUtil.toJSONString(list));
		 */
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String message) throws IOException {
		System.out.println("------sendInfo......");
		logger.info("【" + message + "】" + "send sucess");
		for (WebSocketServer item : webSocketSet) {
			try {
				item.sendMessage("88888888888888");
			} catch (Exception e) {
				logger.error(item.session.getId() + " send message fail");
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}

	// @Scheduled(cron="0 0/1 * * * ?")
	public void sendOrderNumToAdmin() throws Exception {
		System.out.println("------sendOrderNumToAdmin......");
		/*
		 * if (WebSocketServer.getOnlineCount() < 1) { logger.info("当前在线人数为" +
		 * getOnlineCount() + "不进行心跳检测"); return; } logger.info("当前在线人数为" +
		 * getOnlineCount() + "检测心跳"); Map<String, Object> data = new
		 * HashMap<String, Object>(); data.put("test", 2);
		 */
		// WebSocketServer.sendInfo(JSON.toJSONString(data));
	}

	public static void sendMessage2(List<LotteryPO> list) throws Exception {
		for (WebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(JSONUtil.toJSONString(list));
			} catch (Exception e) {
				logger.error(item.session.getId() + " send message fail");
				continue;
			}
		}
	}

	public static void sendMessage3(List<Map.Entry<String, Float>> list) throws Exception {
		for (WebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(JSONUtil.toJSONString(list));
			} catch (Exception e) {
				logger.error(item.session.getId() + " send message fail");
				continue;
			}
		}
	}

}
