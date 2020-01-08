package org.tdds.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.simpl.SystemPropertyInstanceIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	@Autowired
	private MonitoringMapper daoMonitoring;

	@Autowired
	private Map<String, String> lostMap=new HashMap<String, String>();
	
	@Resource
	private MqttPahoMessageHandler mh;

	@Resource
	private DefaultMqttPahoClientFactory df;

	private String mqttMsg;

	@Override
	public Map<String, Object> findByName(Machine machine) {
		Map<String, Object> map = daoMonitoring.selectOneByName(machine.getName());
		try {
			 Message<String> message = MessageBuilder.withPayload(new JSONObject(map).toJSONString()) .setHeader(MqttHeaders.TOPIC, machine.getMqttSorce()).build();
			 mh.handleMessage(message);
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}
		
		return map;
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

	@SuppressWarnings("unused")
	private void subMessage(String topic, int qos) {
		try {
			String clientId = mh.getClientId();
			MqttConnectOptions optioins = df.getConnectionOptions();
			optioins.setCleanSession(true);
			MqttClient mac = df.getClientInstance(optioins.getServerURIs()[0], clientId);
			mac.connect(df.getConnectionOptions());
			mac.subscribe(topic, qos);
			mac.setCallback(new MqttCallback() {
				@SuppressWarnings("static-access")
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					if(message.getPayload()!=null) {
						String msg = new String(message.getPayload());
						if(new JSONObject().isValid(msg)) {
							mqttMsg=new String(message.getPayload());
							lostMap.put(topic, mqttMsg);
						}else {
							mqttMsg="";
						}
					}else {
						mqttMsg="";
					}
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}

				@Override
				public void connectionLost(Throwable cause) {
					MqttClient mac;
					try {
						mac = df.getClientInstance(df.getConnectionOptions().getServerURIs()[0],  mh.getClientId());
						mac.reconnect();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
			});
			System.out.print(mqttMsg);
		} catch (Exception e) {
			if(!lostMap.isEmpty() && lostMap.containsKey(topic)) {
				mqttMsg=lostMap.get(topic);
			}
			System.out.println(e.getMessage().toString());
		}

	}

	@SuppressWarnings("static-access")
	@Override
	public Map<String, Object> subscriberJsonFromMqttServer(Machine machine) {
		Map<String, Object> mls = new HashMap<>();
		mls.put("machineName", machine.getName());
		mls.put("currentTime", new Date());
		subMessage(machine.getMqttTopic(),1);
		if (!StringUtils.isEmpty(mqttMsg) && new JSONObject().isValid(mqttMsg)) {
			JSONObject jsonObject = (JSONObject) new JSONObject().parse(mqttMsg);
		if(jsonObject==null) {
			mls.put("machineSignal", "POWEROFF");
			return mls;
		}
			if(jsonObject.containsKey("limo")) {
				if(jsonObject.getInteger("limo")==0) {
					mls.put("machineSignal", "POWEROFF");
				}
				if(jsonObject.getInteger("limo")==1){
					mls.put("machineSignal", "RUNNING");
				}
				return mls;
			}
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
				if (jsonObject.getString("device_state")!=null) {
					String deviceState =jsonObject.getString("device_state");
					if(deviceState.equalsIgnoreCase("0")) {
						mls.put("machineSignal", "RUNNING");
						if(jsonObject.getString("cnc_runstatus")!=null) {
							String cncRunstatus=jsonObject.getString("cnc_runstatus");
							if(cncRunstatus.equals("0")) {
								mls.put("cncRunstatus", "RESET");
							}
							if(cncRunstatus.equals("1")) {
								mls.put("cncRunstatus", "STOP");
							}
							
							if(cncRunstatus.equals("2")) {
								mls.put("cncRunstatus", "HOLD");
							}
							
							if(cncRunstatus.equals("3")) {
								mls.put("cncRunstatus", "START");
							}
							
							if(cncRunstatus.equals("4")) {
								mls.put("cncRunstatus", "MSTR");
							}
							
							if(cncRunstatus.equals("5")) {
								mls.put("cncRunstatus", "Other");
							}
						}
					}
					if(deviceState.equalsIgnoreCase("1")) {
						mls.put("machineSignal", "POWEROFF");
					}
				}else {
					if(jsonObject.getString("cnc_runstatus")!=null) {
						mls.put("machineSignal", "RUNNING");
					}
				}

			}
		} else {
			mls.put("machineSignal", "POWEROFF");
		}
		return mls;
	}

}
