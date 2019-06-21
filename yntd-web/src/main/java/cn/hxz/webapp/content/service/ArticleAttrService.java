package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.ArticleAttr;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * ArticleAttrService.java Create on 2017-03-22 15:21:28
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ArticleAttrService {

	ArticleAttr load(Long id);

	ArticleAttr loadCached(Long id);

	Page<ArticleAttr> find(PageRequest pageable);

	Page<ArticleAttr> find(PageRequest pageable, String orderBy);

	int create(ArticleAttr entity);
	
	int update(ArticleAttr entity);

}
