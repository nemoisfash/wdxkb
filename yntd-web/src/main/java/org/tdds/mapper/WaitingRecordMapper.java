package org.tdds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.WaitingRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;


public interface WaitingRecordMapper extends DynaMapper<WaitingRecord>{
	Double selectWaitingTime(@Param(value="id")Long id);

	List<Map<String, Object>> findAllRecordById(@Param(value="id")Long id);

	Double findPieData(@Param(value="id")Long id);

	Double findLineData(@Param(value="str")String str,@Param(value="id")Long id);

	Double findGaugeData(@Param(value="id")Long id);

	Double findMemberLineData(@Param(value="date")String date);

	List<Map<String, Object>> exportData(@Param(value="filter")Map<String, Object> filter);
}
