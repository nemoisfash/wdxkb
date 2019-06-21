package cn.hxz.webapp.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.Advert;
import cn.hxz.webapp.content.mapper.AdvertMapper;
import cn.hxz.webapp.content.service.AdvertService;
import net.chenke.playweb.QueryFilters;
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
public class AdvertServiceImpl implements AdvertService {
	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private AdvertMapper daoAdvert;

	@Override
	public Advert load(Long id){
		return daoAdvert.selectByPrimaryKey(id);
	}
	
	@Override
	public Advert create(Advert entity){
		daoAdvert.insert(entity);
		return entity;
	}
	
	@Override
	public Advert update(Advert entity){
		daoAdvert.updateByPrimaryKeySelective(entity);
		return entity;
	}
	
	@Override
	public boolean enable(Long id){
		Advert entity = load(id);
		entity.setEnabled(!entity.getEnabled());
		daoAdvert.updateByPrimaryKeySelective(entity);
		return entity.getEnabled();
	}
	
	@Override
	public void remove(Long id){
		Advert entity = new Advert();
		entity.setId(id);
		entity.setTrashed(true);
		daoAdvert.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	public void delete(Long id){
		daoAdvert.deleteByPrimaryKey(id);
	}
	
	@Override
	public Page<Advert> find(Long typeId, PageRequest pageable, QueryFilters filters){
		if (filters==null)
			filters = new QueryFilters();
		Example example = new Example(Advert.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		if (typeId!=null)
			criteria.andEqualTo("typeId", typeId);
		else
			return new PageImpl<Advert>(new ArrayList<Advert>(), pageable);
		
		String orderBy = filters.toString("orderBy");
		if (StringUtils.isNotBlank(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<Advert> entities = daoAdvert.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Advert>(entities, pageable);
	}
	
//	@Override
//	public Page<Advert> findAll(List<Long> groupIds, Map<String,?> filters, PageRequest pageable, String orderBy){
//		Example example = new Example(Advert.class);
//		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("trashed", false);
//		if (groupIds==null || groupIds.size()==0)
//			criteria.andIn("groupId", groupIds);
//		else
//			return new PageImpl<Advert>(new ArrayList<Advert>(), pageable);
//		
//		if (StringUtils.isNotBlank(orderBy))
//			example.setOrderByClause(orderBy);
//		else
//			example.setOrderByClause(ORDER_BY);
//		List<Advert> entities = daoAdvert.selectByExampleAndRowBounds(example, pageable);
//		return new PageImpl<Advert>(entities, pageable);
//	}
}
