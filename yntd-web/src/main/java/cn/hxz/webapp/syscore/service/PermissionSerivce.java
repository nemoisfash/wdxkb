package cn.hxz.webapp.syscore.service;

import java.util.List;

import cn.hxz.webapp.syscore.entity.Permission;

/**
 * 
 * @author chenke
 * 
 */
public interface PermissionSerivce {

	Permission load(Long id);
	
	Permission load(String code);
	
	List<Permission> findAll(String parentId);
}
