package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.SinglesPageAttr;
import cn.hxz.webapp.content.mapper.SinglesPageAttrMapper;
import cn.hxz.webapp.content.service.SinglesPageAttrService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * SinglesPageAttrServiceImpl.java Create on 2017-03-22 15:20:41
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class SinglesPageAttrServiceImpl implements SinglesPageAttrService {

	public static final String ORDER_BY = "id DESC";

	@Autowired
	private SinglesPageAttrMapper daoSinglesPageAttr;

	@Override
	public SinglesPageAttr load(Long id){
		return daoSinglesPageAttr.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_singles_page_attr_'+#id", unless="#result==null")
	public SinglesPageAttr loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<SinglesPageAttr> find(PageRequest pageable){
		return find(pageable, ORDER_BY);
	}

	@Override
	public Page<SinglesPageAttr> find(PageRequest pageable, String orderBy){
		Example example = new Example(SinglesPageAttr.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause(orderBy);
		List<SinglesPageAttr> entities = daoSinglesPageAttr.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<SinglesPageAttr>(entities, pageable);
	}

	@Override
	public int create(SinglesPageAttr entity){
		return daoSinglesPageAttr.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_singles_page_attr_'+#entity.id")
	public int update(SinglesPageAttr entity){
		return daoSinglesPageAttr.updateByPrimaryKeySelective(entity);
	}

}
