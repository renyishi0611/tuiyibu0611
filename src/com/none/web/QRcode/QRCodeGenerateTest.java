package com.none.web.QRcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.none.web.QRcode.coder.DeviceChangeQRCoder;
import com.none.web.QRcode.coder.ForgetAppPasswordQRCoder;
import com.none.web.QRcode.coder.NewUserSignUpQRCoder;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.QRCodeUtil;

public class QRCodeGenerateTest {
	/**
	 * generateNewUserSignUpQRCode
	 * 
	 * @param staffId
	 * @param mail
	 * @param chineseName
	 * @param outputStream
	 *            FileOutputStream or Response.outputStream
	 * @throws IOException
	 * @throws WriterException
	 */
	public void generateNewUserSignUpQRCode(String staffId, String mail,
			String chineseName, OutputStream outputStream) throws IOException,
			WriterException {
		String[] valuesAdam = new String[] { "Mobile_GZ", null,
				"GLTc" };
		String departmentAdam = null;
		String mailAdam = null;
		String entityAdam = null;
		if (valuesAdam != null) {
			departmentAdam = valuesAdam[0];
			mailAdam = valuesAdam[1];
			entityAdam = valuesAdam[2];
		} else {
			// TODO: error message
		}
		// check
		boolean check = true;
		// check staffId
		if (staffId == null || staffId.isEmpty()
				|| staffId.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check entity
		if (entityAdam == null || entityAdam.isEmpty()
				|| entityAdam.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check department
		if (departmentAdam == null || departmentAdam.isEmpty()
				|| departmentAdam.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check mail
		String mailFinal = null;
		if (mailAdam == null || mailAdam.isEmpty()) {
			mailFinal = mail;
		} else {
			if (!mailAdam.equals(mail)) {
				// check = false;
				// TODO: error message
			}
			mailFinal = mailAdam;
		}
		if (mailFinal == null || mailFinal.isEmpty()
				|| mailFinal.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check chineseName
		if (chineseName == null || chineseName.isEmpty()
				|| chineseName.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// pass
		if (check) {
			// TODO: other actions. maybe save to the database

			// get statick key
			String staticKey = AppConstants.QR_CODER_AES_STATIC_KEY;
			// encrypt
			String content = NewUserSignUpQRCoder.encrypt(staffId,"", entityAdam,
					departmentAdam, mailFinal, chineseName, staticKey,"","");
			System.out.println(content);
			// generate QR code
			QRCodeUtil.encode(content, outputStream);
		} else {
			// TODO: error message

		}

	}

	/**
	 * generateForgetAppPasswordQRCode
	 * 
	 * @param staffId
	 * @param outputStream
	 *            FileOutputStream or Response.outputStream
	 * @throws IOException
	 * @throws WriterException
	 */
	public void generateForgetAppPasswordQRCode(String staffId,
			OutputStream outputStream) throws IOException, WriterException {
		// TODO:maybe get other information from database
		String entity = "unknown";
		String department = "unknown";
		String mail = "unknown";
		String chineseName = "unknown";
		// check
		boolean check = true;
		// check staffId
		if (staffId == null || staffId.isEmpty()
				|| staffId.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check entity
		if (entity == null || entity.isEmpty()
				|| entity.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check department
		if (department == null || department.isEmpty()
				|| department.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check mail
		if (mail == null || mail.isEmpty()
				|| mail.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check chineseName
		if (chineseName == null || chineseName.isEmpty()
				|| chineseName.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// pass
		if (check) {
			// get statick key
			String staticKey = AppConstants.QR_CODER_AES_STATIC_KEY;
			// encrypt
			String content = ForgetAppPasswordQRCoder.encrypt(staffId,"", entity,
					department, mail, chineseName, staticKey,"","");
			System.out.println(content);
			// generate QR code
			QRCodeUtil.encode(content, outputStream);
		} else {
			// TODO: error message

		}

	}

	/**
	 * generateDeviceChangeQRCode
	 * 
	 * @param staffId
	 * @param outputStream
	 *            FileOutputStream or Response.outputStream
	 * @throws IOException
	 * @throws WriterException
	 */
	public void generateDeviceChangeQRCode(String staffId,
			OutputStream outputStream) throws IOException, WriterException {
		// TODO:maybe get other information from database
		String entity = "unknown";
		String department = "unknown";
		String mail = "unknown";
		String chineseName = "unknown";
		// check
		boolean check = true;
		// check staffId
		if (staffId == null || staffId.isEmpty()
				|| staffId.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check entity
		if (entity == null || entity.isEmpty()
				|| entity.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check department
		if (department == null || department.isEmpty()
				|| department.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check mail
		if (mail == null || mail.isEmpty()
				|| mail.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// check chineseName
		if (chineseName == null || chineseName.isEmpty()
				|| chineseName.contains(AppConstants.QRCODER_SPLIT_CHAR)) {
			check = false;
			// TODO: error message
		}
		// pass
		if (check) {
			// get statick key
			String staticKey = AppConstants.QR_CODER_AES_STATIC_KEY;
			// encrypt
			String content = DeviceChangeQRCoder.encrypt(staffId,"", entity,
					department, mail, chineseName, staticKey,"","");
			System.out.println(content);
			// generate QR code
			QRCodeUtil.encode(content, outputStream);
		} else {
			// TODO: error message

		}

	}

	// DEMO

	public static void main(String[] args) throws WriterException, IOException,
			NotFoundException {
		QRCodeGenerateTest qr = new QRCodeGenerateTest();
		File file = null;
		FileOutputStream fos = null;

		// generateNewUserSignUpQRCode
		String staffId = "25145862";
		String mail = "kennethwkwong@hsbc.com.hk";
		String chineseName = "kennethwkwong";

		file = new File("/Users/yanjun/Documents/workspace/JavaWS/tmp/NewUserSignUpQRCode-Kennethwkong.png");
		fos = new FileOutputStream(file);
		qr.generateNewUserSignUpQRCode(staffId, mail, chineseName, fos);
		fos.close();

		// generateForgetAppPasswordQRCode
		staffId = "25145862";

		file = new File("/Users/yanjun/Documents/workspace/JavaWS/tmp/ForgetAppPasswordQRCode.png");
		fos = new FileOutputStream(file);
		qr.generateForgetAppPasswordQRCode(staffId, fos);
		fos.close();

		// generateDeviceChangeQRCode
		staffId = "25145862";

		file = new File("/Users/yanjun/Documents/workspace/JavaWS/tmp/DeviceChangeQRCode.png");
		fos = new FileOutputStream(file);
		qr.generateDeviceChangeQRCode(staffId, fos);
		fos.close();
	}
}
