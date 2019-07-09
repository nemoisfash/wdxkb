package org.tdds.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.tdds.entity.MonitoringList;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.MonitoringService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WarningRecordService;

import com.alibaba.fastjson.JSONObject;

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
	
	private static List<Map<String, Object>> timeLineData = new ArrayList<>();
	
	// 西部大森manual=running
	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING" ,"MANUAL"};
	
	private static final String[] COLOR = {"#12b07b","#a6a5a5","#e65a65","#feb501","#feb501"};

	List<Map<String, Object>> statuslist = new ArrayList<>();

	@SuppressWarnings("unused")
	@RequestMapping(value = "datalist", method = RequestMethod.GET)
	public Object loging(HttpServletRequest request, HttpServletResponse res) {
		Boolean success = true;
		List<MonitoringList> entities = bizMonitoring.findAll();
		for (MonitoringList monitoringList : entities) {
			 Machine entity = bizMachine.findMachineByName(monitoringList.getMachineName());
			 if(entity == null){
				 bizMachine.insert(monitoringList);
			 }else{
				 bizMachine.update(monitoringList, entity);
			 }
			 monitoringList.setMachineName(entity.getCode());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("resault", entities);
		return map;
	}
	
	@RequestMapping(value = "alermMessage", method = RequestMethod.GET)
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		List<String> days = getMonthDate(time);
		List<Map<String, Object>> maps = new ArrayList<>();
		for (String status : STATUS) {
			Map<String, Object> entity = new HashMap<>();
			List<Object> value = new LinkedList<>();
			for (String date : days) {
				Double num = bizLogRecord.findData(date, status, null);
				value.add(num);
			}
			entity.put("data", value);
			entity.put("name", StatusEnum.getValue(status));
			entity.put("type", "line");
			maps.add(entity);
		}
		map.put("xAxis", days);
		map.put("series", maps);
		return map;
	}

	private List<String> getMonthDate(String time) {
		List<String> days = new LinkedList<>();
		String strs[] = time.split("-");
		Calendar calendar = Calendar.getInstance();
		int year = Integer.parseInt(strs[0]);
		int month = Integer.parseInt(strs[1]) - 1;
		calendar.set(year, month, 1);
		int maxDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int j = 1; j <= maxDay; j++) {
			calendar.set(year, month, j);
			time = sdf.format(calendar.getTime());
			days.add(time);
		}
		return days;
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
				Map<String, Object> entity = new HashMap<>();
				Double num = bizLogRecord.findData(null, status, machine.getId());
				entity.put("value", num);
				entity.put("name", StatusEnum.getValue(status));
				entities.add(new JSONObject(entity));
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

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */

	@RequestMapping(value = "timeLine", method = RequestMethod.GET)
	@ResponseBody
	public Object timer(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		List<Machine> machines = bizMachine.findMachine();
		List<String> names = new ArrayList<>();
		for (Machine machine : machines) {
			names.add(machine.getName());
		}
		Map<String, Object> resault = new HashMap<>();
		resault.put("categories", names);
		resault.put("data", createData());
		return resault;
	}
	
	@RequestMapping(value = "clearTimeLineData", method = RequestMethod.GET)
	public void timer(){
		timeLineData.clear();
	}

	/**
	 * 
	 * itemStyle:{ normal:{color:'#72b362'} }
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	private List<Map<String, Object>> createData() throws ParseException {
		List<Machine> machines = bizMachine.findMachine();
		int i = 0;
		for (Machine machine : machines) {
			Map<String, Object> map2 = new HashMap<>();
			List<Object> value = new ArrayList<>();
			value.add(i);
			map2.put("name",StatusEnum.getValue(machine.getStatus()));
			value.add(DateUtils.DateToString(machine.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
			value.add(DateUtils.DateToString(machine.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
			long timeDiff=0;
			String color=null;
			if(machine.getStatus().equals(STATUS[0])){
				color=COLOR[0];
				timeDiff=machine.getrTimes();
			}else if(machine.getStatus().equals(STATUS[1])){
				color=COLOR[1];
				timeDiff=machine.getpTimes();
			}else if(machine.getStatus().equals(STATUS[2])){
				color=COLOR[2];
				timeDiff=machine.getaTimes();
			}else if(machine.getStatus().equals(STATUS[3])){
				color=COLOR[3];
				timeDiff=machine.getwTimes();
			}else{
				color=COLOR[4];
				timeDiff=machine.getwTimes();
			}
			value.add(Math.abs(timeDiff));
			map2.put("value", value);
			Map<String, Object> normalMap =new HashMap<>();
			normalMap.put("color",color);
			map2.put("itemStyle",normalMap);
			timeLineData.add(map2);
			i++;
		}
		return timeLineData;
	}

	/**
	 * @param running:运行
	 * @param waitting:等待
	 * @param warning:报警
	 *            status=1:停机 status=0:运行
	 * @param ip:设备ip
	 * @param port:端口号
	 * @return io 设备采集数据状态
	 */
	private String getStatus(String ip) {
		String status = "RUNNING";
		Boolean running = true;
		Boolean waitting = true;
		Boolean warning = true;
		try {
			running = Modbus4jUtil.readInputStatus(ip, 502, 1, 0);
			waitting = Modbus4jUtil.readInputStatus(ip, 502, 1, 1);
			warning = Modbus4jUtil.readInputStatus(ip, 502, 1, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (running) {
			if (waitting) {
				status = "WAITING";
			} else if (warning) {
				status = "MANUAL";
			}

		} else if (running == null) {
			status = "POWEROFF";
		}

		return status;
	}

}
