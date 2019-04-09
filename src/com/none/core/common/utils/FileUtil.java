package com.none.core.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class FileUtil {
	/**
	 * 
	 * @param picPathList  要删除的文件列表
	 * @param path        path  要替换的服务器路径
	 * @throws UnsupportedEncodingException
	 */
	public static void deletefile(List<String > picPathList,String path,HttpServletRequest request) throws UnsupportedEncodingException {
		
		if(picPathList!=null && picPathList.size()>0){
			for(int i=0;i<picPathList.size();i++){
				String filepath = picPathList.get(i);
				path = path.substring(0, (path.length()-6));
				filepath = filepath.replace(path, "");
				//文件的物理路径
				
				String path_temp = request.getServletContext().getRealPath("/");
				path_temp=path_temp.substring(0, (path_temp.length()-6))+filepath;
				
				File file = new File(path_temp);
				if(file.exists()){
					//删除文件
					file.delete();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param picPath  图片地址
	 * @param path        path  要替换的服务器路径
	 * @throws UnsupportedEncodingException
	 */
	public static void deletefile(String picPath,String path,HttpServletRequest request) throws UnsupportedEncodingException {
		String filepath = picPath;
		path = path.substring(0, (path.length()-6));
		filepath = filepath.replace(path, "");
		//文件的物理路径
				
		String path_temp = request.getServletContext().getRealPath("/");
		path_temp=path_temp.substring(0, (path_temp.length()-6))+filepath;
				
		File file = new File(path_temp);
		if(file.exists()){
			//删除文件
			file.delete();
		}
	}
}
