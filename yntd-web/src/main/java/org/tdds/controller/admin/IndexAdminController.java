package org.tdds.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
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

import cn.hxz.webapp.util.echarts.StatusEnum;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/index")
public class IndexAdminController {
	
	//西部大森manual=running
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
			Double num= bizLogRecord.findData(null,status,null);
			entity.put(status, num);
		}
		return entity;
	}
	
	@RequestMapping(value = "/monitoring", method = RequestMethod.GET)
	@ResponseBody
	public Object warningRecord(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map =new HashMap<>();
		List<Machine> machines = bizMachine.findMachine();
		List<MonitoringList> entities = new ArrayList<>();
		for (Machine machine : machines) {
				MonitoringList monitoringList = bizMonitoring.findByName(machine.getName());
				entities.add(monitoringList);
		}
		map.put("resault", entities);
		map.put("count", entities);
		return map;
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
				names.add(machine.getName());
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
			map.put("stack","总量");
			series.add(map);
		}
		return series;
	}
	
}
