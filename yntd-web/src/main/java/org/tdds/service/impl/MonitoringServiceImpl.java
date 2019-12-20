package org.tdds.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.DateUtils;
import cn.hxz.webapp.util.MyMqttClient;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	private static final String[] TYPE = { "overrideRapid", "overrideSpindle", "overrideFeed" };

	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING", "MANUAL" };

	@Autowired
	private MonitoringMapper daoMonitoring;

	@Override
	public Map<String, Object> findByName(String name) {
		Map<String, Object> map = daoMonitoring.selectOneByName(name);
		if (map != null && !map.isEmpty()) {
			JSONObject json = new JSONObject();
			json.put("currentTime", DateUtils.DateToString(new Date() , "HH:mm:ss"));
			json.put("cncProducts", map.get("partscountResult"));
			json.put("cncActfspeed",0);
			json.put("cncActspeed", 0);
			json.put("overrideFeed", map.get("overrideFeed"));
			json.put("cncSrate", map.get("overrideSpindle"));
			json.put("cncSload", 0);
			json.put("cncToolnum", map.get("toolNo"));
			json.put("cncSeq", map.get("mainprogramNo"));
			json.put("cncCurrentPna", map.get("mainprogramComment"));
			json.put("machineSignal",map.get("machineSignal"));
			json.put("machineName",map.get("machineName"));
			String clientId = Objects.toString(map.get("machineName"),null) ;
			String topic = "iot/" + clientId;
			MyMqttClient.publish(topic, clientId, json.toJSONString());
		}
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

	@SuppressWarnings("static-access")
	@Override
	public Map<String, Object> subscriberJsonFromMqttServer(Machine machine) {
		String clientId = machine.getCode();
		String topic = "iot/" + clientId;
		String message = "";
		Map<String, Object> mls = new HashMap();
		try {
			message = MyMqttClient.subscribe(topic, clientId);
			mls.put("machineName", machine.getName());
			if (!StringUtils.isEmpty(message)) {
				JSONObject jsonObject = (JSONObject) new JSONObject().parse(message);
				if (machine.getMqttSorce() == 0) {
					Date date = new Date(Long.parseLong(jsonObject.getString("ts")));
					mls.put("currentTime", DateUtils.DateToString(date, "HH:mm:ss"));
					mls.put("powerOnTime", jsonObject.getString("tk"));
					mls.put("cncProducts", jsonObject.getString("nl"));
					mls.put("cncActfspeed", jsonObject.getString("sjs"));
					mls.put("cncActspeed", jsonObject.getString("szs"));
					mls.put("overrideFeed", jsonObject.getString("os"));
					mls.put("cncSrate", jsonObject.getString("oz"));
					mls.put("cncSload", jsonObject.getString("la"));
					mls.put("cncToolnum", jsonObject.getString("dp"));
					mls.put("cncCurrentPno", jsonObject.getString("ph"));
					mls.put("cncCurrentPna", jsonObject.getString("pm"));
					mls.put("mxAxis", jsonObject.getString("mcx"));
					mls.put("myAyis", jsonObject.getString("mcy"));
					mls.put("xload", jsonObject.getString("lx"));
					mls.put("yload", jsonObject.getString("ly"));
					mls.put("axAxis", jsonObject.getString("acx"));
					mls.put("ayAyis", jsonObject.getString("acy"));
					mls.put("rxAxis", jsonObject.getString("rcx"));
					mls.put("ryAyis", jsonObject.getString("rcy"));
					mls.put("currentToolOffset", jsonObject.getString("dz"));
					mls.put("cncGcode", jsonObject.getString("gt"));
					mls.put("cncAlarm", jsonObject.getString("as"));
					mls.put("alamType", jsonObject.getString("al"));
					mls.put("cncSeq", jsonObject.getString("py"));
					mls.put("ramainCoordinatesX", jsonObject.getString("scx"));
					mls.put("ramainCoordinatesY", jsonObject.getString("scy"));
					mls.put("cncCurrentpro", jsonObject.getString("pn"));
					mls.put("cncRunstatus", jsonObject.getString("co2"));
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

				} else if (machine.getMqttSorce() == 1) {
					mls.put("currentTime", new Date());
					if (!StringUtils.isEmpty(jsonObject.getString("cnc_products"))) {
						mls.put("cncProducts", jsonObject.getString("cnc_products"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_actfspeed"))) {
						mls.put("cncActfspeed", jsonObject.getString("cnc_actfspeed"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_actspeed"))) {
						mls.put("cncActspeed", jsonObject.getString("cnc_actspeed"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_srate"))) {
						mls.put("cncSrate", jsonObject.getString("cnc_srate"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_sload"))) {
						mls.put("cncSload", jsonObject.getString("cnc_sload"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_seq"))) {
						mls.put("cncSeq", jsonObject.getString("cnc_seq"));
					}

					if (!StringUtils.isEmpty(jsonObject.getString("cnc_tool_num"))) {
						mls.put("cncToolNum", jsonObject.getString("cnc_tool_num"));
					}

					if (jsonObject.getJSONArray("cnc_alarm") != null) {
						JSONArray ja = jsonObject.getJSONArray("cnc_alarm");
						if (!ja.isEmpty()) {
							mls.put("machineSignal", "ALARM");
							mls.put("alarmCount", ja.size());
						}
					}
					if (jsonObject.getJSONObject("device_state") != null) {
						JSONObject jo = jsonObject.getJSONObject("device_state");
						if (jo.getInteger("Online") == 1) {
							if (jsonObject.getInteger("cncSload") == 0) {
								mls.put("machineSignal", "WAITING");
							} else {
								mls.put("machineSignal", "RUNNING");
							}
						}
						if (jo.getInteger("Offline") == 1) {
							mls.put("machineSignal", "POWEROFF");
						}
					}

				}
			} else {
				mls.put("machineSignal", "POWEROFF");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mls;
	}

}
