package cn.hxz.webapp.util.echarts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;

/**
 * 
 * 更据配置文件创建echrts图表
 * @author 李靖
 *
 */
public class EchartsCreater {

	private static final String CONFIG_FILE = "echarts/echarts-build.properties";

	public static List<Map<String, Object>> LINE_SERIES_DATA = new ArrayList<>();

	public static List<String> LINE_XAXIS_DATA = new ArrayList<>();

	public static List<String> PIE_XAXIS_DATA = new ArrayList<>();
	
	public static List<Map<String, Object>> PIE_ENTITIES = new ArrayList<>();
	
	public static List<String> BAR_XAXIS_DATA = new ArrayList<>();
	
	public static List<String> BAR_YAXIS_DATA = new ArrayList<>();
	
	public static List<Map<String, Object>> BAR_SERIES_DATA = new ArrayList<>();

	/**
	 * 
	 * 根据key 获取配置文件里面相应的值
	 * 
	 * @param key
	 * @return
	 */
	private static String getValue(String key) {
		Properties pro = new Properties();
		InputStream in = EchartsCreater.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
		try {
			pro.load(new InputStreamReader(in, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return pro.getProperty(key);

	}

	/**
	 * 
	 * 创建折线图
	 * 
	 * @return
	 */
	public static Map<String, Object> buildLine() {
		Map<String, Object> map = new HashMap<>();
		map.put("legendData", createLineLegend());
		map.put("xAxisData", LINE_XAXIS_DATA);
		map.put("series", createLineSeries());
		return map;
	}

	/**
	 * 
	 * 创建折线图Legend
	 * 
	 * @return
	 */
	private static Map<String, Object> createLineLegend() {
		Map<String, Object> legend = new HashMap<>();
		legend.put("data", getValue(EchartsLineOptions.LINE_LEGEND_CN).split(","));
		return legend;
	}
	/**
	 * 创建折线图series name:'邮件营销', type:'line', stack: '总量',
	 */
	@ResponseBody
	private static Object createLineSeries() {
		String[] legendEn = getValue(EchartsLineOptions.LINE_LEGEND_EN).split(",");
		String[] legendCn = getValue(EchartsLineOptions.LINE_LEGEND_CN).split(",");
		List<Map<String, Object>> seriesData = new ArrayList<>();
		for (int i = 0; i < legendEn.length; i++) {
			Map<String, Object> series = new HashMap<>();
			series.put("name",legendCn[i]);
			series.put("type","line");
			series.put("data", createLineSeriesData(legendEn[i]));
			seriesData.add(series);
		}
		return seriesData;
	}

	/**
	 * 创建series.data
	 * @param key
	 * @return seriesData
	 */
	private static List<Integer> createLineSeriesData(String key) {
		List<Integer> seriesData = new ArrayList<>();
		for (int i = 0; i < LINE_SERIES_DATA.size(); i++) {
			Map<String, Object> entity = LINE_SERIES_DATA.get(i);
			String value = String.valueOf(entity.get(key));
			seriesData.add(Integer.parseInt(value));
		}
		return seriesData;
	}

	/**
	 * 创建饼图
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> buildPie() {
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < PIE_ENTITIES.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			String name = String.valueOf(PIE_ENTITIES.get(i).get("name"));
			map.put("titleData", name);
			map.put("legendData", getValue(EchartsPieOptions.PIE_LEGEND_CN).split(","));
			map.put("seriesData", createPieSeries(PIE_ENTITIES.get(i)));
			list.add(map);
		}
		return list;
	}

	/**
	 * 
	 * 创建饼图series.data
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	private static List<Map<String, Object>> createPieSeries(Map<String, Object> entity) {
		String[] pieLegendEn = getValue(EchartsPieOptions.PIE_LEGEND_EN).split(",");
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < pieLegendEn.length; i++) {
			String key=String.valueOf(pieLegendEn[i]);
			Map<String, Object> map = new HashMap<>();
			map.put("name",StatusEnum.getValue(key));
			map.put("value",Integer.valueOf(entity.get(key).toString()));
			list.add(map);
		}
		return list;
	}

	/**
	 * 创建柱状图
	 * 
	 * @return
	 */
	public static Map<String, Object> buildBar() {
		Map<String, Object> map = new HashMap<>();
		map.put("legend",createBarLegend());
		map.put("xAxis",createBarXAxis());
		map.put("yAxisData",BAR_YAXIS_DATA );
		map.put("series",createBarSeries());
		return map;
	}
 
	@ResponseBody
	private static List<Map<String, Object>>createBarSeries() {
		String [] legendEn = getValue(EchartsBarOptions.BAR_LEGEND_EN).split(",");
		List<Map<String, Object>> seriesData = new ArrayList<>();
		for (int i = 0; i < legendEn.length; i++) {
			Map<String, Object> series = new HashMap<>();
			series.put("name", StatusEnum.getValue(legendEn[i]));
			series.put("type", getValue(EchartsBarOptions.BAR_SERIES_TYPE));
			series.put("barGap", getValue(EchartsBarOptions.BAR_SERIES_BARGAP));
			series.put("data", createBarSeriesData(legendEn[i]));
			seriesData.add(series);
		}
		return seriesData;
	}
	
	private static Object createBarSeriesData(String key) {
		List<Integer> seriesData = new ArrayList<>();
		for (int i = 0; i < BAR_SERIES_DATA.size(); i++) {
			Map<String, Object> entity = BAR_SERIES_DATA.get(i);
			String value = null;
			if(entity.get(key)!=null){
				value= String.valueOf(entity.get(key));
				seriesData.add(Integer.parseInt(value));
			}
			
		}
		return seriesData;
	}

	/**
	 * 
	 * 创建柱状图XAxis
	 * @return
	 */
	private static Object createBarXAxis() {
		Map<String, Object> xAxis = new HashMap<>();
		String flag = getValue(EchartsBarOptions.BAR_XAXIS_TYPE);
		xAxis.put("type",flag);
		if(flag!=null && flag.equalsIgnoreCase("category")){
			xAxis.put("data", BAR_XAXIS_DATA);
		}
		return xAxis;
	}

	/**
	 * 创建柱状图legend
	 * @return
	 */
	private static Map<String, Object> createBarLegend() {
		Map<String, Object> legend = new HashMap<>();
		if(getValue(EchartsBarOptions.BAR_LEGEND_CN).indexOf(",")>-1){
			legend.put("data", getValue(EchartsBarOptions.BAR_LEGEND_CN).split(","));
		}else{
			legend.put("data", getValue(EchartsBarOptions.BAR_LEGEND_CN));
		}
		return legend;
	}

}
