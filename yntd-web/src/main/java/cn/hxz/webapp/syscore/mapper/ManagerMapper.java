package cn.hxz.webapp.syscore.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hxz.webapp.syscore.entity.Manager;
import net.chenke.playweb.support.mybatis.DynaMapper;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface ManagerMapper extends DynaMapper<Manager> {
	
	List<Map<String, Object>> findAll(@Param("args") Map<String, Object> args, PageRequest pageable);
}