package cn.hxz.webapp.syscore.service;

import java.util.Map;

import cn.hxz.webapp.syscore.entity.SiteAttr;

/**
 * SiteAttrService.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface SiteAttrService {

	SiteAttr load(Long siteId, String field);

	SiteAttr loadCached(Long siteId, String field);
	
	int save(SiteAttr entity);
	
	Map<String, SiteAttr> find(Long siteId);
	
	Map<String, SiteAttr> findCached(Long siteId);

	Map<String, SiteAttr> find(Long siteId, String section);

	Map<String, SiteAttr> findCached(Long siteId, String section);

	Map<String, SiteAttr> find(Long siteId, String section, String orderBy);
	
	Map<String, SiteAttr> findCached(Long siteId, String section, String orderBy);

	int create(SiteAttr entity);
	
	int update(SiteAttr entity);

}
