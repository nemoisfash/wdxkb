package org.tdds.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MachineMapper;
import org.tdds.service.MachineService;
import org.tdds.service.PowerOffRecordService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WaitingRecordService;
import org.tdds.service.WarningRecordService;

import net.chenke.playweb.QueryFilters;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import org.springframework.util.StringUtils;

@Service
public class MachineServiceImpl implements MachineService {

	private static final String[] STATUS = {"RUNNING", "POWEROFF", "ALARM", "WAITING","MANUAL"};
	
	@Autowired
	private MachineMapper machineDao;
	
	@Autowired
	private WarningRecordService bizWarningRecord;
	
	@Autowired
	private RunningRecordService bizRunningRecord;

	@Autowired
	private PowerOffRecordService bizPowerOff;
	
	@Autowired
	private WaitingRecordService bizWaitingRecord;

	@Override
	public Long selectMidByName(String machineName) {
		return machineDao.selectIdByName(machineName);
	}

	@Override
	public List<Machine> findMachine() {
		return machineDao.selectAll();
	}

	@Override
	public List<Long> findMachineids() {
		return machineDao.findMachineids();
	}

	@Override
	public String findMachineNames(Long id) {
		 
		return machineDao.findMachineName(id);
	}

	@Override
	public List<Machine> findMachines(QueryFilters filters) {
		 Example example = new Example(Machine.class);
		 Criteria criteria = example.createCriteria();
		 if(StringUtils.hasText(Objects.toString(filters.get("name"), null))){
			 criteria.andEqualTo("name", Objects.toString(filters.get("name")));
		 }else if(StringUtils.hasText(Objects.toString(filters.get("machineNo"), null))){
			 criteria.andEqualTo("machineNo", Objects.toString(filters.get("name")));
		 }else if(StringUtils.hasText(Objects.toString(filters.get("type"), null))){
			 String type =Objects.toString(filters.get("type"));
			 criteria.andEqualTo("type",Long.parseLong(type));
		 }
		 return machineDao.selectByExample(example);
	}

	@Override
	public Machine load(Long id) {
		Machine machine =new Machine();
		if(id!=null){
			machine.setId(id);
			machine=machineDao.selectOne(machine);
		}
		return machine;
	}

	@Override
	public List<Map<String, Object>> exportInfore(Long id) {
		return machineDao.exportInfore(id);
	}

	@Override
	public int update(MonitoringList monitoringList,Machine entity) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String today = fmt.format(date);
		String statusTime= fmt.format(entity.getStartTime());
		if(statusTime !=null || !today.equals(statusTime)){
			entity.setStartTime(date);
		}
		String status=entity.getStatus();
		String mstatus= monitoringList.getMachineSignal();
		if(!status.equals(mstatus)){
			 if(status.equals(STATUS[0])){
				 bizRunningRecord.insert(monitoringList, entity);
			 }else if(status.equals(STATUS[1])){
				 bizPowerOff.insert(monitoringList, entity);
			 }else if(status.equals(STATUS[2])){
				 bizWarningRecord.insert(monitoringList, entity);
			 }else if(status.equals(STATUS[3])){
				 bizWaitingRecord.insert(monitoringList, entity);
			 }else {
				 bizWaitingRecord.insert(monitoringList, entity);
			 }
				entity.setStatus(mstatus);
				entity.setStartTime(date);
				entity.setEndTime(date);
		}else{
			entity.setEndTime(date);
		}
		return machineDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public Machine findMachineByName(String machineName) {
		Machine entity = new Machine();
		entity.setName(machineName);
		return machineDao.selectOne(entity);
	}

	@Override
	public void insert(MonitoringList monitoringList) {
		Machine machine = new Machine();
		Date date=new Date();
		machine.setName(monitoringList.getMachineName());
		machine.setStatus(monitoringList.getMachineSignal());
		machine.setStartTime(date);
		machine.setEndTime(date);
		machine.setCode(monitoringList.getMachineName());
		machineDao.insertSelective(machine);
	}

	@Override
	public void updateImage(Machine machine) {
		machineDao.updateByPrimaryKeySelective(machine);
	}
}
