package org.tdds.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

@Service
public class MonitoringServiceImpl implements MonitoringService {
	private static final String CONFIG_FILE = "machineName/machineInfo.properties";
	
	private static final String  CODE = "_CODE";
	
	@Autowired
	private MonitoringMapper daoMonitoring;

	@Override
	public MonitoringList findByName(String name) {
		
		return daoMonitoring.selectOneByName(name);
	}

	@Override
	public List<MonitoringList> findAll() {
		return daoMonitoring.selectAll();
	}
	 
}

