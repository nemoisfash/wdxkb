package org.tdds.service;
import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;



public interface MonitoringService {
	Map<String, Object> findByName(Machine machine);

	List<MonitoringList> findAll();

	Integer findStatusNum(String status);

	Map<String, Object> subscriberJsonFromMqttServer(Machine machine);
}
