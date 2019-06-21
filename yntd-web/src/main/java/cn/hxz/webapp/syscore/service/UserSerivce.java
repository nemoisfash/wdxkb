package cn.hxz.webapp.syscore.service;

import java.util.List;

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.entity.User;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface UserSerivce {

	User load(Long id);
	
	User load(String username);
	
	User loadByUsernameOrEmail(String args);
	
	User create(User entity);
	
	User update(User entity);

	boolean remove(Long id);

	boolean delete(Long id);
	
	boolean enable(Long id);
	
	boolean passwd(Long id, String password);
	
	boolean connectRoles(Long id, Long[] roleIds);
	
	List<Role> findConnectedRoles(Long id);
	
	Page<User> findAll(QueryFilters filters, PageRequest pageable);
	
	List<String> findRolesCode(String username);
	
	List<String> findPermissionsCode(String username);
}
