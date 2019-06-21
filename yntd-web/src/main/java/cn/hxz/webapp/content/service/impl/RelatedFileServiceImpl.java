package cn.hxz.webapp.content.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.RelatedFile;
import cn.hxz.webapp.content.mapper.RelatedFileMapper;
import cn.hxz.webapp.content.service.RelatedFileService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RelatedFileServiceImpl.java Create on 2017-03-22 15:25:43
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class RelatedFileServiceImpl implements RelatedFileService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private RelatedFileMapper daoRelatedFile;

	@Override
	public RelatedFile load(Long id){
		return daoRelatedFile.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_related_file_'+#id", unless="#result==null")
	public RelatedFile loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<RelatedFile> find(Map<String, String> filters, PageRequest pageable, String orderBy){
		Example example = new Example(RelatedFile.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		//TODO 处理filter条件
		long channelId = NumberUtils.toLong(filters.get("channelId"), -1L);
		if (channelId > 0L)
			criteria.andEqualTo("channelId", channelId);
		String channelIds = filters.get("channelIds");
		if (StringUtils.isNoneBlank(channelIds))
			criteria.andIn("channelId", Arrays.asList(StringUtils.split(channelIds, ",")));
		if (StringUtils.isNotBlank(filters.get("keyword")))
			criteria.andLike("title", "%"+filters.get("keyword")+"%");
		if (StringUtils.isNotBlank(filters.get("enabled")))
			criteria.andEqualTo("enabled", filters.get("enabled"));

		if (StringUtils.isNotBlank(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<RelatedFile> entity = daoRelatedFile.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedFile>(entity, pageable);
	}

	@Override
	public Page<RelatedFile> find(Long channelId, PageRequest pageable){
		return find(channelId, pageable, ORDER_BY);
	}

	@Override
	public Page<RelatedFile> find(Long channelId, PageRequest pageable, String orderBy){
		Example example = new Example(RelatedFile.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("channelId", channelId);
		example.setOrderByClause(orderBy);
		List<RelatedFile> entities = daoRelatedFile.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedFile>(entities, pageable);
	}
	
	@Override
	public Page<RelatedFile> find(List<Long> channelIds, PageRequest pageable){
		return find(channelIds, pageable, ORDER_BY);
	}
	
	@Override
	public Page<RelatedFile> find(List<Long> channelIds, PageRequest pageable, String orderBy){
		Example example = new Example(RelatedFile.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("channelId", channelIds);
		example.setOrderByClause(orderBy);
		List<RelatedFile> entities = daoRelatedFile.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedFile>(entities, pageable);
	}
	
	@Override
	public Page<RelatedFile> filter(List<Long> channelIds, Map<String,String> filters, PageRequest pageable){
		return filter(channelIds, filters, pageable, ORDER_BY);
	}
	
	@Override
	public Page<RelatedFile> filter(List<Long> channelIds, Map<String,String> filters, PageRequest pageable, String orderBy){
		Example example = new Example(RelatedFile.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("channelId", channelIds);
		if (filters.containsKey("title"))
			criteria.andLike("title", String.format("%%%s%%", filters.get("title")));		
		example.setOrderByClause(orderBy);
		List<RelatedFile> entities = daoRelatedFile.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<RelatedFile>(entities, pageable);
	}

	@Override
	public int create(RelatedFile entity){
		return daoRelatedFile.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_file_'+#entity.id")
	public int update(RelatedFile entity){
		return daoRelatedFile.updateByPrimaryKeySelective(entity);
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_file_'+#id")
	public int enable(Long id, boolean curr){
		RelatedFile entity = new RelatedFile();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoRelatedFile.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'cms_related_file_'+#id")
	public int remove(Long id){
		RelatedFile entity = new RelatedFile();
		entity.setId(id);
		entity.setTrashed(true);
		return daoRelatedFile.updateByPrimaryKeySelective(entity);
	}

}
