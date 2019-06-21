package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;

import net.chenke.playweb.QueryFilters;

public interface MachineService{

	Long selectMidByName(String machineName);
	
	List<Machine> findMachine();

	List<Long> findMachineids();

	String findMachineNames(Long id);

	List<Machine> findMachines(QueryFilters filters);

	Machine load(Long id);

	List<Map<String, Object>> exportInfore(Long id);

	int update(Machine machine);
}
