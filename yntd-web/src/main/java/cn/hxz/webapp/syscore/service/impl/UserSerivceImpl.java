package cn.hxz.webapp.syscore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.mapper.UserMapper;
import cn.hxz.webapp.syscore.service.UserSerivce;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.HashUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @author chenke
 * 
 */
@Service
public class UserSerivceImpl implements UserSerivce {
	
	public static final String ORDER_BY = "create_time ASC";
	
	@Autowired
	private UserMapper daoUser;
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_account_'+#id", unless="#result==null")
	public User load(Long id){
		if(id==null)
			return null;

		User entity = CacheUtils.get(CacheUtils.CACHE_SYS, String.format("sys_account_%s", id));
		if (entity!=null)
			return entity;

		User record = new User();
		record.setId(id);
		record.setTrashed(false);
		entity = daoUser.selectOne(record);
		if (entity!=null)
			CacheUtils.put(CacheUtils.CACHE_SYS, String.format("sys_account_%s", entity.getUsername()), entity);

		return entity;
	}

	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_account_'+#username", unless="#result==null")
	public User load(String username) {
		if(StringUtils.isBlank(username))
			return null;

		User entity = CacheUtils.get(CacheUtils.CACHE_SYS, String.format("sys_account_%s", username));
		if (entity!=null)
			return entity;
		
		User record = new User();
		record.setUsername(username);
		record.setTrashed(false);
		entity = daoUser.selectOne(record);
		if (entity!=null)
			CacheUtils.put(CacheUtils.CACHE_SYS, String.format("sys_account_%s", entity.getId()), entity);
		
		return entity;
	}
	
	@Override
	public User loadByUsernameOrEmail(String args) {
		if(StringUtils.isBlank(args))
			return null;
		return daoUser.loadByUsernameOrEmail(args);
	}

	@Override
	public User create(User entity){
		if (entity==null || entity.getUsername()==null || entity.getPassword()==null)
			return null;
		
		String salt = HashUtils.randomSalt();
		entity.setSalt(salt);
		entity.setPassword(HashUtils.MD5(entity.getPassword(), salt));
		daoUser.insertSelective(entity);
		
		return entity;
	}
	
	@Override
	public User update(User entity){
		if (entity==null || entity.getId()==null)
			return null;
		
		int cnt = daoUser.updateByPrimaryKeySelective(entity);
		
		if (cnt > 0)
			evictCached(entity.getId());
		
		return entity;
	}
	
	@Override
	public boolean remove(Long id) {
		if (id==null)
			return false;
		User entity = new User();
		entity.setId(id);
		entity.setTrashed(true);
		int cnt = daoUser.updateByPrimaryKeySelective(entity);
		
		if (cnt > 0)
			evictCached(id);
		
		return cnt > 0;
	}

	@Override
	public boolean delete(Long id) {
		if (id==null)
			return false;
		int cnt = daoUser.deleteByPrimaryKey(id);
		
		if (cnt > 0)
			evictCached(id);
		
		return cnt > 0;
	}

	@Override
	public boolean enable(Long id) {
		if (id==null)
			return false;

		User record = daoUser.selectByPrimaryKey(id);
		if (record==null)
			return false;
		
		User entity = new User();
		entity.setId(id);
		entity.setEnabled(!record.getEnabled());
		int cnt = daoUser.updateByPrimaryKeySelective(entity);
		
		if (cnt > 0)
			evictCached(id);
		
		return cnt > 0;
	}

	@Override
	public boolean passwd(Long id, String password){
		if (id==null || password==null)
			return false;
		
		User account = daoUser.selectByPrimaryKey(id);
		if (account==null)
			return false;
		
		User entity = new User();
		entity.setId(id);
		entity.setPassword(HashUtils.MD5(password, account.getSalt()));
		int cnt = daoUser.updateByPrimaryKeySelective(entity);
		
		if (cnt > 0)
			evictCached(id);
		
		return cnt > 0;
	}
	
	@Override
	@Transactional
	public boolean connectRoles(Long id, Long[] roleIds){
		if (id == null)
			return false;
		daoUser.disconnectRoles(id);
		if (roleIds != null && roleIds.length > 0) {
			for (Long roleId : roleIds) {
				daoUser.connectRole(id, roleId);
			}
		}
		return true;
	}
	
	@Override
	public List<Role> findConnectedRoles(Long id) {
		return daoUser.findConnectedRoles(id);
	}
	
	@Override
	public Page<User> findAll(QueryFilters filters, PageRequest pageable){
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		example.setOrderByClause(ORDER_BY);
		List<User> entities = daoUser.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<User>(entities, pageable);
	}

	@Override
	public List<String> findRolesCode(String username) {
		List<String> roleCodes = new ArrayList<String>();
		
		List<String> roleCodesForUser = daoUser.findRoleCodeForUserByUsername(username);
		List<String> roleCodesForGroup = daoUser.findRoleCodeForGroupByUsername(username);	
		
		roleCodes.addAll(roleCodesForUser);
		roleCodes.addAll(roleCodesForGroup);
		
		return roleCodes;
	}

	@Override
	public List<String> findPermissionsCode(String username) {
		List<String> permissionCodes = new ArrayList<String>();
		
		List<String> permissionCodesForUser = daoUser.findPermissionCodeForUserByUsername(username);
		List<String> permissionCodesForGroup = daoUser.findPermissionCodeForGroupByUsername(username);	
		
		permissionCodes.addAll(permissionCodesForUser);
		permissionCodes.addAll(permissionCodesForGroup);
		
		return permissionCodes;
	}
	
	private void evictCached(Long id){
		CacheUtils.evict(CacheUtils.CACHE_SYS, String.format("sys_account_%s", id));
		User evict = load(id);
		if (evict!=null)
			CacheUtils.evict(CacheUtils.CACHE_SYS, String.format("sys_account_%s", evict.getUsername()));
	}

}
