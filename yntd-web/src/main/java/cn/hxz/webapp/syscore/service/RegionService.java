package cn.hxz.webapp.syscore.service;

import cn.hxz.webapp.syscore.entity.Region;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * RegionService.java Create on 2017-03-20 16:03:58
 * <p>
 * 所在地区 业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface RegionService {

	Region load(Long id);

	Region loadCached(Long id);

	Page<Region> find(Long parentId, PageRequest pageable);

	Page<Region> find(Long parentId, PageRequest pageable, String orderBy);

	int create(Region entity);
	
	int update(Region entity);

	int enable(Long id, boolean curr);
	
	int remove(Long id);

}
