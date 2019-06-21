package cn.hxz.webapp.syscore.mapper;

import cn.hxz.webapp.syscore.entity.Permission;
import net.chenke.playweb.support.mybatis.DynaMapper;

/**
 * PermissionMapper.java Create on 2017-01-16 23:12:09
 * <p>
 * 权限表 Mapper接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface PermissionMapper extends DynaMapper<Permission> {
	
//	List<Object> findRolePermission(@Param("roleId")Long roleId);
}