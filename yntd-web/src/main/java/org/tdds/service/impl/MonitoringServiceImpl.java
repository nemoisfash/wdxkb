package org.tdds.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.mqtt.MyMqttClient;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	@Autowired
	private MonitoringMapper daoMonitoring;

	@Override
	public Map<String, Object> findByName(Machine machine) {
		return daoMonitoring.selectOneByName(machine.getName());
	}

	@SuppressWarnings("unused")
	public void publishMonitoring(String topic, String content) {
		Long time = new Date().getTime();
		MyMqttClient mc = new MyMqttClient(time.toString(), false);
		try {
			mc.pub(topic, content, 1, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		Map<String, Object> mls = new HashMap<>();
		mls.put("machineName", machine.getName());
		mls.put("currentTime", new Date());
		Long time = new Date().getTime();
		MyMqttClient mc = new MyMqttClient(time.toString(), false);
		mc.sub(machine.getMqttTopic(), 0);
		String message = mc.content;
		if (!StringUtils.isEmpty(message) && new JSONObject().isValid(message)) {
			JSONObject jsonObject = (JSONObject) new JSONObject().parse(message);
			if (machine.getMqttSorce() == 0) {
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
		return mls;
	}

	@Override
	public void subscriberClientMessage() {
		Long time = new Date().getTime();
		MyMqttClient mc = new MyMqttClient(time.toString(), true);
		mc.sub("reportData", 0);
	}
}
