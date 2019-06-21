package cn.hxz.webapp.syscore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.syscore.entity.Option;
import cn.hxz.webapp.syscore.entity.OptionGroup;
import cn.hxz.webapp.syscore.mapper.OptionMapper;
import cn.hxz.webapp.syscore.service.OptionGroupService;
import cn.hxz.webapp.syscore.service.OptionService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * OptionServiceImpl.java Create on 2017-03-20 16:03:57
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class OptionServiceImpl implements OptionService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private OptionMapper daoOption;
	@Autowired
	private OptionGroupService bizOptionGroup;
	
	@Override
	public Option load(Long id){
		return daoOption.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'sys_option_'+#id", unless="#result==null")
	public Option loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<Option> find(Long groupId, Boolean enabled, PageRequest pageable){
		return find(groupId, enabled, pageable, ORDER_BY);
	}

	@Override
	public Page<Option> find(Long groupId, Boolean enabled, PageRequest pageable, String orderBy){
		Example example = new Example(Option.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		if (groupId==null)
			criteria.andIsNull("groupId");
		else
			criteria.andEqualTo("groupId", groupId);
		if (enabled!=null)
			criteria.andEqualTo("enabled", enabled);
		example.setOrderByClause(orderBy);
		List<Option> entities = daoOption.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Option>(entities, pageable);
	}
	
	@Override
	public Page<Option> find(String code, Boolean enabled, PageRequest pageable){
		return this.find(code, enabled, pageable, ORDER_BY);
	}
	
	@Override
	public Page<Option> find(String code, Boolean enabled, PageRequest pageable, String orderBy) {
		OptionGroup group = bizOptionGroup.load(code);
		
		if (group==null)
			return new PageImpl<Option>(new ArrayList<Option>(), pageable);
		
		return this.find(group.getId(), enabled, pageable, orderBy);
	}

	@Override
	public int create(Option entity){
		return daoOption.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_'+#entity.id")
	public int update(Option entity){
		return daoOption.updateByPrimaryKeySelective(entity);
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_'+#id")
	public int enable(Long id, boolean curr){
		Option entity = new Option();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoOption.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_option_'+#id")
	public int remove(Long id){
		Option entity = new Option();
		entity.setId(id);
		entity.setTrashed(true);
		return daoOption.updateByPrimaryKeySelective(entity);
	}

}
