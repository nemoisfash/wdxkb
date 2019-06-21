package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.RelatedLinkAttr;
import cn.hxz.webapp.content.mapper.RelatedLinkAttrMapper;
import cn.hxz.webapp.content.service.RelatedLinkAttrService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RelatedLinkAttrServiceImpl.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class RelatedLinkAttrServiceImpl implements RelatedLinkAttrService {

	public static final String ORDER_BY = "id DESC";

	@Autowired
	private RelatedLinkAttrMapper daoRelatedLinkAttr;

	@Override
	public RelatedLinkAttr load(Long id){
		return daoRelatedLinkAttr.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_related_link_attr_'+#id", unless="#result==null")
	public RelatedLinkAttr loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<RelatedLinkAttr> find(PageRequest pageable){
		return find(pageable, ORDER_BY);
	}

	@Override
	public Page<RelatedLinkAttr> find(PageRequest pageable, String orderBy){
		Example example = new Example(RelatedLinkAttr.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause(orderBy);
		List<RelatedLinkAttr> entities = daoRelatedLinkAttr.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedLinkAttr>(entities, pageable);
	}

	@Override
	public int create(RelatedLinkAttr entity){
		return daoRelatedLinkAttr.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_link_attr_'+#entity.id")
	public int update(RelatedLinkAttr entity){
		return daoRelatedLinkAttr.updateByPrimaryKeySelective(entity);
	}

}
