package cn.hxz.webapp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * 
 * athor:lijing
 * last modify 2018-10-9
 */
public class ExcelExportUtils {

	private static String node;

	public static String fileName;

	private static final String row_start_write_value = "row_start_write_value";

	private static final String all_row0_height = "all_row0_height";

	private static final String all_title_row_height = "all_title_row_height";

	private static final String all_title_font_type = "all_title_font_type";

	private static final String all_title_font_size = "all_title_font_size";

	private static final String all_title_sub_font_size = "all_title_sub_font_size";

	private static final String title = "title";

	private static final String title_zh_row1_value = "title_zh_row1_value";

	private static final String title_zh_row1_cellAddress = "title_zh_row1_cellAddress";

	private static final String title_zh_row2_value = "title_zh_row2_value";

	private static final String title_zh_row2_cellAddress = "title_zh_row2_cellAddress";

	private static final String cell_has_mergedregion = "cell_has_mergedregion";

	private static final String title_zh = "title_zh";

	private static final String cell_mergedregion_address = "cell_mergedregion_address";

	private static final String confgFile = "exportutil/excel-export.properties";

	private static final String has_special_rows = "has_special_rows";

	private static final String has_special_rows_num = "has_special_rows_num";

	private static final String has_special_rows_title_zh = "has_special_rows_title_zh";

	private static XSSFWorkbook wb;

	private static XSSFSheet sheet;

	public static byte[] createExcel(String title, String time, List<Map<String, Object>> list) {
		node = title;
		if (StringUtils.isEmpty(time)) {
			setFileNameWithNoTime(title);
		} else {
			setFileName(title, time);
		}
		wb = new XSSFWorkbook();
		sheet = wb.createSheet(fileName);
		return writeData(list);
	}

	/**
	 * 写数据
	 * 
	 * @param node
	 * @param Date
	 * @param list
	 */
	private static byte[] writeData(List<Map<String, Object>> list) {
		String mergedregion = getValue(node + cell_has_mergedregion);
		if (mergedregion.equalsIgnoreCase("true")) {
			addMergedRegion();
			createTitleHasMergedRegion();
		} else if (mergedregion.equalsIgnoreCase("false")) {
			createTitleNoMergedRegion();
		}
		int rowstartnum = Integer.parseInt(getValue(node + row_start_write_value));
		String[] cloumns = getValue(node + title).split(",");
		String hasSpecialRows = getValue(node + has_special_rows);
		int rowNum = 0;
		if (hasSpecialRows != null && hasSpecialRows.equalsIgnoreCase("true")) {
			rowNum = list.size() - 1;
			createSpecialRows(list);
		} else {
			rowNum = list.size();
		}
		for (int i = 0; i < rowNum; i++) {
			Row row = sheet.createRow(rowstartnum);
			int cellstartnum = 0;
			for (int m = 0; m < cloumns.length; m++) {
				Cell cell = row.createCell(cellstartnum);
				if (!list.get(i).containsKey(cloumns[m])) {
					cell.setCellValue("");
				} else {
					cell.setCellValue(String.valueOf(list.get(i).get(cloumns[m])));
				}

				cellstartnum++;
				setContentStyle(cell);
			}
			rowstartnum++;
		}
		ByteArrayOutputStream boStream = new ByteArrayOutputStream();
		try {
			wb.write(boStream);
			boStream.flush();
			boStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = boStream.toByteArray();
		return bytes;
	}

	private static String setFileName(String title, String time) {
		String fname = getValue(title);
		if (StringUtils.indexOf(fname, ',') > -1) {
			String[] str = fname.split(",");
			fileName = str[0] + time + str[1];
		} else {
			fileName = time + fname;
		}

		return fileName;
	}

	private static void setFileNameWithNoTime(String title) {
		fileName = getValue(title);
	}

	/*
	 * 
	 * 根据key获取配置文件的value
	 */
	private static String getValue(String key) {
		Properties prop = new Properties();
		InputStream in = ExcelExportUtils.class.getClassLoader().getResourceAsStream(confgFile);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";

		}
		return prop.getProperty(key);
	}

