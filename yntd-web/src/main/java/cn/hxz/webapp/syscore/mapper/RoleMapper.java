package cn.hxz.webapp.syscore.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hxz.webapp.syscore.entity.Role;
import net.chenke.playweb.support.mybatis.DynaMapper;

/**
 * 
 * @author chenke
 * 
 */
public interface RoleMapper extends DynaMapper<Role> {

	void connectPermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
	
	void disconnectGroups(@Param("id") Long id);
	
	void disconnectAccounts(@Param("id") Long id);
	
	void disconnectPermissions(@Param("id") Long id);
	
	List<Long> findPermissionIds(@Param("id") Long id);
	
	List<Map<String, Object>> findGroups(@Param("id") Long id);
	
	List<Map<String, Object>> findAccounts(@Param("id") Long id);
	
//	List<Map<String, Object>> selectRole(PageRequest pageable);

//	void deleteRole(@Param("id")Long id);

//	void updateRolePermission(@Param("id")Long id);

//	List<Object> queryRolePermission(@Param("id")Long id);


//	Map<String, String> queryUserRole(@Param("id")Long id);
//	/**
//	 * 根据角色id查询拥有哪些权限
//	 * @param id
//	 * @return
//	 */
//	List<Object> selectRolePermission(@Param("id") Long id);
	
//	/**
//	 * 根据权限组id查询有多少管理员在使用它
//	 * @param id
//	 * @return
//	 */
//	int selectRoleCount(@Param("id")Long id);
}