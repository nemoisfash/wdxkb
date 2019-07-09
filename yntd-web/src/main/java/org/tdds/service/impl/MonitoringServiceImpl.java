package org.tdds.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.MonitoringList;
import org.tdds.mapper.MonitoringMapper;
import org.tdds.service.MonitoringService;

import net.sf.ehcache.config.Configuration.Monitoring;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MonitoringServiceImpl implements MonitoringService {
	
	private static final String[] TYPE = { "overrideRapid", "overrideSpindle", "overrideFeed"};
	
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

	@Override
	public Integer findStatusNum(String status) {
		Example  example = new Example(MonitoringList.class);
		Criteria criteria= example.createCriteria();
		criteria.andEqualTo("machineSignal",status);
		return daoMonitoring.selectCountByExample(example);
	}
	 
}

