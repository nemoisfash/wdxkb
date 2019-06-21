package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.AdvertGroup;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface AdvertGroupService {
	
	AdvertGroup load(Long id);
	
	AdvertGroup load(Long siteId, String code);

	AdvertGroup create(AdvertGroup entity);
	
	AdvertGroup update(AdvertGroup entity);
	
	void remove(Long id);
	
	void delete(Long id);
	
	boolean enable(Long id);
	
	List<AdvertGroup> find(Long siteId);
	
	Page<AdvertGroup> findAll(Long siteId, Boolean enabled, PageRequest pageable);
}
