package com.none.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * 通用http发送方法 * * @author King
 */
public class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

	public static void main(String[] args) {
		String responseData = HttpUtils.sendGet("http://pbox.chinasoftinc.com/Pmo/service/api/employee/search",
				"E000862694");

		System.out.println(responseData);

		boolean isLegalUser = false;
		String[] propertyArr = responseData.replace("{", "").replace("}", "").split(",");

		Map<String, String> employer = new HashMap<String, String>();
		String key = "", value = "";

		for (int i = 0; i < propertyArr.length; i++) {
			String[] keyValue = propertyArr[i].split(":");
			key = keyValue[0];
			value = keyValue[1];
			employer.put(key, value);
		}

		if (!employer.containsKey("msg")) {
			isLegalUser = true;
		}

		System.out.println(isLegalUser);
		System.out.println(employer);
	}

	/**
	 * 向指定 URL 发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;

		try {
			String urlNameString = url + "/" + param;
			// String urlNameString = url;
			log.info("sendGet - {}", urlNameString);

			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;

			while ((line = in.readLine()) != null) {
				result.append(line);
			}

			log.info("recv - {}", result);
		} catch (ConnectException e) {
			log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e.getMessage());
		} catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e.getMessage());
		} catch (IOException e) {
			log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e.getMessage());
		} catch (Exception e) {
			log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				log.error("调用in.close Exception, url=" + url + ",param=" + param, ex.getMessage());
			}
		}

		return result.toString();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();

		try {
			String urlNameString = url + "?" + param;
			log.info("sendPost - {}", urlNameString);

			URL realUrl = new URL(urlNameString);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

			String line;

			while ((line = in.readLine()) != null) {
				result.append(line);
			}

			log.info("recv - {}", result);
		} catch (ConnectException e) {
			log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e.getMessage());
		} catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e.getMessage());
		} catch (IOException e) {
			log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e.getMessage());
		} catch (Exception e) {
			log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("调用in.close Exception, url=" + url + ",param=" + param, ex.getMessage());
			}
		}

		return result.toString();
	}

	public static String sendSSLPost(String url, String param) {
		StringBuilder result = new StringBuilder();
		String urlNameString = url + "?" + param;

		try {
			log.info("sendSSLPost - {}", urlNameString);

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

			URL console = new URL(urlNameString);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.connect();

			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String ret = "";

			while ((ret = br.readLine()) != null) {
				if ((ret != null) && !ret.trim().equals("")) {
					result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
				}
			}

			log.info("recv - {}", result);
			conn.disconnect();
			br.close();
		} catch (ConnectException e) {
			log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e.getMessage());
		} catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e.getMessage());
		} catch (IOException e) {
			log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e.getMessage());
		} catch (Exception e) {
			log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e.getMessage());
		}

		return result.toString();
	}

	/**
	 * @desc:
	 * @param postData
	 *            请求参数（json格式）
	 * @param postUrl
	 *            请求的URL地址
	 * @return 返回数据（string格式）
	 */
	public static String JsonSMS(String postData, String postUrl) {
		try {
			// 发送POST请求
			URL url = new URL(postUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", "" + postData.length());

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(postData);
			out.flush();
			out.close();

			// 获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("connect failed!");

				return "";
			}

			// 获取响应内容体
			String line;

			// 获取响应内容体
			String result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

			while ((line = in.readLine()) != null) {
				result += (line + "\n");
			}

			in.close();

			return result;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}

		return "";
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public static boolean validateEhr(String ehrNo) {
		boolean isLegalUser = false;

		String url = "http://pbox.chinasoftinc.com/Pmo/service/api/employee/search";

		String responseData = HttpUtils.sendGet(url, ehrNo);

		String[] propertyArr = responseData.replace("{", "").replace("}", "").split(",");

		Map<String, String> employer = new HashMap<String, String>();
		String key = "", value = "";

		for (int i = 0; i < propertyArr.length; i++) {
			String[] keyValue = propertyArr[i].split(":");
			key = keyValue[0];
			value = keyValue[1];
			employer.put(key, value);
		}

		if (!employer.containsKey("msg")) {
			isLegalUser = true;
		}

		return isLegalUser;
	}
}