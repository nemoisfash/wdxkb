package org.tdds.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.DateUtils;
import cn.hxz.webapp.util.ExcelExportUtil;
import cn.hxz.webapp.util.MyClient;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	private static final String[] TYPE = { "overrideRapid", "overrideSpindle", "overrideFeed" };

	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING", "MANUAL" };

	@Autowired
	private MonitoringMapper daoMonitoring;

	private static final String confgFile = "mqttconfig/mqttconfig.properties";

	@Override
	public Map<String, Object> findByName(String name) {

		return daoMonitoring.selectOneByName(name);
	}

	@Override
	public List<MonitoringList> findAll() {
		return daoMonitoring.selectAll();
	}

	@Override
	public Integer findStatusNum(String status) {
		Example example = new Example(MonitoringList.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("machineSignal", status);
		return daoMonitoring.selectCountByExample(example);
	}

	@Override
	public Map<String, Object> subscriberJsonFromMqttServer(Machine machine) {
		String host = getValue("mqtt_host");
		String userName = getValue("mqtt_username");
		String pwd = getValue("mqtt_pwd");
		MyClient.HOST = host;
		MyClient.USERNAME = userName;
		MyClient.PASSWORD = pwd;
		String clientId = machine.getCode();
		String TOPIC1 = "iot/" + clientId;
		String message = "";
		Map<String, Object> mls = new HashMap();
		try {
			MyClient.start(TOPIC1, clientId);
			Thread.sleep(1000);
			message = MyClient.msg;
			mls.put("machineName", machine.getName());
			if (!StringUtils.isEmpty(message)) {
				if (machine.getMqttSorce() == 0) {
					JSONObject jsonObject = (JSONObject) new JSONObject().parse(message);
					Date date = new Date(Long.parseLong(jsonObject.getString("ts")));
					mls.put("currentTime", DateUtils.DateToString(date, "HH:mm:ss"));
					mls.put("powerOnTime", jsonObject.getString("tk"));
					mls.put("partscountResult", jsonObject.getString("nl"));
					mls.put("spindleSpeed", jsonObject.getString("sjs"));
					mls.put("overrideSpindle", jsonObject.getString("szs"));
					mls.put("overrideFeed", jsonObject.getString("os"));
					mls.put("spindleMultiplying", jsonObject.getString("oz"));
					mls.put("spindleLoad", jsonObject.getString("la"));
					mls.put("currentPregramNo", jsonObject.getString("ph"));
					mls.put("currentPregramName", jsonObject.getString("pm"));
					mls.put("mxAxis", jsonObject.getString("mcx"));
					mls.put("myAyis", jsonObject.getString("mcy"));
					mls.put("xload", jsonObject.getString("lx"));
					mls.put("yload", jsonObject.getString("ly"));
					mls.put("axAxis", jsonObject.getString("acx"));
					mls.put("ayAyis", jsonObject.getString("acy"));
					mls.put("rxAxis", jsonObject.getString("rcx"));
					mls.put("ryAyis", jsonObject.getString("rcy"));
					mls.put("currentToolNo", jsonObject.getString("dp"));
					mls.put("currentToolOffset", jsonObject.getString("dz"));
					mls.put("gModal", jsonObject.getString("gt"));
					mls.put("alarmCount", jsonObject.getString("as"));
					mls.put("alamType", jsonObject.getString("al"));
					mls.put("currentProgramStatementNo", jsonObject.getString("py"));
					mls.put("ramainCoordinatesX", jsonObject.getString("scx"));
					mls.put("ramainCoordinatesY", jsonObject.getString("scy"));
					mls.put("currentProgramContent", jsonObject.getString("pn"));
					mls.put("currentProgramContent", jsonObject.getString("co2"));
					if (jsonObject.get("co1") != null && jsonObject.getString("co1").equals("1")) {
						mls.put("processingStates", "快速移动状态");
					}

					if (jsonObject.get("co2") != null && jsonObject.getString("co2").equals("1")) {
						mls.put("processingStates", "直线切削状态");
					}

					if (jsonObject.get("co3") != null && jsonObject.getString("co3").equals("1")) {
						mls.put("processingStates", "顺时针圆弧状态");
					}

					if (jsonObject.get("co4") != null && jsonObject.getString("co4").equals("1")) {
						mls.put("processingStates", "逆时针圆弧状态");
					}

					if (jsonObject.getString("rz") != null && jsonObject.getString("rz").equals("1")) {
						mls.put("machineSignal", "RUNNING");
					}

					if (jsonObject.getString("wz") != null && jsonObject.getString("wz").equals("1")) {
						mls.put("machineSignal", "WAITING");
					}

					if (jsonObject.getString("az") != null && jsonObject.getString("az").equals("1")) {
						mls.put("machineSignal", "ALARM");
					}
				}
			} 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mls;
	}

	private static String getValue(String key) {
		Properties prop = new Properties();
		InputStream in = ExcelExportUtil.class.getClassLoader().getResourceAsStream(confgFile);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
		return prop.getProperty(key);
	}

}
