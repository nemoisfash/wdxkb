package cn.hxz.webapp.syscore.service;

import java.util.List;

import cn.hxz.webapp.syscore.entity.Group;
import cn.hxz.webapp.syscore.entity.Role;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface GroupSerivce {

	Group load(Long id);
	
	Group create(Group entity);
	
	Group update(Group entity);
	
	boolean delete(Long id);
	
	boolean remove(Long id);
	
	boolean enable(Long id);
	
	List<Group> find();
	
	List<Group> find(Long parentId);
	
	Page<Group> findAll(Long parentId, PageRequest pageable);
	
	boolean connectRoles(Long id, Long[] roleIds);
	
	List<Role> findConnectedRoles(Long id);
}
