package org.tdds.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tdds.entity.Machine;
import org.tdds.entity.Report;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.MonitoringService;
import org.tdds.service.ReportService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WarningRecordService;

import com.alibaba.fastjson.JSONObject;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import cn.hxz.webapp.syscore.support.BasePortalController;
import cn.hxz.webapp.util.DateUtils;
import cn.hxz.webapp.util.echarts.StatusEnum;
import cn.hxz.webapp.util.modbus.Modbus4jUtil;

@Controller
@RequestMapping("/member")
public class MachineController extends BasePortalController {
	@Autowired
	private MachineService bizMachine;

	@Autowired
	private MonitoringService bizMonitoring;

	@Autowired
	private WarningRecordService bizWarningRecord;

	@Autowired
	private RunningRecordService bizRunningRecord;

	@Autowired
	private LogRecordService bizLogRecord;

	@Autowired
	private ReportService bizLogReport;

	// 西部大森manual=running
	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING", "MANUAL" };

	private static final String[] COLOR = { "#12b07b", "#a6a5a5", "#e65a65", "#feb501", "#feb501" };

	List<Map<String, Object>> statuslist = new ArrayList<>();

	@ResponseBody
	@RequestMapping(value = "datalist", method = RequestMethod.GET)
	public Object loging(HttpServletRequest request, HttpServletResponse res) {
		Boolean success = true;
		List<Machine> machines = bizMachine.findMachine();
		List<Map<String, Object>> entities = new ArrayList<>();
		for (Machine machine : machines) {
			Map<String, Object> monitor = null;
			if (machine.getIo() != null) {
				if (machine.getIo() == 0) {
					monitor = bizMonitoring.findByName(machine.getCode());
				} else if (machine.getIo() == 1) {
					monitor = new HashMap();
					monitor.put("machineName", machine.getName());
					monitor.put("machineSignal", getStatus(machine.getmIp()));
				} else if (machine.getIo() == 2) {
					monitor = bizMonitoring.subscriberJsonFromMqttServer(machine);
				}
			}
			if(!monitor.isEmpty()) {
				bizMachine.update(monitor, machine);
				entities.add(monitor);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("resault", entities);
		return map;
	}

	@RequestMapping(value = "/alermMessage", method = RequestMethod.GET)
	@ResponseBody
	private Object alermMessage() {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> entities = bizWarningRecord.findAll();
		map.put("resault", entities);
		return map;
	}

	/**
	 * 每天每小时设备运行状况 一天24*60分钟
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "line", method = RequestMethod.GET)
	public Object line(HttpServletRequest request, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<>();
		List<String> times = new ArrayList<>();
		List<Map<String, Object>> maps = new ArrayList<>();
		List<Machine> machines = bizMachine.findMachine();
		for (Machine entity : machines) {

		}
		map.put("xAxis", DateUtils.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		map.put("series", maps);

		return map;
	}

	/**
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "pie", method = RequestMethod.GET)
	@ResponseBody
	public Object pie(HttpServletRequest request, HttpServletResponse res) {
		List<Machine> machines = bizMachine.findMachine();
		List<Map<String, Object>> list = new ArrayList<>();
		for (Machine machine : machines) {
			List<Map<String, Object>> entities = new LinkedList<>();
			for (String status : STATUS) {
				if (!status.equalsIgnoreCase(STATUS[4])) {
					Map<String, Object> entity = new HashMap<>();
					Double num = bizLogRecord.findData(null, status, machine.getId());
					entity.put("value", num);
					entity.put("name", StatusEnum.getValue(status));
					entities.add(new JSONObject(entity));
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("data", entities);
			map.put("machineName", machine.getName());
			list.add(map);
		}
		return list;
	}

	/**
	 * 
	 * 设备运行排名
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ranking", method = RequestMethod.GET)
	public Object ranking(HttpServletRequest request, HttpServletResponse res) {
		List<Machine> machines = bizMachine.findMachine();
		Map<String, Double> sortMap = new HashMap<>();
		for (Machine machine : machines) {
			Double num = bizRunningRecord.findRankData(machine.getId());
			sortMap.put(machine.getName(), num);
		}
		return sortMap(sortMap);
	}

	private List<Map.Entry<String, Double>> sortMap(Map<String, Double> map) {
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		return list;
	}

	@RequestMapping(value = "/timeLine/categories", method = RequestMethod.GET)
	@ResponseBody
	public Object categories(HttpServletRequest request, HttpServletResponse response) {
		List<Machine> machines = bizMachine.findMachine();
		List<String> names = new ArrayList<>();
		for (Machine entity : machines) {
			names.add(entity.getName());
		}
		return names;
	}

	@RequestMapping(value = "/reportList", method = RequestMethod.GET)
	@ResponseBody
	public Object reportList(HttpServletRequest request, HttpServletResponse response) {
		List<Report> reportsList = bizLogReport.findAll();
		List<Map<String, Object>> entities = new ArrayList<>();
		for (Report report : reportsList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("machineName", report.getMachineName());
			map.put("plannedOtime", report.getPlannedOtime() + "H");
			map.put("actualOtime", report.getActualOtime() + "H");
			map.put("timeOee", createOee(report.getPlannedOtime(), report.getActualOtime()));
			map.put("plannedCapacity", report.getPlannedCapacity());
			map.put("actualCapacity", report.getActualCapacity());
			map.put("performanceOee", createOee(report.getPlannedCapacity(), report.getActualCapacity()));
			map.put("number", report.getNumber());
			map.put("goodNumber", report.getGoodNumber());
			map.put("goodYield", createOee(report.getNumber(), report.getGoodNumber()));
			entities.add(map);
		}
		return entities;
	}

	private String createOee(int dividend, int divisor) {
		String f = null;
		if (dividend != 0) {
			if (dividend == divisor) {
				f = "100";
			} else {
				Double numDouble = (Double.valueOf(divisor) / Double.valueOf(dividend)) * 100;
				f = new DecimalFormat("#.00").format(numDouble);
			}
		} else {
			f = "0";
		}
		return f + "%";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/timeLine/seriesData", method = RequestMethod.GET)
	@ResponseBody
	public Object timer(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		List<Machine> machines = bizMachine.findMachine();
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
			list.add(new JSONObject(map2));
			i++;
		}

		return list;
	}

	/**
	 * @param running:运行
	 * @param waitting:等待
	 * @param warning:报警  status=1:停机 status=0:运行
	 * @param ip:设备ip
	 * @param port:端口号
	 * @return io 设备采集数据状态
	 */
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
