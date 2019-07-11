package org.tdds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.PowerOffRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface PowerOffRecordMapper extends DynaMapper<PowerOffRecord> {

	List<Map<String, Object>> findAllRecordById(@Param(value = "id") Long id);

	List<Map<String, Object>> exportData(@Param(value = "filter") Map<String, Object> filter);

	List<String> findTimeLineTimes(@Param(value = "machineId") Long machineId);

	Double findPoweroffData(@Param(value = "map") Map<String, Object> map);

	Map<String, Object> findAllRecordsByMachineId(@Param(value = "machineId") Long machineId);

	Double findTimeDiffByFilters(@Param(value = "filter") Map<String, Object> filter);
}
