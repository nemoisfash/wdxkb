package org.tdds.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tdds.entity.Machine;
import org.tdds.entity.ManualRecord;
import org.tdds.entity.MonitoringList;
import org.tdds.entity.PowerOffRecord;
import org.tdds.entity.RunningRecord;
import org.tdds.entity.WaitingRecord;
import org.tdds.entity.WarningRecord;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.ManualRecordService;
import org.tdds.service.MonitoringService;
import org.tdds.service.PowerOffRecordService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WaitingRecordService;
import org.tdds.service.WarningRecordService;

import com.alibaba.fastjson.JSONObject;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.ExcelExportUtils;
import cn.hxz.webapp.util.echarts.StatusEnum;
import cn.hxz.webapp.util.modbus.Modbus4jUtil;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/logging")
public class LoggingAdminController extends BaseWorkbenchController {
	@Autowired
	private LogRecordService bizLogRecord;

	@Autowired
	private MonitoringService bizMonitoring;

	@Autowired
	private RunningRecordService bizRunning;

	@Autowired
	private PowerOffRecordService bizPowerOff;

	@Autowired
	private WarningRecordService bizWarning;

	@Autowired
	private WaitingRecordService bizWaiting;

	@Autowired
	private ManualRecordService bizManual;

	@Autowired
	private MachineService bizMachine;
	
	// 西部大森 running=manual
	private static final String[] STATUS = { "RUNNING", "POWEROFF", "ALARM", "WAITING" ,"MANUAL"};
	
	private static final String[] TYPE = { "overrideRapid", "overrideSpindle", "overrideFeed"};
	
	private static final String[] TYPE_CN = { "进给倍率", "主轴倍率", "切削倍率"};
	 
	private static final String uuid = HashUtils.MD5(LoggingAdminController.class.getName());

