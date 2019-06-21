package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.RelatedFileAttr;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * RelatedFileAttrService.java Create on 2017-03-22 15:21:28
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface RelatedFileAttrService {

	RelatedFileAttr load(Long id);

	RelatedFileAttr loadCached(Long id);

	Page<RelatedFileAttr> find(PageRequest pageable);

	Page<RelatedFileAttr> find(PageRequest pageable, String orderBy);

	int create(RelatedFileAttr entity);
	
	int update(RelatedFileAttr entity);

}
