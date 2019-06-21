package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.mapper.AdvertGroupMapper;
import cn.hxz.webapp.content.service.AdvertGroupService;
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
public class AdvertTypeServiceImpl implements AdvertGroupService {
	
	public static final String ORDER_BY = "priority ASC";

	@Autowired
	private AdvertGroupMapper daoAdvertGroup;

	@Override
	public AdvertGroup load(Long id){
		return daoAdvertGroup.selectByPrimaryKey(id);
	}
	
	@Override
	public AdvertGroup load(Long siteId, String code){
		AdvertGroup record = new AdvertGroup();
		record.setSiteId(siteId);
		record.setCode(code);
		return daoAdvertGroup.selectOne(record);
	}
	
	@Override
	public AdvertGroup create(AdvertGroup entity){
		daoAdvertGroup.insertSelective(entity);
		return entity;
	}
	
	@Override
	public AdvertGroup update(AdvertGroup entity){
		daoAdvertGroup.updateByPrimaryKeySelective(entity);
		return entity;
	}
	
	@Override
	public void remove(Long id){
		AdvertGroup entity = new AdvertGroup();
		entity.setId(id);
		entity.setTrashed(true);
		daoAdvertGroup.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	public void delete(Long id){
		daoAdvertGroup.deleteByPrimaryKey(id);
	}
	
	@Override
	public boolean enable(Long id){
		AdvertGroup entity = load(id);
		entity.setEnabled(!entity.getEnabled());
		daoAdvertGroup.updateByPrimaryKeySelective(entity);
		return entity.getEnabled();
	}
	
	@Override
	public List<AdvertGroup> find(Long siteId){
		Example example = new Example(AdvertGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("enabled", true);
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("siteId", siteId);
		example.setOrderByClause(ORDER_BY);
		return daoAdvertGroup.selectByExample(example);
	}
	
	@Override
	public Page<AdvertGroup> findAll(Long siteId, Boolean enabled, PageRequest pageable){
		Example example = new Example(AdvertGroup.class);
		Criteria criteria = example.createCriteria();
		if (enabled!=null)
			criteria.andEqualTo("enabled", true);
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("siteId", siteId);
		example.setOrderByClause(ORDER_BY);
		List<AdvertGroup> entities = daoAdvertGroup.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<AdvertGroup>(entities, pageable);
	}
}