	@RequestMapping(value = "/{type}/list", method = RequestMethod.GET)
	public String list(@PathVariable String type, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid + type);
		model.addAttribute("filters", filters);
		model.addAttribute("type", type);
		return this.view("/tdds/logging/" + type + "/list");
	}

	@RequestMapping(value = "/{type}/data", method = RequestMethod.GET)
	@ResponseBody
	public Object data(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid + type);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		Map<String, Object> map = new HashMap<>();

		if (type.equalsIgnoreCase(STATUS[0].toLowerCase())) {
			Page<RunningRecord> entities = bizRunning.findAllRecords(filters, pageable);
			map.put("entities", entities);
		} else if (type.equalsIgnoreCase(STATUS[1].toLowerCase())) {
			Page<PowerOffRecord> entities = bizPowerOff.findAllRecords(filters, pageable);
			map.put("entities", entities);
		} else if (type.equalsIgnoreCase(STATUS[2].toLowerCase())) {
			Page<WarningRecord> entities = bizWarning.findAllRecords(filters, pageable);
			map.put("entities", entities);
		} else if (type.equalsIgnoreCase(STATUS[3].toLowerCase())) {
			Page<WaitingRecord> entities = bizWaiting.findAllRecords(filters, pageable);
			map.put("entities", entities);
		} else {
			Page<ManualRecord> manualRecords = bizManual.findAllRecords(filters, pageable);
			map.put("manualRecords", manualRecords);
		}
		map.put("number", pageable.getPageNumber());
		return map;
	}

	@RequestMapping(value = "/pie", method = RequestMethod.GET)
	@ResponseBody
	public Object pie(@RequestParam(value = "id", required = false) Long id, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> entities = new LinkedList<>();
		Machine machine = bizMachine.load(id);
		for (String status : STATUS) {
			Map<String, Object> entity = new HashMap<>();
			Double num = bizLogRecord.findData(null, status, machine.getId());
			entity.put("value", num);
			entity.put("name", StatusEnum.getValue(status));
			entities.add(entity);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("resault", entities);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/gauge", method = RequestMethod.GET)
	public Object gauge(@RequestParam(value = "machineName") String machineName, HttpServletRequest request,
			HttpServletResponse response) {
		 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(); 
		 MonitoringList entity = bizMonitoring.findByName(machineName);
		for (String type : TYPE) {
			Map<String, Object> map = new LinkedHashMap<>();
			if (type.equalsIgnoreCase(TYPE[0])) {
				if(entity!=null) {
					map.put("value",Integer.parseInt(entity.getOverrideRapid()));
				}else {
					map.put("value",0);
				}
				map.put("name",TYPE_CN[0]);
			}else if(type.equalsIgnoreCase(TYPE[1])){
				if(entity!=null) {
					map.put("value",Integer.parseInt(entity.getOverrideSpindle()));
				}else {
					map.put("value",0);
				}
				map.put("name",TYPE_CN[1]);
			}else {
				if(entity!=null) {
					map.put("value",Integer.parseInt(entity.getOverrideFeed()));
				}else {
					map.put("value",0);
				}
				map.put("name",TYPE_CN[2]);
			}
			JSONObject jsonObj=new JSONObject(map);
			list.add(jsonObj);
		}
		return list;
	}
	
	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	@ResponseBody
	public Object monitor(@RequestParam(value = "name", required = false) String name, HttpServletRequest request,
			HttpServletResponse response) {
		Machine machine = bizMachine.findMachineByName(name);
		MonitoringList montior =null;
		if(!machine.getIo()) {
			montior=bizMonitoring.findByName(name);
			montior.setMachineSignal(StatusEnum.getValue(montior.getMachineSignal()));
		}else {
			montior =new MonitoringList();
			String status=getStatus(machine.getmIp());
			montior.setMachineStatus(StatusEnum.getValue(status));
		}
		return montior;
	}
	
	@RequestMapping(value = "/{type}/sumTimeDiff", method = RequestMethod.GET)
	@ResponseBody
	public Object sumTimeDiff(@PathVariable String type, HttpServletRequest request,
			HttpServletResponse response) {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid + type);
		Double num =0.0;
		if(type.equalsIgnoreCase(STATUS[0])) {
			num=bizRunning.findTimeDiffByFilters(filters);
		}else if(type.equalsIgnoreCase(STATUS[1])) {
			num=bizPowerOff.findTimeDiffByFilters(filters);
		}else if(type.equalsIgnoreCase(STATUS[2])) {
			num=bizWarning.findTimeDiffByFilters(filters);
		}else{
			num=bizWaiting.findTimeDiffByFilters(filters);
		}
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("timediff", num);
		return map;
	}

	@RequestMapping(value = "/line", method = RequestMethod.GET)
	@ResponseBody
	public Object line(@RequestParam(value = "id", required = false) Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		List<String> days = getMonthDate(time);
		List<Map<String, Object>> maps = new ArrayList<>();
		Machine machine=bizMachine.load(id);
		for (String status : STATUS) {
			Map<String, Object> entity = new HashMap<>();
			List<Object> value = new LinkedList<>();
			for (String str : days) {
				Double num = bizLogRecord.findData(str, status, machine.getId());
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

	@RequestMapping(value = "/{type}/exportdata", method = RequestMethod.GET)
	public void exportData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "type") String type) throws UnsupportedEncodingException {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid + type);
		String filename = null;
		List<Map<String, Object>> entities = new ArrayList<>();
		if (type.equalsIgnoreCase(STATUS[0].toLowerCase())) {
			filename = "设备运行日志";
			entities = bizRunning.exportData(filters);
		} else if (type.equalsIgnoreCase(STATUS[1].toLowerCase())) {
			filename = "设备停机日志";
			entities = bizPowerOff.exportData(filters);
		} else if (type.equalsIgnoreCase(STATUS[2].toLowerCase())) {
			filename = "设备报警日志";
			entities = bizWarning.exportData(filters);
		} else if (type.equalsIgnoreCase(STATUS[3].toLowerCase())) {
			filename = "设备等待日志";
			entities = bizWaiting.exportData(filters);
		} else {
			filename = "设备手动日志";
			entities = bizManual.exportData(filters);
		}
		int i=0;
		Map<String, Object> lastMap=new HashMap<String, Object>();
		for(Map<String, Object> map:entities) {
			String timediffString=Objects.toString(map.get("timediff"),null);
			i+=Integer.valueOf(timediffString);
		}
		lastMap.put("sumtimediff",i);
		entities.add(lastMap);
		byte[] bytes = ExcelExportUtils.createExcel(type + "_", null, entities);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.addHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(filename + ".xlsx", "utf-8"));
		try {
			OutputStream out = response.getOutputStream();
			out.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
