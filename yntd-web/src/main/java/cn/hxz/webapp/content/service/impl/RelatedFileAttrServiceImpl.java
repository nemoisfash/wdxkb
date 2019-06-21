package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.RelatedFileAttr;
import cn.hxz.webapp.content.mapper.RelatedFileAttrMapper;
import cn.hxz.webapp.content.service.RelatedFileAttrService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RelatedFileAttrServiceImpl.java Create on 2017-03-22 15:20:40
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class RelatedFileAttrServiceImpl implements RelatedFileAttrService {

	public static final String ORDER_BY = "id DESC";

	@Autowired
	private RelatedFileAttrMapper daoRelatedFileAttr;

	@Override
	public RelatedFileAttr load(Long id){
		return daoRelatedFileAttr.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_related_file_attr_'+#id", unless="#result==null")
	public RelatedFileAttr loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<RelatedFileAttr> find(PageRequest pageable){
		return find(pageable, ORDER_BY);
	}

	@Override
	public Page<RelatedFileAttr> find(PageRequest pageable, String orderBy){
		Example example = new Example(RelatedFileAttr.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause(orderBy);
		List<RelatedFileAttr> entities = daoRelatedFileAttr.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedFileAttr>(entities, pageable);
	}

	@Override
	public int create(RelatedFileAttr entity){
		return daoRelatedFileAttr.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_file_attr_'+#entity.id")
	public int update(RelatedFileAttr entity){
		return daoRelatedFileAttr.updateByPrimaryKeySelective(entity);
	}

}
