package cn.hxz.webapp.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author chenke
 * 
 */
public class ImageUtils {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	public static BufferedImage clip(byte[] src, 
			String fileSuffix, int x, int y, int width, int height){
		BufferedImage dst = null;
        try {
    		ImageInputStream imageInputStream = 
    				ImageIO.createImageInputStream(new ByteArrayInputStream(src));
            Iterator<ImageReader> iterator = 
            		ImageIO.getImageReadersBySuffix(fileSuffix); // JPG | PNG | BMP
            ImageReader imageReader = iterator.next();
			imageReader.setInput(imageInputStream, true);
			
	        ImageReadParam param = imageReader.getDefaultReadParam();
	        Rectangle rectangle = new Rectangle(x, y, width, height); // 指定截取范围
	        param.setSourceRegion(rectangle);
	        dst = imageReader.read(0, param);
		} catch (IOException e) {
			logger.debug(e.getMessage());
			if (logger.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		return dst;
	}
	
	public static BufferedImage scale(byte[] src, double scale){
		BufferedImage dst = null;
		
		try {
			ImageInputStream imageInputStream = 
					ImageIO.createImageInputStream(new ByteArrayInputStream(src));
			BufferedImage bufferedImage = ImageIO.read(imageInputStream);
			
			dst = scale(bufferedImage, scale);            
		} catch (IOException e) {
			logger.debug(e.getMessage());
			if (logger.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		
		return dst;
	}
	
	public static BufferedImage scale(BufferedImage src, double scale){
		BufferedImage dst = null;

        int width = (int)((double) src.getWidth() * scale);
        int height = (int)((double) src.getHeight() * scale);
        
        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);  
        dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        Graphics g = dst.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
        g.dispose();
		
		return dst;
	}
	
	/**
	 * 图片等比缩放
	 * 
	 * @param data
	 *            原始图片数据
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 * @throws IOException
	 */
	public static byte[] scaling(byte[] data, int width, int height, String extension) throws IOException {

		BufferedImage src = toBufferedImage(data);

		// 改变大小
		Image image = src.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		double ratio = 0; // 缩放比例
		if (src.getWidth() > width || src.getHeight() > height) {
			// 计算比例
			if (src.getHeight() > src.getWidth()) {
				ratio = (new Integer(height)).doubleValue() / src.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue() / src.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			image = op.filter(src, null);
		} else {
			image = src;
		}

		BufferedImage dst = null;
        dst = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);  
        Graphics g = dst.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
        g.dispose();
		return toByteArray(dst, extension);
	}

	/**
	 * byte数组转换为图像
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage toBufferedImage(byte[] data) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		return ImageIO.read(bis);
	}
	
	public static byte[] toByteArray(BufferedImage bufferedImage){
		return toByteArray(bufferedImage, "JPG");
	}
	
	public static byte[] toByteArray(BufferedImage bufferedImage, String fmt){
		byte[] dst = null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, fmt, outputStream);
			dst = outputStream.toByteArray();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			if (logger.isDebugEnabled()){
				e.printStackTrace();
			}
		}
		return dst;
	}

}
