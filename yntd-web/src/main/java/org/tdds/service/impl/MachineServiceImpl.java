package org.tdds.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Machine;
import org.tdds.mapper.MachineMapper;
import org.tdds.service.MachineService;
import net.chenke.playweb.QueryFilters;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import org.springframework.util.StringUtils;

@Service
public class MachineServiceImpl implements MachineService {

	private static final String CONFIG_FILE = "machineName/machineInfo.properties";

	@Autowired
	private MachineMapper machineDao;

	@Override
	public Long selectMidByName(String machineName) {
		return machineDao.selectIdByName(machineName);
	}

	@Override
	public List<Machine> findMachine() {
		return machineDao.findMachine();
	}

	@Override
	public List<Long> findMachineids() {
		// TODO Auto-generated method stub
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
	public int update(Machine machine) {
		return machineDao.updateByPrimaryKeySelective(machine);
	}

	 
 
}
