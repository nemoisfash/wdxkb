package org.tdds.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.RunningRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface RunningRecordMapper extends DynaMapper<RunningRecord> {

	List<Map<String, Object>> exportData(@Param(value="filter")Map<String, Object> filter);

	List<String> findTimeLineTimes(@Param(value="machineId") Long machineId);

	Double findRunningData(@Param(value="map")Map<String, Object> map);

	Double findRankData(@Param(value="name") String name);

	Map<String, Object> findAllRecordsByMachineId(@Param(value="machineId") Long machineId);

}
