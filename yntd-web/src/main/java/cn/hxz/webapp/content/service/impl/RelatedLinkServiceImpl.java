package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.RelatedLink;
import cn.hxz.webapp.content.mapper.RelatedLinkMapper;
import cn.hxz.webapp.content.service.RelatedLinkService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RelatedLinkServiceImpl.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class RelatedLinkServiceImpl implements RelatedLinkService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private RelatedLinkMapper daoRelatedLink;

	@Override
	public RelatedLink load(Long id){
		return daoRelatedLink.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_related_link_'+#id", unless="#result==null")
	public RelatedLink loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<RelatedLink> findAll(Long channelId, PageRequest pageable){
		return findAll(channelId, pageable, ORDER_BY);
	}
	
	@Override
	public Page<RelatedLink> findAll(Long channelId,PageRequest pageable, String orderBy){
		Example example = new Example(RelatedLink.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("channelId", channelId);
		example.setOrderByClause(orderBy);
		List<RelatedLink> entities = daoRelatedLink.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedLink>(entities, pageable);
	}
	
	@Override
	public Page<RelatedLink> find(Long channelId, PageRequest pageable){
		return find(channelId,true, pageable, ORDER_BY);
	}

	@Override
	public Page<RelatedLink> find(Long channelId,Boolean enable,PageRequest pageable, String orderBy){
		Example example = new Example(RelatedLink.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("enabled", enable);
		criteria.andEqualTo("channelId", channelId);
		example.setOrderByClause(orderBy);
		List<RelatedLink> entities = daoRelatedLink.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedLink>(entities, pageable);
	}
	
	@Override
	public Page<RelatedLink> find(List<Long> channelIds, PageRequest pageable){
		return find(channelIds, true,pageable, ORDER_BY);
	}
	
	@Override
	public Page<RelatedLink> find(List<Long> channelIds,Boolean enable, PageRequest pageable, String orderBy){
		Example example = new Example(RelatedLink.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("channelId", channelIds);
		criteria.andEqualTo("enabled", enable);
		example.setOrderByClause(orderBy);
		List<RelatedLink> entities = daoRelatedLink.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedLink>(entities, pageable);
	}

	@Override
	public int create(RelatedLink entity){
		return daoRelatedLink.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_link_'+#entity.id")
	public int update(RelatedLink entity){
		return daoRelatedLink.updateByPrimaryKeySelective(entity);
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_link_'+#id")
	public int enable(Long id, boolean curr){
		RelatedLink entity = new RelatedLink();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoRelatedLink.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_link_'+#id")
	public int remove(Long id){
		RelatedLink entity = new RelatedLink();
		entity.setId(id);
		entity.setTrashed(true);
		return daoRelatedLink.updateByPrimaryKeySelective(entity);
	}

}
