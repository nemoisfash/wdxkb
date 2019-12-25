package org.tdds.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.PushCallback;
import cn.hxz.webapp.util.websocket.MyWsHandler;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	private static final String[] TYPE = { "overrideRapid", "overrideSpindle", "overrideFeed" };

	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING", "MANUAL" };

	private static final String[] topics = { "dataList", "pies", "ranking", "timeLineCategories",
			"timeLineSeriesData" };

	@Autowired
	private MonitoringMapper daoMonitoring;

	@Resource
	private MqttPahoMessageHandler mqttHandler;

	@Resource
	private DefaultMqttPahoClientFactory clientFactory;

	@Override
	public Map<String, Object> findByName(Machine machine) {
		return daoMonitoring.selectOneByName(machine.getName());
	}

	@SuppressWarnings("unused")
	public void publishMonitoring(String topic, String content) {
		Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
		mqttHandler.handleMessage(messages);
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
		String message = null;
		Map<String, Object> mls = new HashMap<>();
		mls.put("machineName", machine.getName());
		mls.put("currentTime", new Date());
		Long clientId = new Date().getTime();
		MqttClient client;
		try {
			client = clientFactory.getClientInstance(clientFactory.getConnectionOptions().getServerURIs()[0],
					clientId.toString());
			client.connect(clientFactory.getConnectionOptions());
			client.subscribe(machine.getMqttTopic(),0);
			client.setCallback(new PushCallback());
			message = PushCallback.MESSAGE;
			if (!StringUtils.isEmpty(message)) {
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
		} catch (Exception e) {
			 System.out.println(e.getMessage());
		}
		return mls;
	}

	@Override
	public void subscriberClientMessage() {
		MqttClient client;
		try {
			Long clientId = new Date().getTime();
			client = clientFactory.getClientInstance(clientFactory.getConnectionOptions().getServerURIs()[0],
					clientId.toString());
			client.connect(clientFactory.getConnectionOptions());
			client.subscribe("reportData", 0);
			client.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable cause) {

				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					byte[] data = message.getPayload();
					TextMessage tMsg = new TextMessage(new String(data));
					MyWsHandler.sendMessageToClient(tMsg);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}

			});
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