	/*
	 * 给内容添加样式
	 */
	private static void setContentStyle(Cell cell) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setWrapText(true);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 
	 * 栏目单元格合并
	 */
	private static void addMergedRegion() {
		String[] address = getValue(node + cell_mergedregion_address).split(",");
		for (int i = 0; i < address.length; i++) {
			int rowstart = 0;
			int rowend = 0;
			int cellstart = 0;
			int cellend = 0;
			rowstart = Integer.parseInt(address[i].split("_")[0]);
			rowend = Integer.parseInt(address[i].split("_")[1]);
			cellstart = Integer.parseInt(address[i].split("_")[2]);
			cellend = Integer.parseInt(address[i].split("_")[3]);
			sheet.addMergedRegion(new CellRangeAddress(rowstart, rowend, cellstart, cellend));
		}
	}

	/**
	 * 有合并情况的栏目的相应单元格填内容
	 */
	private static void createTitleHasMergedRegion() {
		String[] titleZhRow1 = getValue(node + title_zh_row1_value).split(",");
		String[] titleZhRow2 = getValue(node + title_zh_row2_value).split(",");
		String[] titleZhRow1CellAddress = getValue(node + title_zh_row1_cellAddress).split(",");
		String[] titleZhRow2CellAddress = getValue(node + title_zh_row2_cellAddress).split(",");
		String titleRowHeight = getValue(all_title_row_height);
		createRow0Title();
		Row row1 = sheet.createRow(1);
		row1.setHeightInPoints(Integer.parseInt(titleRowHeight));
		Row row2 = sheet.createRow(2);
		row2.setHeightInPoints(Integer.parseInt(titleRowHeight));
		for (int i = 0; i < titleZhRow1CellAddress.length; i++) {
			Cell cell = row1.createCell(Integer.parseInt(titleZhRow1CellAddress[i]));
			cell.setCellValue(titleZhRow1[i]);
			setTitleStyle(cell);
		}

		for (int j = 0; j < titleZhRow2CellAddress.length; j++) {
			Cell cell = row2.createCell(Integer.parseInt(titleZhRow2CellAddress[j]));
			cell.setCellValue(titleZhRow2[j]);
			setTitleStyle(cell);
		}
	}

	/**
	 * 添加表头
	 */
	private static void createRow0Title() {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		String row0Height = getValue(all_row0_height);
		String fontSize = getValue(all_title_font_size);
		String[] cloumns = getValue(node + title).split(",");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cloumns.length - 1));
		Row row0 = sheet.createRow(0);
		row0.setHeightInPoints(Integer.parseInt(row0Height));
		Cell row0title = row0.createCell(0);
		row0title.setCellValue(fileName);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints(Short.parseShort(fontSize));
		cellStyle.setFont(font);
		row0title.setCellStyle(cellStyle);
	}

	/**
	 * 给栏目添加样式
	 * 
	 * @param cell
	 */
	private static void setTitleStyle(Cell cell) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		String fontType = getValue(all_title_font_type);
		String fontSize = getValue(all_title_sub_font_size);
		font.setFontName(fontType);
		font.setFontHeightInPoints(Short.parseShort(fontSize));
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 
	 * 栏目无合并情况添加内容
	 */
	private static void createTitleNoMergedRegion() {
		String[] titleZh = getValue(node + title_zh).split(",");
		createRow0Title();
		Row row1 = sheet.createRow(1);
		for (int i = 0; i < titleZh.length; i++) {
			Cell cell = row1.createCell(i);
			cell.setCellValue(titleZh[i]);
			setTitleStyle(cell);
		}
	}
	 
	private static void createSpecialRows(List<Map<String, Object>> list) {
		String[] specialRowsTitleZh = getValue(node + has_special_rows_title_zh).split(",");
		String specialRowsNum = getValue(node + has_special_rows_num);
		int num = Integer.parseInt(specialRowsNum);
		Row row = sheet.createRow(list.size() + num);
		Map<String, Object> map = list.get(list.size() - 1);
		for (int i = 0; i < specialRowsTitleZh.length; i++) {
			Cell cell = row.createCell(i);
			setContentStyle(cell); 
			if (map.get(specialRowsTitleZh[i]) == null) {
				cell.setCellValue(specialRowsTitleZh[i]);
			} else {
				cell.setCellValue(map.get(specialRowsTitleZh[i]).toString());
			}
		}
	}
}
