package org.tdds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.WarningRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface WarningRecordMapper extends DynaMapper<WarningRecord> {
	List<Map<String, Object>> findAllRecordById(@Param(value = "id") Long id);

	List<Map<String, Object>> exportData(@Param(value = "filter") Map<String, Object> filter);

	List<Map<String, Object>> selectWarningRecords();

	List<String> findTimeLineTimes(@Param(value = "machineId") Long machineId);

	Double findAlarmData(@Param(value = "map") Map<String, Object> map);

	Map<String, Object> findAllRecordsByMachineId(@Param(value = "machineId") Long machineId);

	Double findTimeDiffByFilters(@Param(value = "filter") Map<String, Object> filter);
}
