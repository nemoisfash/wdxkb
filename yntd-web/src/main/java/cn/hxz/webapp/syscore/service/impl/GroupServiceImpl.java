package cn.hxz.webapp.syscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hxz.webapp.syscore.entity.Group;
import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.mapper.GroupMapper;
import cn.hxz.webapp.syscore.service.GroupSerivce;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @author cn.feeboo
 * 
 */
@Service
public class GroupServiceImpl implements GroupSerivce {

	@Autowired
	private GroupMapper daoGroup;

	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_group_'+#id", unless="#result==null")
	public Group load(Long id){
		if(id==null)
			return null;
		return daoGroup.selectByPrimaryKey(id);
	}

	@Override
	public Group create(Group entity) {
		if (entity==null)
			return null;
		daoGroup.insertSelective(entity);
		return entity;
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_group_'+#entity.id")
	public Group update(Group entity) {
		if (entity==null)
			return null;
		daoGroup.updateByPrimaryKeySelective(entity);
		return entity;
	}

	@Override
	@Transactional
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_group_'+#id")
	public boolean remove(Long id) {
		if (id==null)
			return false;
		
		Group entity = new Group();
		entity.setId(id);
		entity.setTrashed(true);
		int cnt = daoGroup.updateByPrimaryKeySelective(entity);
		
		return cnt > 0;
	}

	@Override
	@Transactional
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_group_'+#id")
	public boolean delete(Long id) {
		if (id==null)
			return false;
		
		int cnt = daoGroup.deleteByPrimaryKey(id);
		
		return cnt > 0;
	}

	@Override
	@Transactional
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_group_'+#id")
	public boolean enable(Long id) {
		if (id==null)
			return false;

		Group record = daoGroup.selectByPrimaryKey(id);
		if (record==null)
			return false;
		
		Group entity = new Group();
		entity.setId(id);
		entity.setEnabled(!record.getEnabled());
		int cnt = daoGroup.updateByPrimaryKeySelective(entity);
		
		return cnt > 0;
		
	}
	
	@Override
	public List<Group> find(){
		Example example = new Example(Group.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("enabled", true);
		criteria.andEqualTo("trashed", false);
//		criteria.andEqualTo("builtin", false);
		return daoGroup.selectByExample(example);
	}
	
	@Override
	public List<Group> find(Long parentId){
		Example example = new Example(Group.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("enabled", true);
		criteria.andEqualTo("trashed", false);
//		criteria.andEqualTo("builtin", false);
		if(parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		return daoGroup.selectByExample(example);
	}

	@Override
	public Page<Group> findAll(Long parentId, PageRequest pageable) {
		Example example = new Example(Group.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
//		criteria.andEqualTo("builtin", false);
		if(parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		//example.setOrderByClause(ORDER_BY);
		List<Group> entities = daoGroup.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Group>(entities, pageable);
	}
	
	@Override
	@Transactional
	public boolean connectRoles(Long id, Long[] roleIds) {
		if (id == null)
			return false;
		daoGroup.disconnectRoles(id);
		if (roleIds != null && roleIds.length > 0) {
			for (Long roleId : roleIds) {
				daoGroup.connectRole(id, roleId);
			}
		}
		return true;
	}

	@Override
	public List<Role> findConnectedRoles(Long groupId) {
		return daoGroup.findConnectedRoles(groupId);
	}
}
