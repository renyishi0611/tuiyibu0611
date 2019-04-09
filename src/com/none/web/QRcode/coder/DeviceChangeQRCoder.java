package com.none.web.QRcode.coder;

import java.io.IOException;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;

public class DeviceChangeQRCoder {
	/**
	 * encrypt
	 * 
	 * @param staffId
	 * @param entity
	 * @param department
	 * @param mail
	 * @param chineseName
	 * @param key
	 * @return
	 */
	public static String encrypt(String staffId, String entity,String buCode,
			String department, String mail, String chineseName, String lineManagerId,String lineManagerName, String key) {
		String actionType = AppConstants.ACTIONTYPE_DEVICECHANGE;
		String content = staffId + AppConstants.QRCODER_SPLIT_CHAR + buCode
				+AppConstants.QRCODER_SPLIT_CHAR + entity
				+ AppConstants.QRCODER_SPLIT_CHAR + department
				+ AppConstants.QRCODER_SPLIT_CHAR + mail
				+ AppConstants.QRCODER_SPLIT_CHAR + chineseName
				+ AppConstants.QRCODER_SPLIT_CHAR + lineManagerId
				+ AppConstants.QRCODER_SPLIT_CHAR + lineManagerName
				+ AppConstants.QRCODER_SPLIT_CHAR + actionType;
		return AESUtil.encrypt(content, key);
	}

	/**
	 * 
	 * @param code
	 * @param String
	 *            []{staffId,entity,department,mail,chineseName,actionType}
	 * @return
	 */
	public static String[] decrypt(String code, String key) {
		String content = AESUtil.decrypt(code, key);
		String[] values = content.split(AppConstants.QRCODER_SPLIT_CHAR_ESCAPE);
		// check
		if (values.length != 9) {
			// TODO: error message
		}
		return values;
	}
	
	public static void main(String[] args) throws WriterException, IOException,
	NotFoundException {

		for(int k=0;k<10;k++){
			//System.out.println(encrypt("mary000"+k,"mary"+k,"mary000"+k,"mary"+k+"@163.com","mary"+k,AppConstants.QR_CODER_AES_STATIC_KEY));
			//System.out.println(encrypt("Bing"+k,"","Bing"+k,"Bing"+k,"Bing"+k+"@163.com","Bing"+k,AppConstants.QR_CODER_AES_STATIC_KEY));
			System.out.println(encrypt("Martin"+k,"HDPG","Martin"+k,"Martin"+k,"Martin"+k+"@163.com","Martin"+k,"33333","Bing",AppConstants.QR_CODER_AES_STATIC_KEY));
		}
	}

}
