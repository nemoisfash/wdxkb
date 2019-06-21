package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.entity.NavlinkType;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface CmsNavlinkTypeService {

	NavlinkType load(Long id);

	NavlinkType load(Long siteId, String code);

	NavlinkType create(NavlinkType entity);
	
	NavlinkType update(NavlinkType entity);
	
	boolean delete(Long id);
	
//	boolean remove(Long id);

	boolean enable(Long id);
	
	List<NavlinkType> find(Long siteId);

	Page<NavlinkType> findAll(Long siteId, PageRequest pageable);

}
