package org.tdds.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import cn.hxz.webapp.util.echarts.StatusEnum;
import cn.hxz.webapp.util.modbus.Modbus4jUtil;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/index")
public class IndexAdminController {
	
	private static final String[] STATUS = {"RUNNING", "POWEROFF", "ALARM", "WAITING"/*,"MANUAL"*/};
	
	@Autowired
	private LogRecordService bizLogRecord;
	
	@Autowired
	private MachineService bizMachine;
	
	@Autowired
	private MonitoringService bizMonitoring;
	
	private  static List<Map<String, Object>> series = new ArrayList<>();
	
	private  static List<String> names = new ArrayList<>();
	
	private static final String uuid = HashUtils.MD5(IndexAdminController.class.getName());
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	@ResponseBody
	public Object data(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> entity = new HashMap<>();
		for(String status:STATUS){
			Integer num= bizMachine.findStatusNum(status);
			entity.put(status, num);
		}
		return entity;
	}
	
	@RequestMapping(value = "/datalist", method = RequestMethod.GET)
	@ResponseBody
	public Object datalist(HttpServletRequest request,HttpServletResponse response){
		List<Machine> machines = bizMachine.findMachine();
		List<Map<String,Object>> monitoringLists =new ArrayList<>();
		for(Machine entity:machines){
			Map<String,Object> monitoringlist= new HashMap<>();
			if (entity.getIo() != null) {
				if (entity.getIo() == 0) {
					monitoringlist = bizMonitoring.findByName(entity);
				} else if (entity.getIo() == 1) {
					monitoringlist.put("machineName", entity.getName());
					monitoringlist.put("machineSignal", getStatus(entity.getmIp()));
				} else if (entity.getIo() == 2) {
					monitoringlist = bizMonitoring.subscriberJsonFromMqttServer(entity);
				}
			}
			monitoringLists.add(monitoringlist);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("resault", monitoringLists);
		return map;
	}
	
	@RequestMapping(value = "/monitoring", method = RequestMethod.GET)
	@ResponseBody
	public Object warningRecord(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map =new HashMap<>();
		List<MonitoringList> monitoringList = bizMonitoring.findAll();
		map.put("resault", monitoringList);
		map.put("count", monitoringList.size());
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "line", method = RequestMethod.GET)
	public Object line(HttpServletRequest request, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		List<String> days = getMonthDate(time);
		List<Object> values = new LinkedList<>();
		for (String date : days) {
			Double num = bizLogRecord.findRunningData(date);
			values.add(num);
		}
		map.put("data", values);
		map.put("xAxis", days);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for (int j = 1; j <= maxDay; j++) {
			calendar.set(year, month, j);
			time = sdf.format(calendar.getTime());
			days.add(time);
		}
		return days;
	}
	
	/*
	 *name: '直接访问',
     *type: 'bar',
     *stack: '总量',
	 * 
	 */
	@RequestMapping(value = "/bar", method = RequestMethod.GET)
	@ResponseBody
	public Object bar(HttpServletRequest request,HttpServletResponse response){
		series.clear();
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		List<Machine> machines = bizMachine.findMachines(filters);
		if(names.isEmpty()){
			for(Machine machine:machines){
				names.add(machine.getCode());
			}
		}
		
		Map<String,Object> finalmap = new HashMap<>();
		finalmap.put("series", createSeries(machines));
		finalmap.put("yAxisData", names);
		return finalmap;
	}
	
	@ResponseBody
	private Object createSeries(List<Machine> machines){
		for(String status:STATUS){
			Map<String,Object> map = new HashMap<>();
			List<Double> data= new ArrayList<>();
			for(Machine machine:machines){
				Double num= bizLogRecord.findData(null,status,machine.getId());
				data.add(num);
			}
			map.put("data",data);
			map.put("name",StatusEnum.getValue(status));
			map.put("type","bar");
			map.put("barWidth","30%");
			map.put("stack","总量");
			series.add(map);
		}
		return series;
	}
	
	private String getStatus(String ip) {
		String status = STATUS[0];
		Boolean running = true;
		Boolean waitting = true;
		Boolean warning = true;
		try {
			running = Modbus4jUtil.readInputStatus(ip, 502, 1, 0);
			waitting = Modbus4jUtil.readInputStatus(ip, 502, 1, 1);
			warning = Modbus4jUtil.readInputStatus(ip, 502, 1, 2);
		} catch (ModbusTransportException | ErrorResponseException
				| ModbusInitException e) {
			status=STATUS[1];
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
