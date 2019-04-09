package com.none.core.common.utils;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;


public class QRCodeUtil {
	private static Logger logger = Logger.getLogger(QRCodeUtil.class);
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	/**
	 * encode
	 * 
	 * @param content
	 * @param outputStream
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static OutputStream encode(String content, OutputStream outputStream)
			throws WriterException, IOException {
		return QRCodeUtil.encode(content, outputStream, 250, 250, null);
	}

	/**
	 * encode
	 * 
	 * @param content
	 * @param outputStream
	 * @param logoPath
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static OutputStream encode(String content,
			OutputStream outputStream, String logoPath) throws WriterException,
			IOException {
		return QRCodeUtil.encode(content, outputStream, 250, 250, logoPath);
	}
	
	/**
	 * encode
	 * 
	 * @param content
	 * @param outputStream
	 * @param logoPath
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static OutputStream encode(String content,
			OutputStream outputStream, String logoPath,int with,int height) throws WriterException,
			IOException {
		return QRCodeUtil.encode(content, outputStream, with, height, logoPath);
	}

	/**
	 * encode
	 * 
	 * @param content
	 * @param outputStream
	 * @param width
	 * @param height
	 * @param logoPath
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static OutputStream encode(String content,
			OutputStream outputStream, int width, int height, String logoPath)
			throws WriterException, IOException {
		// L:7%;M:15%;Q:25%;H:30%.
		return QRCodeUtil.encode(content, outputStream, ErrorCorrectionLevel.L,
				width, height, logoPath);
	}

	/**
	 * encode
	 * 
	 * @param content
	 * @param outputStream
	 * @param level
	 * @param width
	 * @param height
	 * @param logoPath
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public static OutputStream encode(String content,
			OutputStream outputStream, ErrorCorrectionLevel level, int width,
			int height, String logoPath) throws WriterException, IOException {
		//logoPath="";
		BitMatrix byteMatrix;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// hints.put(EncodeHintType.MARGIN, 1);
		// L:7%;M:15%;Q:25%;H:30%.
		hints.put(EncodeHintType.ERROR_CORRECTION, level);
		// hints.put(EncodeHintType.MAX_SIZE, 350);
		// hints.put(EncodeHintType.MIN_SIZE, 100);
		byteMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, width, height, hints);
		if (logoPath == null || logoPath.equals("")) {
			MatrixToImageWriter.writeToStream(byteMatrix, "png", outputStream);
		} else {
			writeLogoImage(byteMatrix, "png", outputStream, logoPath);
		}
		return outputStream;
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		System.out.println("begin toBufferedImage:"+width+","+height);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		System.out.println("continue toBufferedImage");
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	public static BufferedImage toBufferedImage(BitMatrix matrix,BufferedImage image) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	

	/**
	 * writeLogoImage
	 * 
	 * @param matrix
	 * @param format
	 * @param outputStream
	 * @param logoPath
	 * @throws IOException
	 */
	public static void writeLogoImage(BitMatrix matrix, String format,
			OutputStream outputStream, String logoPath) throws IOException {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		System.out.println("begin toBufferedImage:"+width+","+height);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		 image = toBufferedImage(matrix,image);
		System.out.println("writeLogoImage");
		Graphics2D gs = image.createGraphics();

		// load logo
		File logoFile = new File(logoPath);
		BufferedImage logoImage = ImageIO.read(logoFile);
		//中间小图标的宽度与高度
		int widthLogo = logoImage.getWidth(), heightLogo = logoImage
				.getHeight();
		if(widthLogo>185){
			widthLogo = 185;
		}
		if(heightLogo>108&&heightLogo!=120){
			heightLogo = 108;
		}
		//System.out.println(widthLogo  +"===="+ heightLogo);
		int x = (image.getWidth() - widthLogo) / 2;
		int y = (image.getHeight() - heightLogo) / 2;
		gs.drawImage(logoImage, x, y, widthLogo, heightLogo, null);
		//gs.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
		LogoConfiguration logoConfig = new LogoConfiguration(null,5);
		gs.setStroke(new BasicStroke(5f));
		gs.setColor(logoConfig.getBorderColor());
		//gs.drawRect(x, y, widthLogo, heightLogo);

		gs.dispose();
		logoImage.flush();
		ImageIO.write(image, format, outputStream);
		image=null;
	}

