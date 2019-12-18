package org.tdds.mapper;




import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.tdds.entity.MonitoringList;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface MonitoringMapper extends DynaMapper<MonitoringList>{
	
	Map<String, Object> selectOneByName(@Param(value="name")String name);
	
}
