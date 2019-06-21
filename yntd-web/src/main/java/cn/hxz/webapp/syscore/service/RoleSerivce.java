package cn.hxz.webapp.syscore.service;

import java.util.List;
import java.util.Map;

import cn.hxz.webapp.syscore.entity.Role;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface RoleSerivce {

	Role load(Long key);
	
	Role load(String code);

	Role create(Role role);

	Role update(Role role);
	
	boolean delete(Long id);
	
	List<Role> find();

	Page<Role> findAll(QueryFilters filters, PageRequest pageable);
	
	boolean connectPermissions(Long id, Long[] permissionIds);
	
	List<Long> findPermissionIds(Long id);
	
	List<Map<String, Object>> findGroups(Long id);
	
	List<Map<String, Object>> findAccounts(Long id);
	
//	List<Map<String, Object>> findPermissions(Long id);
	
//	List<String> findCodeByUsername(String username);

//	List<Object> queryRolePermission(Long id);

//	Page<Role> find(String code,  PageRequest pageRequest);
	
//	/**
//	 * 根据权限组id查询有多少管理员在使用它
//	 * @param id
//	 * @return
//	 */
//	int findRoleCount(Long id);
}
