package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.SinglesPageAttr;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * SinglesPageAttrService.java Create on 2017-03-22 15:21:29
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface SinglesPageAttrService {

	SinglesPageAttr load(Long id);

	SinglesPageAttr loadCached(Long id);

	Page<SinglesPageAttr> find(PageRequest pageable);

	Page<SinglesPageAttr> find(PageRequest pageable, String orderBy);

	int create(SinglesPageAttr entity);
	
	int update(SinglesPageAttr entity);

}
