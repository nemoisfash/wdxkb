package cn.hxz.webapp.syscore.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.syscore.entity.SiteAttr;
import cn.hxz.webapp.syscore.mapper.SiteAttrMapper;
import cn.hxz.webapp.syscore.service.SiteAttrService;
import cn.hxz.webapp.util.CacheUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * SiteAttrServiceImpl.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class SiteAttrServiceImpl implements SiteAttrService {

	public static final String ORDER_BY = "id DESC";

	@Autowired
	private SiteAttrMapper daoSiteAttr;

	@Override
	public SiteAttr load(Long id, String field){
		SiteAttr record = new SiteAttr();
		record.setId(id);
		record.setField(field);
		return daoSiteAttr.selectOne(record);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_site_attr_id_'+#id+'_field_'+#field", unless="#result==null")
	public SiteAttr loadCached(Long id, String field){
		return load(id, field);
	}
	
	@Override
	public int save(SiteAttr entity){
		SiteAttr record = new SiteAttr();
		record.setId(entity.getId());
		record.setField(entity.getField());
		SiteAttr db = daoSiteAttr.selectOne(record);
		int cnt = 0;
		if (db==null){
			cnt = create(entity);
		} else {
			cnt = update(entity);
		}
		return cnt;
	}
	
	@Override
	public Map<String, SiteAttr> find(Long siteId){
		return find(siteId, null, ORDER_BY);
	}
	
	@Override
	public Map<String, SiteAttr> findCached(Long siteId){
		return findCached(siteId, null, ORDER_BY);
	}
	
	@Override
	public Map<String, SiteAttr> find(Long siteId, String section){
		return find(siteId, section, ORDER_BY);
	}
	
	@Override
	public Map<String, SiteAttr> findCached(Long siteId, String section){
		return findCached(siteId, section, ORDER_BY);
	}
	
	@Override
	public Map<String, SiteAttr> find(Long siteId, String section, String orderBy){
		Example example = new Example(SiteAttr.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", siteId);
		if (StringUtils.isNotBlank(section))
			criteria.andEqualTo("section", section);
		
		if (StringUtils.isNotBlank(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<SiteAttr> entities =  daoSiteAttr.selectByExample(example);
		Map<String, SiteAttr> map = new LinkedHashMap<String,SiteAttr>();
		for (SiteAttr attr : entities){
			if (StringUtils.isNotBlank(attr.getField()) && StringUtils.isNotBlank(attr.getValue()))
				map.put(attr.getField(), attr);
		}
		return map;
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_site_attr_id='+#id+'_section='+#section", unless="#result==null")
	public Map<String, SiteAttr> findCached(Long siteId, String section, String orderBy){
		return find(siteId, section, orderBy);
	}

	@Override
	public int create(SiteAttr entity){
		CacheUtils.evict(CacheUtils.CACHE_SYS, String.format("sys_site_attr_id=%s_section=%s", entity.getId(), entity.getSection()));
		return daoSiteAttr.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_site_attr_id_'+#entity.id+'_field_'+#entity.field")
	public int update(SiteAttr entity){
		CacheUtils.evict(CacheUtils.CACHE_SYS, String.format("sys_site_attr_id=%s_section=%s", entity.getId(), entity.getSection()));
		return daoSiteAttr.updateByPrimaryKeySelective(entity);
	}

}
