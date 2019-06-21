package cn.hxz.webapp.syscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.syscore.entity.OptionGroup;
import cn.hxz.webapp.syscore.mapper.OptionGroupMapper;
import cn.hxz.webapp.syscore.service.OptionGroupService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * OptionGroupServiceImpl.java Create on 2017-03-20 16:03:58
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class OptionGroupServiceImpl implements OptionGroupService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private OptionGroupMapper daoOptionGroup;

	@Override
	public OptionGroup load(Long id){
		return daoOptionGroup.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'sys_option_group_'+#id", unless="#result==null")
	public OptionGroup loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<OptionGroup> find(Boolean enabled, PageRequest pageable){
		return find(enabled, pageable, ORDER_BY);
	}
	
	@Override
	public OptionGroup load(String code) {
		OptionGroup record = new OptionGroup();
		record.setCode(code);
		return daoOptionGroup.selectOne(record);
	}

	@Override
	public Page<OptionGroup> find(Boolean enabled, PageRequest pageable, String orderBy){
		Example example = new Example(OptionGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		if (enabled!=null)
			criteria.andEqualTo("enabled", enabled);
		example.setOrderByClause(orderBy);
		List<OptionGroup> entities = daoOptionGroup.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<OptionGroup>(entities, pageable);
	}

	@Override
	public int create(OptionGroup entity){
		return daoOptionGroup.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_group_'+#entity.id")
	public int update(OptionGroup entity){
		return daoOptionGroup.updateByPrimaryKeySelective(entity);
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_group_'+#id")
	public int enable(Long id, boolean curr){
		OptionGroup entity = new OptionGroup();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoOptionGroup.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_group_'+#id")
	public int remove(Long id){
		OptionGroup entity = new OptionGroup();
		entity.setId(id);
		entity.setTrashed(true);
		return daoOptionGroup.updateByPrimaryKeySelective(entity);
	}
}
