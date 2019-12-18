package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;
import org.tdds.entity.MonitoringList;
import org.tdds.entity.WarningRecord;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

public interface WarningRecordService {


	Page<WarningRecord> findAllRecords(QueryFilters filters, PageRequest pageable);

	List<Map<String, Object>> exportData(QueryFilters filters);

	List<Map<String,Object>> findAll();

	List<String> findTimeLineTimes(Long id);

	void insert(Map<String, Object> monitoringList, Machine entity);

	Double findAlarmData(Map<String, Object> map);

	Map<String, Object> findAllRecordsByMachineId(Long id);

	Double findTimeDiffByFilters(QueryFilters filters);
}
