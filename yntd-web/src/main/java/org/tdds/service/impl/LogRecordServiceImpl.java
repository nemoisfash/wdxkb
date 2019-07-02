package org.tdds.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.service.LogRecordService;
import org.tdds.service.PowerOffRecordService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WaitingRecordService;
import org.tdds.service.WarningRecordService;


@Service
public class LogRecordServiceImpl implements LogRecordService {

	private static final String[] STATUS = {"RUNNING", "POWEROFF", "ALARM", "WAITING","MANUAL"};
	
	@Autowired
	private WaitingRecordService bizWaiting;
	
	@Autowired
	private PowerOffRecordService bizPowerOff;
 
	@Autowired
	private WarningRecordService bizWarning;
	
	@Autowired
	private RunningRecordService bizRunning;

	/*
	 * 西部大森 manual=running
	 * (non-Javadoc)
	 * @see org.tdds.service.LogRecordService#findData(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Double findData(String date, String status,String name) {
		Map<String, Object> map = new HashMap<>();
		if(date!=null){
			map.put("date",date);
		}
		if(name!=null){
			map.put("machineName",name);
		}
		Double count=0.0;
		if(status.equalsIgnoreCase(STATUS[0])){
			count=bizRunning.findRunningData(map);
		}else if(status.equalsIgnoreCase(STATUS[1])){
			count=bizPowerOff.findPoweroffData(map);
		}else if(status.equalsIgnoreCase(STATUS[2])){
			count=bizWarning.findAlarmData(map);
		}else if(status.equalsIgnoreCase(STATUS[3])){
			count=bizWaiting.findWaittingData(map);
		} 
		return count;
	}

	@Override
	public List<Map<String, Object>> findTimeLineData(Long id) {
		List<Map<String, Object>> runningRecords =bizRunning.findAllRecordsByMachineId(id);
		List<Map<String, Object>> poweroffRecords =bizPowerOff.findAllRecordsByMachineId(id);
		List<Map<String, Object>> alarmRecords =bizWarning.findAllRecordsByMachineId(id);
		List<Map<String, Object>> waitRecords =bizWaiting.findAllRecordsByMachineId(id);
		runningRecords.addAll(runningRecords);
		runningRecords.addAll(poweroffRecords);
		runningRecords.addAll(alarmRecords);
		runningRecords.addAll(waitRecords);
		return runningRecords;
		
	}

}

