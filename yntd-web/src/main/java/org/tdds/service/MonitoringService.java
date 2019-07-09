package org.tdds.service;
import java.util.List;

import org.tdds.entity.MonitoringList;


public interface MonitoringService {
	MonitoringList findByName(String name);

	List<MonitoringList> findAll();

	Integer findStatusNum(String status);
}
