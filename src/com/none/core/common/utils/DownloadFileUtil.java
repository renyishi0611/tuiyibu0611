package com.none.core.common.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream; 

import javax.servlet.http.HttpServletResponse;

import com.none.core.exception.ValidateException;
import com.none.web.model.TUploadFiles;

/**
 * @说明 从网络获取文件到本地
 */
public class DownloadFileUtil {
	/**
	 * 下载单个文件
	 * 
	 * @param file：要下载的文件信息
	 * @param response
	 * @throws ValidateException 
	 */
	public static void downLoadSingle(TUploadFiles file, HttpServletResponse response) throws ValidateException {
		String filepath = file.getImg_video_path();
		//文件名称  图片的上传时间（精确到分）+活动前两个单词+图片上传者用户名+文件编号+10位随机字符
		String fileName = DateUtil.timeStampToDate3(file.getUpload_time().getTime()+"")+"_"
						  +StringUtil.getTwoWords(file.getTitle())+"_"
				          +file.getUserName()+file.getFileNo()+"_"
						  +StringUtil.getRandomString(10)
						  +filepath.substring(filepath.lastIndexOf("."), filepath.length());
		
		try {
			InputStream inStream =null;
			OutputStream out = null;
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			//根据文件http路径获取文件流
			URL url = new URL(filepath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");//必须设置为get
			conn.setConnectTimeout(5 * 1000);
			inStream = conn.getInputStream();// 通过输入流获取文件数据
			BufferedInputStream br = new BufferedInputStream(inStream);
			int len = 0;
			byte[] buf = new byte[1024];
			
			
			response.setContentType("applicatoin/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
			out = response.getOutputStream();
			while ((len = br.read(buf)) != -1)
				out.write(buf, 0, len);
			out.flush();
			out.close();
			br.close();
			inStream.close();
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			throw new ValidateException("download.fail");
		} catch (IOException e) {
			//e.printStackTrace();
			throw new ValidateException("download.fail");
		}
	}

	/**
	 * 下载多个文件，打包后下载
	 * @param list  要打包的文件信息
	 * @param title 活动title
	 * @param response
	 * @throws ValidateException 
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	public static void downLoadZipFile(List<TUploadFiles> list,String title,HttpServletResponse response) throws ValidateException{
		//zip文件名称：当前时间+活动名称前两个单词+10位随机数
		String zipFilename = DateUtil.getYMDHM()+"_"+StringUtil.getTwoWords(title)+"_"+StringUtil.randomNumbers(10);
		try {
			zipFilename = java.net.URLEncoder.encode(zipFilename, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		response.setContentType("applicatoin/octet-stream;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename="+zipFilename+".zip");
		response.setCharacterEncoding("UTF-8");
		try {
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			for(TUploadFiles file:list){
				//文件http路径
				String filepath = file.getImg_video_path();
				//文件名称  图片的上传时间（精确到分）+活动前两个单词+图片上传者用户名+文件编号+10位随机字符
				String fileName = DateUtil.timeStampToDate3(file.getUpload_time().getTime()+"")+"_"
								  +StringUtil.getTwoWords(file.getTitle())+"_"
						          +file.getUserName()+file.getFileNo()+"_"
								  +StringUtil.getRandomString(10);
				//fileName = java.net.URLEncoder.encode(fileName, "GBK");
				
				//根据文件http路径获取文件流
				URL url = new URL(filepath);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");//必须设置为get
				conn.setConnectTimeout(5 * 1000);
				InputStream inStream = conn.getInputStream();// 通过输入流获取文件数据
				
				zos.putNextEntry(new ZipEntry(fileName+filepath.substring(filepath.lastIndexOf("."), filepath.length())));
				
				zos.setEncoding("UTF-8"); 
		
				byte[] buffer = new byte[1024];
				int r = 0;
							
				while ((r = inStream.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				zos.flush();
				inStream.close();
				conn.disconnect();
			}
			zos.close();
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			throw new ValidateException("download.fail");
		} catch (IOException e) {
			//e.printStackTrace();
			throw new ValidateException("download.fail");
		}
	}
	
	/**
	 * 下载文件
	 * @param list
	 * @param title
	 * @param response
	 * @throws ValidateException
	 */
	public static void download(List<TUploadFiles> list,String title,String type,HttpServletResponse response) throws ValidateException{
		if(list ==null || list.size()<=0){
			throw new ValidateException("download.fail");
		}else if(list.size() == 1){
			downLoadSingle(list.get(0),response);
		}else{
			downLoadZipFile(list,title,response);
		}
	}
}