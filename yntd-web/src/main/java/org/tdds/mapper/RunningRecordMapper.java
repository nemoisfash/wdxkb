package org.tdds.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.RunningRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface RunningRecordMapper extends DynaMapper<RunningRecord> {

	Double selectRankData(@Param(value="machineId") Long machineId);

	Double findMemberLineData(@Param(value="date")String date);

	List<Map<String, Object>> exportData(@Param(value="filter")Map<String, Object> filter);

}
