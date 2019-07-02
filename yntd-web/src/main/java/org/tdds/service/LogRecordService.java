package org.tdds.service;

import java.util.List;
import java.util.Map;

public interface LogRecordService {

	Double findData(String date, String status, String name);

	List<Map<String, Object>> findTimeLineData(Long id);
}
