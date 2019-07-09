package org.tdds.service;

import java.util.Map;

public interface LogRecordService {

	Double findData(String date, String status, Long machineId);

	Map<String, Object> findTimeLineData(Long id, String status);

	Double findRunningData(String date);
}
