package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.RelatedLinkAttr;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * RelatedLinkAttrService.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface RelatedLinkAttrService {

	RelatedLinkAttr load(Long id);

	RelatedLinkAttr loadCached(Long id);

	Page<RelatedLinkAttr> find(PageRequest pageable);

	Page<RelatedLinkAttr> find(PageRequest pageable, String orderBy);

	int create(RelatedLinkAttr entity);
	
	int update(RelatedLinkAttr entity);

}
