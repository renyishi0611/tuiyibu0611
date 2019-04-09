package com.none.web.QRcode.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

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

public class QRCodeUtil {
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
		return QRCodeUtil.encode(content, outputStream, ErrorCorrectionLevel.M,
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
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

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
		BufferedImage image = toBufferedImage(matrix);
		Graphics2D gs = image.createGraphics();

		// 载入logo
		File logoFile = new File(logoPath);
		BufferedImage logoImage = ImageIO.read(logoFile);
		int widthLogo = logoImage.getWidth(), heightLogo = logoImage
				.getHeight();
		int x = (image.getWidth() - widthLogo) / 2;
		int y = (image.getHeight() - heightLogo) / 2;
		gs.drawImage(logoImage, x, y, widthLogo, heightLogo, null);
		gs.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
		LogoConfiguration logoConfig = new LogoConfiguration();
		gs.setStroke(new BasicStroke(logoConfig.getBorder()));
		gs.setColor(logoConfig.getBorderColor());
		gs.drawRect(x, y, widthLogo, heightLogo);

		gs.dispose();
		logoImage.flush();
		ImageIO.write(image, format, outputStream);
	}

	/**
	 * decode
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws NotFoundException
	 */
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

	public static void main(String[] args) throws WriterException, IOException,
			NotFoundException {
		// String[] values = new String[] { "43596783", "ddd", "吴" };
		String content = "https://www1.qualityassurance.fileupload.asiapacific.hsbc.com/imageAttach/forMobileTeam/GreenPay/eas/web/eas/sca/sca.html";
		File file = new File(
				"/Users/yanjun/Documents/workspace/JavaWS/tmp/download.png");
		String logoPath = "/Users/yanjun/Documents/workspace/JavaWS/tmp/logo.png";
		FileOutputStream fos = new FileOutputStream(file);
		QRCodeUtil.encode(content, fos, logoPath);
		System.out.println("-----  generate code:" + content);
		fos.close();

		FileInputStream fis = new FileInputStream(file);
		String code = QRCodeUtil.decode(fis);
		System.out.println("-----      scan code:" + code);
		fis.close();
	}
}

class LogoConfiguration {
	// logo默认边框颜色
	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo默认边框宽度
	public static final int DEFAULT_BORDER = 2;
	// logo大小默认为照片的1/5
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
