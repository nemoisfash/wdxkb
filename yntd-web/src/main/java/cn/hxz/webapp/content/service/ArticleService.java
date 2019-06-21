package cn.hxz.webapp.content.service;

import java.util.List;
import java.util.Map;

import cn.hxz.webapp.content.entity.Article;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * ArticleSerivce.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ArticleService {
	
	Article load(Long id);
	
	Article loadCached(Long id);
	
	Page<Article> find(Long channelId, Map<String, Object> filters, PageRequest pageable);
	
	List<Map<String,Object>> findArticleBycanteenId(Long channelId, Map<String,Object> filters);
	
	Page<Article> findAll(Long channelId, Boolean hasThumb, PageRequest pageable, String orderBy);
	
	Page<Article> findAll(List<Long> channelIds, Boolean hasThumb, PageRequest pageable, String orderBy);
	
	Page<Article> find(Map<String, String> filters, PageRequest pageable, String orderBy);
	
	Page<Article> findArticles(Map<String, String> filters, PageRequest pageable, String orderBy);
	
	Map<String, Object> findAttrcleAttr(Long id);
	
	int updateSelective(Article entity);
	
//	int enable(Long id, boolean curr);
	
	int remove(Long id);

	Article selected(Long id, String type,Long channelId);

	int delete(Long id);

	int count(Map<String, String> filter);

	Page<Article> searchList(String value, PageRequest pageRequest);

	Page<Article> findNewArticles(PageRequest pageRequest);

	int create(Article entity);

	List<Article> findArticleByCIdAndUId(Long channelId, Long canteenId);
//	Page<Article> search(Long channelld,String value, String key,Boolean hasThumb, PageRequest pageable,Map<String , String> map);
	
}
