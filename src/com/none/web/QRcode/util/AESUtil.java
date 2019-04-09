package com.none.web.QRcode.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.none.web.QRcode.constants.AppConstants;

public class AESUtil {

	private static final String VIPARA = "0102030405060708";
	private static final int AESKEYSIZE = 16;

	/**
	 * encrypt
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encrypt(String content, String key) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		byte[] targetStr = new byte[AESKEYSIZE];
		byte[] keyStr = key.getBytes();
		int kenLength = keyStr.length;
		for (int i = 0; i < AESKEYSIZE; i++) {
			if (i < kenLength) {
				targetStr[i] = keyStr[i];
			} else {
				targetStr[i] = 0;
			}
		}

		String result = "";
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(targetStr, "AES");
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
			byte[] byteContent = content.getBytes("UTF-8");
			byte[] byteRresult = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteRresult.length; i++) {
				String hex = Integer.toHexString(byteRresult[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * decrypt
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String key) {
		if (content.length() < 1)
			return null;
		byte[] byteRresult = new byte[content.length() / 2];
		for (int i = 0; i < content.length() / 2; i++) {
			int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
			byteRresult[i] = (byte) (high * 16 + low);
		}

		byte[] targetStr = new byte[AESKEYSIZE];
		byte[] keyStr = key.getBytes();
		int kenLength = keyStr.length;
		for (int i = 0; i < AESKEYSIZE; i++) {
			if (i < kenLength) {
				targetStr[i] = keyStr[i];
			} else {
				targetStr[i] = 0;
			}
		}

		String result = "";
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(targetStr, "AES");
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
			byte[] byteContent = cipher.doFinal(byteRresult);
			result = new String(byteContent);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		   String key = "HSBCSCAQRAESSK2014";  
		   String a ="EA6DB6326C50AE09130310936FFA7B14";
		   System.out.println("解密后aaa：" + decrypt(a,key)); 
		
	}
	
	
	
}