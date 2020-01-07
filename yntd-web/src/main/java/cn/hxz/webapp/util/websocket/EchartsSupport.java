package cn.hxz.webapp.util.websocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.tdds.entity.Machine;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.MonitoringService;
import org.tdds.service.RunningRecordService;

import com.alibaba.fastjson.JSONObject;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import cn.hxz.webapp.util.DateUtils;
import cn.hxz.webapp.util.echarts.StatusEnum;
import cn.hxz.webapp.util.modbus.Modbus4jUtil;

public class EchartsSupport {
	@Autowired
	private MachineService bizMachine;

	@Autowired
	private MonitoringService bizMonitoring;

	@Autowired
	private RunningRecordService bizRunningRecord;

	@Autowired
	private LogRecordService bizLogRecord;
	
	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING", "MANUAL" };

	private static final String[] COLOR = { "#12b07b", "#a6a5a5", "#e65a65", "#feb501", "#feb501" };

	private static final String[] topics = { "dataList", "pies", "ranking", "timeLineCategories","timeLineSeriesData" };
	
 	private static String[] sorces = {"MEMBER","MECHINELIST"};
 	
	public void callBackRepoortData(String flag) {
		while (true) {
			Map<String, Object> response = new HashMap<>();
			Map<String, Object> dataList = dataList();
			if (!dataList.isEmpty() && dataList.get("content") != null) {
				response.put(topics[0], dataList);
			}
	
			Map<String, Object> pies = pies();
			if (!pies.isEmpty() && pies.get("content") != null) {
				response.put(topics[1], pies);
			}
	
			Map<String, Object> ranking = ranking();
			if (!ranking.isEmpty() && ranking.get("content") != null) {
				response.put(topics[2], ranking);
			}
	
			Map<String, Object> timeLineCategories =  timeLineCategories();
			if (!timeLineCategories.isEmpty() && timeLineCategories.get("content") != null) {
				response.put(topics[3], timeLineCategories);
			}
			Map<String, Object> timeLineSeriesData =  timeLineSeriesData();
			if (!timeLineSeriesData.isEmpty() && timeLineSeriesData.get("content") != null) {
				response.put(topics[4], timeLineSeriesData);
			}
			TextMessage tMsg = new TextMessage(new JSONObject(response).toJSONString());
			MyWsHandler.sendMessageToClient(tMsg);
		}
	};

