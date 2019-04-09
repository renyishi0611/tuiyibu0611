package com.none.web.QRcode.coder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.none.core.common.utils.QRCodeUtil;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;

public class NewUserSignUpQRCoder {

	/**
	 * app用户 二维码信息加密
	 */
	public static String encrypt(String staffId, String entity, String buCode, String department, String mail,
			String chineseName, String lineManagerId, String lineManagerName, String key) {
		 String actionType = AppConstants.ACTIONTYPE_NEWUSERSIGNUP;// 激活
//		String actionType = AppConstants.ACTIONTYPE_DEVICECHANGE;// 更换设备
		String content = staffId + AppConstants.QRCODER_SPLIT_CHAR + entity + AppConstants.QRCODER_SPLIT_CHAR + buCode
				+ AppConstants.QRCODER_SPLIT_CHAR + department + AppConstants.QRCODER_SPLIT_CHAR + mail
				+ AppConstants.QRCODER_SPLIT_CHAR + chineseName + AppConstants.QRCODER_SPLIT_CHAR + lineManagerId
				+ AppConstants.QRCODER_SPLIT_CHAR + lineManagerName + AppConstants.QRCODER_SPLIT_CHAR + actionType
				+ AppConstants.QRCODER_SPLIT_CHAR + getCurrentTime();
		return AESUtil.encrypt(content, key);
	}

	// 加时间戳
	public static String getCurrentTime() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(c.getTime());
	}

	/**
	 * app用户 二维码信息解密
	 */
	public static String[] decrypt(String code, String key) {
		// 解密
		String content = AESUtil.decrypt(code, key);
		// 组装
		String[] values = content.split(AppConstants.QRCODER_SPLIT_CHAR_ESCAPE);
		if (values.length != 9) {
			// TODO: error message
		}
		return values;
	}

	// DEMO
	public static void main(String[] args) throws WriterException, IOException, NotFoundException {

		for (int i = 100; i < 150; i++) {
			//
			String staffId = "A0000" + i;
			// String staffId = "51000010";
			String name = staffId;
			// String name = "design10";
			String entity = "GLTc";
			String buCode = "";
			String department = "Mobile_GZ";
			String mail = name + "@ly.com.hk";
			String chineseName = name + "kennethwkwong";
			String lineManagerId = "";
			String lineManagerName = "";
			String fileName = staffId;

			String staticKey = AppConstants.QR_CODER_AES_STATIC_KEY;
			String content = NewUserSignUpQRCoder.encrypt(staffId, entity, buCode, department, mail, chineseName,
					lineManagerId, lineManagerName, staticKey);
			File file = null;
			FileOutputStream fos = null;
			// generateNewUserSignUpQRCode
			System.out.println(content);
			// file = new File("C://111//" + fileName + ".png");
			file = new File("C://999//" + fileName + ".png");
			fos = new FileOutputStream(file);
			QRCodeUtil.encode(content, fos);
			fos.close();
		}

		//
		// // generateForgetAppPasswordQRCode
		// content = ForgetAppPasswordQRCoder.encrypt(staffId, entity,
		// department, mail, chineseName, staticKey);
		// System.out.println("ForgetAppPasswordQRCoder:"+content);
		// file = new
		// File("/Users/yanjun/Documents/workspace/JavaWS/tmp/ForgetAppPasswordQRCode-"+fileName+".png");
		// fos = new FileOutputStream(file);
		// QRCodeUtil.encode(content, fos);
		// fos.close();
		//
		// // generateDeviceChangeQRCode
		// content = DeviceChangeQRCoder.encrypt(staffId, entity,
		// department, mail, chineseName, staticKey);
		// System.out.println("DeviceChangeQRCoder:"+content);
		// file = new
		// File("/Users/yanjun/Documents/workspace/JavaWS/tmp/DeviceChangeQRCoder-"+fileName+".png");
		// fos = new FileOutputStream(file);
		// QRCodeUtil.encode(content, fos);
		// fos.close();
		// for(int k=0;k<10;k++){
		// //System.out.println(encrypt("mary000"+k,"mary"+k,"mary000"+k,"mary"+k+"@163.com","mary"+k,AppConstants.QR_CODER_AES_STATIC_KEY));
		// //System.out.println(encrypt("Bing"+k,"","Bing"+k,"Bing"+k,"Bing"+k+"@163.com","Bing"+k,AppConstants.QR_CODER_AES_STATIC_KEY));
		// System.out.println(encrypt("Martin"+k,"HDPG","Martin"+k,"Martin"+k,"Martin"+k+"@163.com","Martin"+k,"33333","Bing",AppConstants.QR_CODER_AES_STATIC_KEY));
		// }
		// String[] str=decrypt("",AppConstants.QR_CODER_AES_STATIC_KEY);
		// for(int i=0;i<str.length;i++){
		// System.out.println(str[i]);
		// }
	}

}
