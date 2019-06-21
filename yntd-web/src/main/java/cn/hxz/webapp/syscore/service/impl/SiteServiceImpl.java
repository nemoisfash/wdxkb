package cn.hxz.webapp.syscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.syscore.entity.Domain;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.mapper.DomainMapper;
import cn.hxz.webapp.syscore.mapper.SiteMapper;
import cn.hxz.webapp.syscore.service.SiteAttrService;
import cn.hxz.webapp.syscore.service.SiteService;
import cn.hxz.webapp.util.CacheUtils;

/**
 * 
 * @author chenke
 * 
 */
@Service
public class SiteServiceImpl implements SiteService {

	@Autowired
	private SiteMapper daoSite;
	@Autowired
	private DomainMapper daoDomain;
	@Autowired
	private SiteAttrService bizSiteAttr;

	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_site_'+#id", unless="#result==null")
	public Site load(Long id) {
		if(id==null)
			return null;
		
		Site entity = daoSite.selectByPrimaryKey(id);
		if (entity != null)
			entity.setAttr(bizSiteAttr.findCached(entity.getId()));

		return entity;
	}

	@Override
	public Site create(Site entity) {
		if (entity == null)
			return null;
		daoSite.insertSelective(entity);
		return entity;
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_site_'+#entity.id")
	public Site update(Site entity) {
		if (entity == null || entity.getId() == null)
			return null;
		daoSite.updateByPrimaryKeySelective(entity);
		return entity;
	}

	@Override
	public List<Site> findAll() {
		return daoSite.selectAll();
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_domain_'+#domain", unless="#result==null")
	public Domain loadDomain(String domain){
		Domain record = new Domain();
		record.setDomain(domain);
		return daoDomain.selectOne(record);
	}

}
