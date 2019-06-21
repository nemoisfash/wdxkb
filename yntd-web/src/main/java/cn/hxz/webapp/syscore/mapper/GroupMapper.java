package cn.hxz.webapp.syscore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hxz.webapp.syscore.entity.Group;
import cn.hxz.webapp.syscore.entity.Role;
import net.chenke.playweb.support.mybatis.DynaMapper;

/**
 * 
 * @author chenke
 * 
 */
public interface GroupMapper extends DynaMapper<Group> {
	
	void connectRole(@Param("groupId") Long id, @Param("roleId")Long roleId);

	void disconnectRoles(@Param("groupId") Long id);
	
	List<Role> findConnectedRoles(@Param("groupId") Long id);
}