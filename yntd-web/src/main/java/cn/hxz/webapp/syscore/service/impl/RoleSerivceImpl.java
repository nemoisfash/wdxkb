package cn.hxz.webapp.syscore.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.mapper.RoleMapper;
import cn.hxz.webapp.syscore.service.RoleSerivce;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @author chenke
 * 
 */
@Service
public class RoleSerivceImpl implements RoleSerivce {

	@Autowired
	private RoleMapper daoRole;

	@Override
	public Role load(Long id){
		if (id == null)
			return null;
		return daoRole.selectByPrimaryKey(id);
	}
	
	@Override
	public Role load(String code){
		if (code == null)
			return null;

		Role record = new Role();
		record.setCode(code);
		return daoRole.selectOne(record);
	}

	@Override
	public Role create(Role entity) {
		if (entity == null)
			return null;
		daoRole.insertSelective(entity);
		return entity;
	}

	@Override
	public Role update(Role entity) {
		if (entity == null)
			return null;
		daoRole.updateByPrimaryKeySelective(entity);
		return entity;
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		if (id==null)
			return false;
		daoRole.disconnectGroups(id);
		daoRole.disconnectAccounts(id);
		daoRole.disconnectPermissions(id);
		int cnt = daoRole.deleteByPrimaryKey(id);
		
		return cnt > 0;
	}
	
	@Override
	public List<Role> find(){
		Example example = new Example(Role.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("builtin", false);
		return daoRole.selectByExample(example);
	}

	@Override
	public Page<Role> findAll(QueryFilters filters, PageRequest pageable) {
		if (filters==null)
			filters = new QueryFilters();
		
		Example example = new Example(Role.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("builtin", false);
		List<Role> entity = daoRole.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Role>(entity, pageable);
	}
	
	@Override
	@Transactional
	public boolean connectPermissions(Long id, Long[] permissionIds) {
		if (id == null)
			return false;
		daoRole.disconnectPermissions(id);
		if (permissionIds != null && permissionIds.length > 0) {
			for (Long permissionId : permissionIds) {
				daoRole.connectPermission(id, permissionId);
			}
		}
		return true;
	}
	
	@Override
	public List<Long> findPermissionIds(Long id){
		return daoRole.findPermissionIds(id);
	}
	
	@Override
	public List<Map<String, Object>> findGroups(Long id){
		return daoRole.findGroups(id);
	}
	
	@Override
	public List<Map<String, Object>> findAccounts(Long id){
		return daoRole.findAccounts(id);
	}

//	@Override
//	public List<String> findCodeByUsername(String username) {
//		List<String> roles = new ArrayList<String>();
//		
//		List<String> codesForUser = daoSyscore.findRoleCodeForUserByUsername(username);
//		List<String> codesForGroup = daoSyscore.findRoleCodeForGroupByUsername(username);	
//		
//		roles.addAll(codesForUser);
//		roles.addAll(codesForGroup);
//		
//		return roles;
//	}

//	@Override
//	public List<Object> queryRolePermission(Long id) {
//		return daoRole.queryRolePermission(id);
//	}

//	@Override
//	public Page<Role> find(String code,  PageRequest pageable) {
//		Example example = new Example(Role.class);
//		Criteria criteria = example.createCriteria();
//		if (StringUtils.isNotBlank(code))
//		criteria.andEqualTo("code", code);
//		criteria.andGreaterThan("id", 6);
//		List<Role> entity = daoRole.selectByExampleAndRowBounds(example, pageable);
//		return new PageImpl<Role>(entity, pageable);
//	}

//	@Override
//	public int findRoleCount(Long id) {
//		return daoRole.selectRoleCount(id);
//	}
}
