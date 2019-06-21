package cn.hxz.webapp.content.service;

import java.util.List;
import java.util.Map;

import cn.hxz.webapp.content.entity.Advert;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.mybatis.Pageable;

/**
 * AdvertSerivce.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface AdvertService {
	
	Advert load(Long id);

	Advert create(Advert entity);
	
	Advert update(Advert entity);
	
	void remove(Long id);
	
	void delete(Long id);
	
	boolean enable(Long id);
	
	Page<Advert> find(Long groupId, PageRequest pageable, QueryFilters filters);
	
//	Page<Advert> findAll(List<Long> groupIds, Map<String,?> filters, PageRequest pageable, String orderBy);
}
