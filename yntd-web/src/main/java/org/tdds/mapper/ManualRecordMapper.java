package org.tdds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.ManualRecord;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface ManualRecordMapper extends DynaMapper<ManualRecord>{

	List<Map<String, Object>> exportData(@Param(value="filter")Map<String, Object> filter);

}
