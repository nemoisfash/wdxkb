package cn.hxz.webapp.syscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hxz.webapp.syscore.entity.Permission;
import cn.hxz.webapp.syscore.mapper.PermissionMapper;
import cn.hxz.webapp.syscore.service.PermissionSerivce;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @author chenke
 * 
 */
@Service
public class PermissionSerivceImpl implements PermissionSerivce {

	@Autowired
	private PermissionMapper daoPermission;

	@Override
	public Permission load(Long id){
		if (id == null)
			return null;
		return daoPermission.selectByPrimaryKey(id);
	}

	@Override
	public Permission load(String code){
		if (!StringUtils.hasText(code))
			return null;

		Permission record = new Permission();
		record.setCode(code);
		return daoPermission.selectOne(record);
	}
	
	@Override
	public List<Permission> findAll(String parentId) {
		Example example = new Example(Permission.class);
		Criteria criteria = example.createCriteria();
		if(StringUtils.hasText(parentId))
			criteria.andEqualTo("parentId", parentId);
		else
			criteria.andIsNull("parentId");
		return daoPermission.selectByExample(example);
	}
}
