package com.staff.test.test;

import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;

public class Test {

	public static void main(String[] args) {

		String staffId = "43724599";
		// 加密
		String staffIdEncrypt = AESUtil.encrypt(staffId, AppConstants.QR_CODER_AES_STATIC_KEY);
		System.out.println(staffIdEncrypt);

		// 解密
		// String staffIdDecrypt = AESUtil.decrypt(staffId,
		// AppConstants.QR_CODER_AES_STATIC_KEY);
		// System.out.println(staffIdDecrypt);
	}
}
