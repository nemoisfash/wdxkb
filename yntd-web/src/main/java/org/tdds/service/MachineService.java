package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;

import com.alibaba.fastjson.JSONObject;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

public interface MachineService{

	Long selectMidByName(String machineName);
	
	List<Machine> findMachine();

	List<Long> findMachineids();

	String findMachineNames(Long id);

	Page<Machine> findMachines(QueryFilters filters,PageRequest pageable);

	Machine load(Long id);

	List<Map<String, Object>> exportInfore(Long id);

	Machine findMachineByName(String machineName);
	
	void insert(MonitoringList monitoringList);

	void updateImage(Machine machine);

	List<Machine> findMachines(QueryFilters filters);

	Integer findStatusNum(String status);


	void update(JSONObject monitor);

	void publish(String topic, String jsonString);

	List<Machine> findMachineByIo(Integer i);

}