	/**
	 * decode
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws NotFoundException
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static String decode(InputStream inputStream) throws IOException,
			NotFoundException {
		String resultStr = null;
		Reader reader = new MultiFormatReader();
		BufferedImage image;
		image = ImageIO.read(inputStream);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable hints = new Hashtable();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
		result = new MultiFormatReader().decode(bitmap, hints);
		resultStr = result.getText();
		return resultStr;
	}
	
	public static String genQrcode(String serverPath,String content,String picName,String logoPath){
		File file = new File(serverPath+"/QRcodeImages/");
		if (!file.exists()) {  
            if (!file.mkdir()) {  
                return null;  
            }  
        }  
		file = new File(serverPath+"/QRcodeImages/"+DateUtil.getYearMonth());
		if (!file.exists()) {  
            if (!file.mkdir()) {  
                return null;  
            }  
        }  
		file = new File(serverPath+"/QRcodeImages/"+DateUtil.getYearMonth()+"/"+picName);
		if(file.exists())
		{
			return "/QRcodeImages/"+DateUtil.getYearMonth()+"/"+picName;
		}
		else
		{		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			QRCodeUtil.encode(AESUtil.encrypt(content, AppConstants.QR_CODER_AES_STATIC_KEY), fos, logoPath);
			//System.out.println("-----  generate code:" + content);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/QRcodeImages/"+DateUtil.getYearMonth()+"/"+picName;
		}
	}
	
	
	/**
	 * 
	 * @param serverPath   服务器路径
	 * @param content  二维码内容
	 * @param picName  生成后二维码图片的名称
	 * @param logoPath 二维码logo 图片地址
	 * @param folder   存放二维码的文件夹
	 * @param width    二维码宽度
	 * @param height   二维码高度
	 * @param flag     是否需要加密
	 * @param appInstallPath  #red app安装地址
	 * @return
	 */
	public static String genQrcode(String serverPath,String content,String picName,String logoPath,String folder,int width,int height,boolean flag,String appInstallPath){
		File file = new File(serverPath+"/QRcodeImages/"+folder+"/");
		if (!file.exists()) {  
            if (!file.mkdir()) {  
                return null;  
            }  
        }  
		file = new File(serverPath+"/QRcodeImages/"+folder+"/"+DateUtil.getYearMonth());
		if (!file.exists()) {  
            if (!file.mkdir()) {  
                return null;  
            }  
        }  
		file = new File(serverPath+"/QRcodeImages/"+folder+"/"+DateUtil.getYearMonth()+"/"+picName);
		if(file.exists())
		{
			logger.debug("/QRcodeImages/"+folder+"/"+DateUtil.getYearMonth()+"/"+picName+"has been exist");
			return "/QRcodeImages/"+folder+"/"+DateUtil.getYearMonth()+"/"+picName;
		}
		else
		{
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			if(flag){
				QRCodeUtil.encode(appInstallPath+AESUtil.encrypt(content, AppConstants.QR_CODER_AES_STATIC_KEY), fos, logoPath,width,height);//加密
			}else{
				QRCodeUtil.encode(content, fos, logoPath,width,height);//不加密
			}
			
			//System.out.println("-----  generate code:" + AESUtil.encrypt(content, AppConstants.QR_CODER_AES_STATIC_KEY));
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/QRcodeImages/"+folder+"/"+DateUtil.getYearMonth()+"/"+picName;
		}
	}
	
	/**
	 * @param content  二维码内容
	 * @param picName  生成后二维码图片的名称
	 * @param logoPath 二维码logo 图片地址
	 * @param folder   存放二维码的文件夹
	 * @param width    二维码宽度
	 * @param height   二维码高度
	 * @param flag     是否需要加密
	 * @param appInstallPath  #red app安装地址
	 * @return
	 */
	public static String getImgpath(HttpServletRequest request,String content,String folder,String logoPath,int width,int height,String picName,boolean flag,String appInstallPath){
		String serverPath = request.getSession().getServletContext().getRealPath("/");
		String projectName = request.getContextPath()+"/";
		//System.out.println("serverPath   "+serverPath);
		String path="";
		if(logoPath==null||"".equals(logoPath)){
			path=PathUtil.getServerPath(request)+QRCodeUtil.genQrcode(serverPath.substring(0, (serverPath.length()-projectName.length())), content, picName, logoPath,folder,width,height,flag,appInstallPath);//二维码路径
		}else{
			path=PathUtil.getServerPath(request)+QRCodeUtil.genQrcode(serverPath.substring(0, (serverPath.length()-projectName.length())), content, picName, serverPath.substring(0, (serverPath.length()-projectName.length()))+logoPath,folder,width,height,flag,appInstallPath);//二维码路径
		}
		//System.out.println("path   "+path);
		return path;
	}

	public static void main(String[] args) {
//		String content = "\"book\": {"
//			      +"\"Name\": \"book.getBook_Name()\","
//			      +"\"serialno\": \"book.getBook_mgrNo()\","
//			      +"\"cover\": \"book.getBook_cover()\","
//			      +"\"status\": \"bookStat.getBDetail_status()\","
//			      +"\"borrower\": \"bookStat.getBDetail_staffname()\","
//			      +"\"staffId\": \"bookStat.getBDetail_staffNo()\","
//			      +"\"pickupdate\": \"pickupday\""
//			  +"}";
//		String content = "12345|Library201509281428201458|27|T";
//
//		File file = new File(
//				"/Users/gaosai/Desktop/QRCode/as1.png");
//		//String logoPath = "/Users/gaosai/Desktop/QRCode/download.png";
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(file);
//			QRCodeUtil.encode(AESUtil.encrypt(content, AppConstants.QR_CODER_AES_STATIC_KEY), fos, null);
//			System.out.println("-----  generate code:" + AESUtil.encrypt(content, AppConstants.QR_CODER_AES_STATIC_KEY));
//			fos.close();
//
//			FileInputStream fis = new FileInputStream(file);
//			String code = QRCodeUtil.decode(fis);
//			System.out.println("-----      scan code:" + AESUtil.decrypt(code,AppConstants.QR_CODER_AES_STATIC_KEY));
//			fis.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		String content = "1111666666666611";
		File file = new File("/Users/gaosai/Desktop/download.png");
		String logoPath = "WebRoot/QR_Code_logo.png";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			QRCodeUtil.encode(content, fos, logoPath,600,600);
			fos.close();
			System.out.println("-----  generate code:" + content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

class LogoConfiguration {
	// logo default border color
	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo default border width
	public static final int DEFAULT_BORDER = 2;
	// logo default size
	public static final int DEFAULT_LOGOPART = 5;

	private final int border = DEFAULT_BORDER;
	private final Color borderColor;
	private final int logoPart;

	/**
	 * Creates a default config with on color {@link #BLACK} and off color
	 * {@link #WHITE}, generating normal black-on-white barcodes.
	 */
	public LogoConfiguration() {
		this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
	}

	public LogoConfiguration(Color borderColor, int logoPart) {
		this.borderColor = borderColor;
		this.logoPart = logoPart;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorder() {
		return border;
	}

	public int getLogoPart() {
		return logoPart;
	}
}
