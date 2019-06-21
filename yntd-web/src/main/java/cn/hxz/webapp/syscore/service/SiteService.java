package cn.hxz.webapp.syscore.service;

import java.util.List;

import cn.hxz.webapp.syscore.entity.Domain;
import cn.hxz.webapp.syscore.entity.Site;

/**
 * 
 * @author chenke
 * 
 */
public interface SiteService {
	
	Site load(Long id);
	
//	Site load(String domain);

	Site create(Site entity);
	
	Site update(Site entity);

	List<Site> findAll();
	
	Domain loadDomain(String domain);
}
