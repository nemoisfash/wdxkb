package org.tdds.service;



import java.util.List;
import java.util.Map;

import org.tdds.entity.Machine;
import org.tdds.entity.RunningRecord;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

public interface RunningRecordService {

	void insert(Map<String, Object> monitoringList, Machine entity);

	Page<RunningRecord> findAllRecords(QueryFilters filters, PageRequest pageable);

	List<Map<String, Object>> exportData(QueryFilters filters);

	List<String> findTimeLineTimes(Long id);

	Double findRunningData(Map<String, Object> map);

	Double findRankData(Long machineId);

	Map<String, Object> findAllRecordsByMachineId(Long id);

	Double findTimeDiffByFilters(QueryFilters filters);
}
