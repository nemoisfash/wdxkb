package org.tdds.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.ManualRecord;
import org.tdds.entity.MonitoringList;
import org.tdds.entity.PowerOffRecord;
import org.tdds.entity.RunningRecord;
import org.tdds.entity.WaitingRecord;
import org.tdds.entity.WarningRecord;
import org.tdds.mapper.LogRecordMapper;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.ManualRecordService;
import org.tdds.service.PowerOffRecordService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WaitingRecordService;
import org.tdds.service.WarningRecordService;


@Service
public class LogRecordServiceImpl implements LogRecordService {

	private static final String[] STATUS = {"RUNNING", "POWEROFF", "ALARM", "WAITING","MANUAL"};
	
	@Autowired
	private LogRecordMapper daoLogRecord;
	
	@Autowired
	private WaitingRecordService bizWaiting;
	
	@Autowired
	private PowerOffRecordService bizPowerOff;
	
	@Autowired
	private ManualRecordService bizManualRecord;
	
	@Autowired
	private WarningRecordService bizWarning;
	
	@Autowired
	private RunningRecordService bizRunning;
	
	
	@Autowired
	private MachineService bizMachine;
	/**
	 * 西部大森 manual =running
	 * 
	 */
	public void insert(MonitoringList ml) {
		Long mid = getMachineIdByName(ml.getMachineName());
		if(StringUtils.isNotBlank(ml.getMachineSignal()) && ml.getMachineSignal().equalsIgnoreCase(STATUS[0])){
			RunningRecord rr = new RunningRecord();
				rr.setMachineId(mid);
				rr.setMachineName(ml.getMachineName());
				rr.setRecordTime(new Date());
				rr.setAlarmMessage(ml.getAlarmMessage());
				rr.setAlarmNo(ml.getAlarmNo());
				rr.setMachineMode(ml.getMachineMode());
				rr.setMachineStatus(ml.getMachineStatus());
				rr.setMachiningTimeProgress(ml.getMachiningTimeProgress());
				rr.setMainProgramNo(ml.getMainprogramNo());
				rr.setMaintenanceSignal(ml.getMaintenanceSignal());
				rr.setOverrideFeed(ml.getOverrideFeed());
				rr.setOverrideRapid(ml.getOverrideRapid());
				rr.setOverrideSpindle(ml.getOverrideSpindle());
				rr.setPartsCountResult(ml.getPartscountResult());
				rr.setPartsCountTarget(ml.getPartscountTarget());
				rr.setSpindleMode(ml.getSpindleMode());
			bizRunning.insert(rr);
		}else if(StringUtils.isNotBlank(ml.getMachineSignal()) && ml.getMachineSignal().equalsIgnoreCase(STATUS[1])){
			PowerOffRecord pr= new PowerOffRecord();
				pr.setMachineId(mid);
				pr.setMachineName(ml.getMachineName());
				pr.setAlarmMessage(ml.getAlarmMessage());
				pr.setAlarmNo(ml.getAlarmNo());
				pr.setMachineMode(ml.getMachineMode());
				pr.setMachineStatus(ml.getMachineStatus());
				pr.setMachiningTimeProgress(ml.getMachiningTimeProgress());
				pr.setMainProgramNo(ml.getMainprogramNo());
				pr.setMaintenanceSignal(ml.getMaintenanceSignal());
				pr.setOverrideFeed(ml.getOverrideFeed());
				pr.setOverrideRapid(ml.getOverrideRapid());
				pr.setOverrideSpindle(ml.getOverrideSpindle());
				pr.setPartsCountResult(ml.getPartscountResult());
				pr.setPartsCountTarget(ml.getPartscountTarget());
				pr.setSpindleMode(ml.getSpindleMode());
			bizPowerOff.insert(pr);
		}else if(StringUtils.isNotBlank(ml.getMachineSignal()) && ml.getMachineSignal().equalsIgnoreCase(STATUS[2])) {
			WarningRecord warningRecord = new WarningRecord();
				warningRecord.setMachineId(mid);
				warningRecord.setMachineName(ml.getMachineName());
				warningRecord.setRecordTime(new Date());
				warningRecord.setAlarmMessage(ml.getAlarmMessage());
				warningRecord.setAlarmNo(ml.getAlarmNo());
				warningRecord.setMachineMode(ml.getMachineMode());
				warningRecord.setMachineStatus(ml.getMachineStatus());
				warningRecord.setMachiningTimeProgress(ml.getMachiningTimeProgress());
				warningRecord.setMainProgramNo(ml.getMainprogramNo());
				warningRecord.setMaintenanceSignal(ml.getMaintenanceSignal());
				warningRecord.setOverrideFeed(ml.getOverrideFeed());
				warningRecord.setOverrideRapid(ml.getOverrideRapid());
				warningRecord.setOverrideSpindle(ml.getOverrideSpindle());
				warningRecord.setPartsCountResult(ml.getPartscountResult());
				warningRecord.setPartsCountTarget(ml.getPartscountTarget());
				warningRecord.setSpindleMode(ml.getSpindleMode());
			bizWarning.insert(warningRecord);
		}else if(StringUtils.isNotBlank(ml.getMachineSignal()) && ml.getMachineSignal().equalsIgnoreCase(STATUS[3])){
			WaitingRecord wRecord = new WaitingRecord();
			wRecord.setMachineId(mid);
			wRecord.setMachineName(ml.getMachineName());
			wRecord.setRecordTime(new Date());
			wRecord.setAlarmMessage(ml.getAlarmMessage());
			wRecord.setAlarmNo(ml.getAlarmNo());
			wRecord.setMachineMode(ml.getMachineMode());
			wRecord.setMachineStatus(ml.getMachineStatus());
			wRecord.setMachiningTimeProgress(ml.getMachiningTimeProgress());
			wRecord.setMainProgramNo(ml.getMainprogramNo());
			wRecord.setMaintenanceSignal(ml.getMaintenanceSignal());
			wRecord.setOverrideFeed(ml.getOverrideFeed());
			wRecord.setOverrideRapid(ml.getOverrideRapid());
			wRecord.setOverrideSpindle(ml.getOverrideSpindle());
			wRecord.setPartsCountResult(ml.getPartscountResult());
			wRecord.setPartsCountTarget(ml.getPartscountTarget());
			wRecord.setSpindleMode(ml.getSpindleMode());
			bizWaiting.insert(wRecord);
		}else if(StringUtils.isNotBlank(ml.getMachineSignal()) && ml.getMachineSignal().equalsIgnoreCase(STATUS[4])){
			RunningRecord mr = new RunningRecord();
				mr.setMachineId(mid);
				mr.setMachineName(ml.getMachineName());
				mr.setAlarmMessage(ml.getAlarmMessage());
				mr.setRecordTime(new Date());
				mr.setAlarmNo(ml.getAlarmNo());
				mr.setMachineMode(ml.getMachineMode());
				mr.setMachineStatus(ml.getMachineStatus());
				mr.setMachiningTimeProgress(ml.getMachiningTimeProgress());
				mr.setMainProgramNo(ml.getMainprogramNo());
				mr.setMaintenanceSignal(ml.getMaintenanceSignal());
				mr.setOverrideFeed(ml.getOverrideFeed());
				mr.setOverrideRapid(ml.getOverrideRapid());
				mr.setOverrideSpindle(ml.getOverrideSpindle());
				mr.setPartsCountResult(ml.getPartscountResult());
				mr.setPartsCountTarget(ml.getPartscountTarget());
				bizRunning.insert(mr);
		}
		 
	}
	
	private Long getMachineIdByName(String machineName){
		return bizMachine.selectMidByName(machineName);
	}

	@Override
	public Double findRankData(Long machineId) {
		return daoLogRecord.selectRankData(machineId);
	}

	/*
	 * 西部大森 manual=running
	 * (non-Javadoc)
	 * @see org.tdds.service.LogRecordService#findData(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Double findData(String date, String status,Long id) {
		Map<String, Object> map = new HashMap<>();
		if(date!=null){
			map.put("date",date);
		}
		if(id!=null){
			map.put("machineId",id);
		}
		Double count=0.0;
		if(status.equalsIgnoreCase(STATUS[0])){
			count=daoLogRecord.findRunningData(map);
		}else if(status.equalsIgnoreCase(STATUS[1])){
			count=daoLogRecord.findPoweroffData(map);
		}else if(status.equalsIgnoreCase(STATUS[2])){
			count=daoLogRecord.findAlarmData(map);
		}else if(status.equalsIgnoreCase(STATUS[3])){
			count=daoLogRecord.findWaittingData(map);
		}else if(status.equalsIgnoreCase(STATUS[4])){
			count=daoLogRecord.findRunningData(map);
		}
		return count;
	}

}


