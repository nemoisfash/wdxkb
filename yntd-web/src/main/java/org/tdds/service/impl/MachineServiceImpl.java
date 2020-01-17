package org.tdds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MachineMapper;
import org.tdds.service.MachineService;
import org.tdds.service.PowerOffRecordService;
import org.tdds.service.RunningRecordService;
import org.tdds.service.WaitingRecordService;
import org.tdds.service.WarningRecordService;

import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.DateUtils;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
	public Page<Machine> findMachines(QueryFilters filters,PageRequest pageable) {
		 Example example = new Example(Machine.class);
		 Criteria criteria = example.createCriteria();
		 if(StringUtils.hasText(Objects.toString(filters.get("name"), null))){
			 criteria.andEqualTo("name", Objects.toString(filters.get("name")));
		 }else if(StringUtils.hasText(Objects.toString(filters.get("code"), null))){
			 criteria.andEqualTo("code", Objects.toString(filters.get("name")));
		 }else if(StringUtils.hasText(Objects.toString(filters.get("type"), null))){
			 String type =Objects.toString(filters.get("type"));
			 criteria.andEqualTo("type",Long.parseLong(type));
		 }else if(StringUtils.hasText(Objects.toString(filters.get("io"), null))){
			 String io =Objects.toString(filters.get("io"));
			 criteria.andEqualTo("io",Integer.valueOf(io));
		 }
		 List<Machine> entities = machineDao.selectByExampleAndRowBounds(example, pageable);
		 return new PageImpl<Machine>(entities, pageable);
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

	private int update(JSONObject monitoringList,Machine entity) {
		Date date = new Date();
		String status=entity.getStatus();
		String mstatus= monitoringList.getString("machineSignal");
		if(StringUtils.isEmpty(mstatus)){
			mstatus=STATUS[2];
			monitoringList.put("machineSignal", mstatus);
		}
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
			long timediff=DateUtils.getDatePoor(entity.getStartTime(),entity.getEndTime(), "min");
			if(timediff >10L) {
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
						entity.setStartTime(date);
						entity.setEndTime(date);
			}else {
						entity.setEndTime(date);
			}
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

	@Override
	public List<Machine> findMachines(QueryFilters filters) {
		return machineDao.selectAll();
	}

	@Override
	public Integer findStatusNum(String status) {
		Machine machine = new Machine();
		machine.setStatus(status);
		return machineDao.selectCount(machine);
	}

	@Override
	public void update(JSONObject monitor) {
	 String machineName = monitor.getString("machineName");
	 Machine machine = findMachineByName(machineName);
	 update(monitor,machine);
	}

	@Override
	public void publish(String topic, String content) {
		 
	}

	@Override
	public List<Machine> findMachineByIo(Integer i) {
		 Example example = new Example(Machine.class);
		 Criteria criteria = example.createCriteria();
		 criteria.andNotEqualTo("io", i);
		return machineDao.selectByExample(example);
	}
	 
}