	@SuppressWarnings("unused")
	private Map<String, Object> line() {
		Map<String, Object> map = new HashMap<>();
		List<String> times = new ArrayList<>();
		List<Map<String, Object>> maps = new ArrayList<>();
		List<Machine> machines = bizMachine.findMachine();
		map.put("xAxis", DateUtils.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		map.put("series", maps);
		return map;
	};
	
	private Map<String, Object> pies() {
		List<Machine> machines = bizMachine.findMachine();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		Boolean success = true;
		String topic = "pies";
		try {
			for (Machine machine : machines) {
				List<Map<String, Object>> entities = new LinkedList<>();
				for (String status : STATUS) {
					if (!status.equalsIgnoreCase(STATUS[4])) {
						Map<String, Object> entity = new HashMap<>();
						Double num = bizLogRecord.findData(null, status, machine.getId());
						entity.put("value", num);
						entity.put("name", StatusEnum.getValue(status));
						entities.add(entity);
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("data", entities);
				map.put("machineName", machine.getName());
				list.add(map);
			}
			result.put("content", list);
		} catch (Exception e) {
			success = false;
			JSONObject erroMessage = new JSONObject();
			erroMessage.put("erroPublisTopic", topic);
			erroMessage.put("Reason", e.getMessage());
			result.put("Message", erroMessage);
		}
		result.put("success", success);
		return result;
	};
	
	private Map<String, Object> pie() {
		String data = "";
		return null;
	};
	
	private Map<String, Object> timeLineCategories() {
		Boolean success = true;
		String topic = "timeLineCategories";
		Map<String, Object> result = new HashMap<>();
		try {
			List<Machine> machines = bizMachine.findMachine();
			List<String> names = new ArrayList<>();
			for (Machine entity : machines) {
				names.add(entity.getName());
			}
			result.put("content", names);
		} catch (Exception e) {
			success = false;
			JSONObject erroMessage = new JSONObject();
			erroMessage.put("erroPublisTopic", topic);
			erroMessage.put("Reason", e.getMessage());
			result.put("Message", erroMessage);
		}
		result.put("success", success);
		return result;
	};
	
	private Map<String, Object> timeLineSeriesData() {
		String topic = "timeLineSeriesData";
		List<Machine> machines = bizMachine.findMachine();
		Map<String, Object> result = new HashMap<>();
		Boolean success = true;
		try {
			int i = 0;
			List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
			for (Machine machine : machines) {
				Map<String, Object> map2 = new HashMap<>();
				List<Object> value = new ArrayList<>();
				value.add(i);
				if (machine.getStartTime() != null) {
					value.add(DateUtils.DateToString(machine.getStartTime(), "yyyy-MM-dd HH:mm"));
				} else {
					value.add(DateUtils.DateToString(new Date(), "yyyy-MM-dd HH:mm"));
				}
				if (machine.getEndTime() != null) {
					value.add(DateUtils.DateToString(machine.getEndTime(), "yyyy-MM-dd HH:mm"));
				} else {
					value.add(DateUtils.DateToString(new Date(), "yyyy-MM-dd HH:mm"));
				}
				String color = null;
				if (machine.getStatus().equals(STATUS[0])) {
					color = COLOR[0];
				} else if (machine.getStatus().equals(STATUS[1])) {
					color = COLOR[1];
				} else if (machine.getStatus().equals(STATUS[2])) {
					color = COLOR[2];
				} else if (machine.getStatus().equals(STATUS[3])) {
					color = COLOR[3];
				} else {
					color = COLOR[4];
				}
				Long timeDiff = null;
				if (machine.getStartTime() != null && machine.getEndTime() != null) {
					timeDiff = DateUtils.getDatePoor(machine.getStartTime(), machine.getEndTime(), "min");
				} else {
					timeDiff = 1L;
				}
				value.add(Math.abs(timeDiff));
				map2.put("value", value);
				Map<String, Object> normalMap = new HashMap<>();
				normalMap.put("color", color);
				map2.put("itemStyle", normalMap);
				list.add(map2);
				i++;
			}
			result.put("content", list);
		} catch (Exception e) {
			success = false;
			JSONObject erroMessage = new JSONObject();
			erroMessage.put("erroPublisTopic", topic);
			erroMessage.put("Reason", e.getMessage());
			result.put("Message", erroMessage);
		}
		result.put("success", success);
		return result;
	};
	
	public Map<String, Object> ranking() {
		List<Machine> machines = bizMachine.findMachine();
		Map<String, Double> sortMap = new HashMap<>();
		Boolean success = true;
		String topic = "ranking";
		Map<String, Object> result = new HashMap<>();
		try {
			for (Machine machine : machines) {
				Double num = bizRunningRecord.findRankData(machine.getId());
				sortMap.put(machine.getName(), num);
			}
			List<Map.Entry<String, Double>> content = sortMap(sortMap);
			result.put("content", content);
		} catch (Exception e) {
			success = false;
			JSONObject erroMessage = new JSONObject();
			erroMessage.put("erroPublisTopic", topic);
			erroMessage.put("Reason", e.getMessage());
			result.put("Message", erroMessage);
		}
		result.put("success", success);
		return result;
	};
	
	private List<Entry<String, Double>> sortMap(Map<String, Double> map) {
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		return list;
	}

	private Map<String, Object> dataList() {
		List<Machine> machines = bizMachine.findMachine();
		List<Map<String, Object>> entities = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		Boolean success = true;
		for (Machine machine : machines) {
			Map<String, Object> monitor = new HashMap<>();
			if (machine.getIo() != null) {
				if (machine.getIo() == 0) {
					monitor = bizMonitoring.findByName(machine);
				} else if (machine.getIo() == 1) {
					monitor.put("machineName", machine.getName());
					monitor.put("machineSignal", getStatus(machine.getmIp()));
				} else if (machine.getIo() == 2) {
					monitor = bizMonitoring.subscriberJsonFromMqttServer(machine);
				}
			}
			if (monitor != null && !monitor.isEmpty()) {
				bizMachine.update(monitor, machine);
				entities.add(monitor);
			}
		}
		result.put("content", entities);
		result.put("result", success);
		return result;
	};
	
	private String getStatus(String ip) {
		String status = STATUS[0];
		Boolean running = true;
		Boolean waitting = true;
		Boolean warning = true;
		try {
			running = Modbus4jUtil.readInputStatus(ip, 502, 1, 0);
			waitting = Modbus4jUtil.readInputStatus(ip, 502, 1, 1);
			warning = Modbus4jUtil.readInputStatus(ip, 502, 1, 2);
		} catch (ModbusTransportException | ErrorResponseException | ModbusInitException e) {
			status = STATUS[1];
		}
		if (running) {
			if (!waitting) {
				status = STATUS[3];
			}
			if (warning) {
				status = STATUS[2];
			}

		} else if (running == null) {
			status = STATUS[1];
		}

		return status;
	}
}
