package cn.hxz.webapp.syscore.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hxz.webapp.syscore.entity.Manager;
import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.mapper.ManagerMapper;
import cn.hxz.webapp.syscore.service.ManagerSerivce;
import cn.hxz.webapp.syscore.service.UserSerivce;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author cn.feeboo
 * 
 */
@Service
public class ManagerSerivceImpl implements ManagerSerivce {

	@Autowired
	private ManagerMapper daoManager;
	@Autowired
	private UserSerivce bizUser;
//	@Autowired
//	private RoleMapper daoRole;

	@Override
	public Manager load(Long id){
		if(id==null)
			return null;
		Manager entity = daoManager.selectByPrimaryKey(id);
		if (entity!=null){
			User account = bizUser.load(id);
			entity.setAccount(account);
		}
		return entity;
	}
	
	@Override
	public Manager load(String username) {
		if (StringUtils.isBlank(username))
			return null;
		User account = bizUser.load(username);
		if (account == null)
			return null;
		Manager entity = daoManager.selectByPrimaryKey(account.getId());
		if (entity == null) {
			return null;
		} else {
			entity.setAccount(account);
			return entity;
		}
	}
	
	@Override
	public Manager loadByUsernameOrEmail(String args){
		if (StringUtils.isBlank(args))
			return null;
		User account = bizUser.loadByUsernameOrEmail(args);
		if (account == null)
			return null;
		Manager entity = daoManager.selectByPrimaryKey(account.getId());
		if (entity == null) {
			return null;
		} else {
			entity.setAccount(account);
			return entity;
		}
	}

	@Override
	public Manager create(Manager entity){
		if (entity==null)
			return null;
		
		User account = bizUser.create(entity.getAccount());
		if (account==null)
			return null;
		
		Manager manager = new Manager();
		manager.setId(account.getId());
		daoManager.insertSelective(manager);
		return entity;
	}
	
	@Override
	public Manager update(Manager entity){
		if (entity==null)
			return null;
		
		User account = null;
		if (entity.getAccount()!=null){
			account = bizUser.update(entity.getAccount());
		}
		
		//daoManager.updateByPrimaryKeySelective(entity);
		
		if (account!=null)
			entity.setAccount(account);
		
		return entity;
	}
	
	@Override
	public boolean remove(Long id){
		return bizUser.remove(id);
	}
	
	@Override
	public boolean delete(Long id){
		return bizUser.delete(id);
	}
	
	@Override
	public Boolean enable(Long id){
		return bizUser.enable(id);
	}

	@Override
	public boolean passwd(Long id, String password) {
		return bizUser.passwd(id, password);
	}
	
	@Override
	public Page<Map<String,Object>> findAll(QueryFilters filters, PageRequest pageable) {
		List<Map<String,Object>> entity = daoManager.findAll(filters, pageable);
		return new PageImpl<Map<String,Object>>(entity, pageable);
	}
	
	@Override
	@Transactional
	public boolean connectRoles(Long id, Long[] roleIds) {
		if (id == null || roleIds==null || roleIds.length < 1)
			return false;
		return bizUser.connectRoles(id, roleIds);
	}

	@Override
	public List<Role> findConnectedRoles(Long id) {
		return bizUser.findConnectedRoles(id);
	}

//	@Override
//	public Map<String, String> queryUserRole(Long id) {
//		return daoRole.queryUserRole(id);
//	}

//	@Override
//	public void createUserRole(Long roleId, Long userId) {
//		bizUser.connectRoles(userId, new Long[]{roleId});
//	}

//	@Override
//	public void updateUserRole(Long userId, Long roleId) {
//		bizUser.connectRoles(userId, new Long[]{roleId});
//	}

//	@Override
//	public List<Object> findRolePermission(Long id) {
//		return daoRole.selectRolePermission(id);
//	}
}
