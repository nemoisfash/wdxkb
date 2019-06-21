package net.chenke.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

import cn.hxz.webapp.util.ImageUtils;

public class ExportMSWordTest {

	public void msword() throws Exception {
		XWPFDocument docx = create();

		header(docx);
		cover(docx);

		XWPFParagraph paragraph = null;
		XWPFRun run = null;
		
		paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();

		String image1File = "D:\\chenke\\test\\image1.png";
		String image2File = "D:\\chenke\\test\\image2.png";

		try {
			InputStream inputStream = new FileInputStream(image1File);
			addScalePicture(docx, inputStream);
			inputStream.close();
			inputStream = new FileInputStream(image2File);
			addScalePicture(docx, inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		FileOutputStream out = new FileOutputStream("D:\\chenke\\test\\OOXML.docx");
		docx.write(out);
		out.close();

	}
	
	private static void addScalePicture(XWPFDocument docx, InputStream inputStream){
		XWPFParagraph paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = paragraph.createRun();
		
		XWPFPicture picture = null;
		String blipID = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ImageUtils.toByteArray(bufferedImage, "PNG"));
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();	            
			double scale = 430 / (double)width;
			
			picture = run.addPicture(byteArrayInputStream, XWPFDocument.PICTURE_TYPE_PNG, null, Units.toEMU(430), Units.toEMU(height*scale));
			
			blipID = paragraph.getDocument().addPictureData(picture.getPictureData().getData(), XWPFDocument.PICTURE_TYPE_PNG);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		picture.getCTPicture().getBlipFill().getBlip().setEmbed(blipID);
	}
	
	public void addCustomStyle(XWPFDocument docx, XWPFStyles styles, 
			String strStyleId, String fontName, int pointSize, STStyleType.Enum styleType) {

		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);
		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);

		CTOnOff onoffnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onoffnull);

		// style shows up in the formats bar
		ctStyle.setQFormat(onoffnull);

		XWPFStyle style = new XWPFStyle(ctStyle);

		CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
		size.setVal(new BigInteger(String.valueOf(pointSize*2)));

		CTFonts fonts = CTFonts.Factory.newInstance();
		fonts.setAscii(fontName);
		fonts.setEastAsia(fontName);

		CTRPr rpr = CTRPr.Factory.newInstance();
		rpr.setRFonts(fonts);
		rpr.setSz(size);
		
		style.getCTStyle().setRPr(rpr);
		style.setType(styleType);
		
		styles.addStyle(style);

	}

	private static void addCustomHeadingStyle(XWPFDocument docx, XWPFStyles styles, String strStyleId,
			int headingLevel, int pointSize, String hexColor) {

		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);

		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);

		CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
		indentNumber.setVal(BigInteger.valueOf(headingLevel));

		// lower number > style is more prominent in the formats bar
		ctStyle.setUiPriority(indentNumber);

		CTOnOff onoffnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onoffnull);

		// style shows up in the formats bar
		ctStyle.setQFormat(onoffnull);

		// style defines a heading of the given level
		CTPPr ppr = CTPPr.Factory.newInstance();
		ppr.setOutlineLvl(indentNumber);
		ctStyle.setPPr(ppr);

		XWPFStyle style = new XWPFStyle(ctStyle);

		CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
		size.setVal(new BigInteger(String.valueOf(pointSize)));
		CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
		size2.setVal(new BigInteger("24"));

		CTFonts fonts = CTFonts.Factory.newInstance();
		fonts.setAscii("Loma");

		CTRPr rpr = CTRPr.Factory.newInstance();
		rpr.setRFonts(fonts);
		rpr.setSz(size);
		rpr.setSzCs(size2);

		CTColor color = CTColor.Factory.newInstance();
		color.setVal(hexToBytes(hexColor));
		rpr.setColor(color);
		style.getCTStyle().setRPr(rpr);

		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);

	}

	public static byte[] hexToBytes(String hexString) {
		HexBinaryAdapter adapter = new HexBinaryAdapter();
		byte[] bytes = adapter.unmarshal(hexString);
		return bytes;
	}

	public XWPFDocument create() {
		XWPFDocument docx = new XWPFDocument();

		// is a null op if already defined
		XWPFStyles styles = docx.createStyles();
		if (styles == null)
			styles = docx.getStyles();

		addCustomStyle(docx, styles, "x_text", "仿宋", 14, STStyleType.PARAGRAPH);

		return docx;
	}

	public void header(XWPFDocument docx) throws IOException, XmlException {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;

		// create header start
		String headerFile = "D:\\chenke\\test\\header.png";
		CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docx, sectPr);

		XWPFHeader header = null;
		try {
			header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		paragraph = header.getParagraphArray(0);
		run = paragraph.createRun();

		// InputStream inputStream = new ByteArrayInputStream(byte[] buff);
		InputStream inputStream = null;
		XWPFPicture picture = null;
		try {
			inputStream = new FileInputStream(headerFile);
			picture = run.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, headerFile, Units.toEMU(164),
					Units.toEMU(36));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String blipID = "";
		for (XWPFPictureData picturedata : header.getAllPackagePictures()) {
			blipID = header.getRelationId(picturedata);
		}
		picture.getCTPicture().getBlipFill().getBlip().setEmbed(blipID);
	}

	public void cover(XWPFDocument docx) {
		XWPFParagraph paragraph = null;
		XWPFRun run = null;

		for (int i = 0; i < 2; i++) {
			docx.createParagraph().setStyle("x_text");
		}

		paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily("黑体");
		run.setFontSize(30);
		run.setText("中阿技术转移信息服务平台");

		paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily("黑体");
		run.setFontSize(34);
		run.setText("数据统计报告");

		for (int i = 0; i < 24; i++) {
			docx.createParagraph().setStyle("x_text");
		}

		paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily("黑体");
		run.setFontSize(16);
		run.setText("中国-阿拉伯国家技术转移中心");

		paragraph = docx.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		run = paragraph.createRun();
		run.setFontFamily("黑体");
		run.setFontSize(16);
		run.setText(new SimpleDateFormat("yyyy年MM月dd").format(new Date()));

		docx.createParagraph().createRun().addBreak(BreakType.PAGE);
	}

	public static void main(String[] args) throws Exception {
		ExportMSWordTest export = new ExportMSWordTest();
		export.msword();
		System.out.println("OK");
	}
}
