package org.tdds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;



public interface LogRecordMapper {

	Double findRunningData(@Param(value="map")Map<String, Object> map);

	Double findPoweroffData(@Param(value="map")Map<String, Object> map);

	Double findAlarmData(@Param(value="map")Map<String, Object> map);

	Double findWaittingData(@Param(value="map")Map<String, Object> map);

	Double findManaulData(@Param(value="map")Map<String, Object> map);

	Double selectRankData(@Param(value="machineId")Long machineId);

	List<Map<String, Object>> exportPowerOffData(@Param(value="machineId")Long machineId,@Param(value="startTime")String startTime,@Param(value="endTime")String endTime);

	List<Map<String, Object>> exportAlarmData(@Param(value="machineId")Long machineId,@Param(value="startTime")String startTime,@Param(value="endTime")String endTime);

	List<Map<String, Object>> exportWaittingData(@Param(value="machineId")Long machineId,@Param(value="startTime")String startTime,@Param(value="endTime")String endTime);
	
}
