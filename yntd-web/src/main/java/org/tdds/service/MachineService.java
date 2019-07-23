package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;

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

	int update(MonitoringList machine,Machine entity);

	Machine findMachineByName(String machineName);
	
	void insert(MonitoringList monitoringList);

	void updateImage(Machine machine);

	List<Machine> findMachines(QueryFilters filters);

	Integer findStatusNum(String status);

}
