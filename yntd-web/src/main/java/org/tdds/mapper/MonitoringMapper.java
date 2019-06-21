package org.tdds.mapper;




import org.apache.ibatis.annotations.Param;
import org.tdds.entity.MonitoringList;

import net.chenke.playweb.support.mybatis.DynaMapper;

public interface MonitoringMapper extends DynaMapper<MonitoringList>{
	
	MonitoringList selectOneByName(@Param(value="name")String name);
	
}
