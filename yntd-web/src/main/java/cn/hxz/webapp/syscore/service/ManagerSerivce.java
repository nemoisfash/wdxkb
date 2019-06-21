package cn.hxz.webapp.syscore.service;

import java.util.List;
import java.util.Map;

import cn.hxz.webapp.syscore.entity.Manager;
import cn.hxz.webapp.syscore.entity.Role;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author cn.feeboo
 * 
 */
public interface ManagerSerivce {

	Manager load(Long id);
	
	Manager load(String username);
	
	Manager loadByUsernameOrEmail(String args);
	
	Manager create(Manager account);

	Manager update(Manager account);
	
	boolean remove(Long id);
	
	boolean delete(Long id);

	Boolean enable(Long id);
	
	boolean passwd(Long id, String password);
	
	Page<Map<String, Object>> findAll(QueryFilters filters, PageRequest pageable);
	
	boolean connectRoles(Long id, Long[] roleIds);
	
	List<Role> findConnectedRoles(Long id);

//	void createUserRole(Long roleId, Long userId);

//	void updateUserRole(Long id, Long roleId);
	
//	Map<String, String> queryUserRole(Long id);
	
//	/**
//	 * 根据权限组id连表查询底下有多少权限
//	 * @param id
//	 * @return
//	 */
//	List<Object> findRolePermission(Long id);
}
