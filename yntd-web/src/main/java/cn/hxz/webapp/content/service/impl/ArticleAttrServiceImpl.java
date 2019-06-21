package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.ArticleAttr;
import cn.hxz.webapp.content.mapper.ArticleAttrMapper;
import cn.hxz.webapp.content.service.ArticleAttrService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ArticleAttrServiceImpl.java Create on 2017-03-22 15:20:40
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ArticleAttrServiceImpl implements ArticleAttrService {

	public static final String ORDER_BY = "id DESC";

	@Autowired
	private ArticleAttrMapper daoArticleAttr;

	@Override
	public ArticleAttr load(Long id){
		return daoArticleAttr.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_article_attr_'+#id", unless="#result==null")
	public ArticleAttr loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<ArticleAttr> find(PageRequest pageable){
		return find(pageable, ORDER_BY);
	}

	@Override
	public Page<ArticleAttr> find(PageRequest pageable, String orderBy){
		Example example = new Example(ArticleAttr.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause(orderBy);
		List<ArticleAttr> entities = daoArticleAttr.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<ArticleAttr>(entities, pageable);
	}

	@Override
	public int create(ArticleAttr entity){
		return daoArticleAttr.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_article_attr_'+#entity.id")
	public int update(ArticleAttr entity){
		return daoArticleAttr.updateByPrimaryKeySelective(entity);
	}

}
