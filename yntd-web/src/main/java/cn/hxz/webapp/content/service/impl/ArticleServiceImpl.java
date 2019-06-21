package cn.hxz.webapp.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import cn.hxz.webapp.content.entity.Article;
import cn.hxz.webapp.content.entity.ArticleAttr;
import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.mapper.ArticleAttrMapper;
import cn.hxz.webapp.content.mapper.ArticleMapper;
import cn.hxz.webapp.content.service.ArticleService;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ArticleSerivceImpl.java Create on 2017-01-16 23:12:09
 * <p>
 * 业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	public static final String ORDER_BY = "id DESC, priority DESC";
	@Autowired
	private ArticleMapper daoArticle;
	@Autowired
	private ArticleAttrMapper daoArticleAttr;
	@Autowired
	private ChannelService bizChannel;

	public Article load(Long articleId) {
		Assert.notNull(articleId);

		Article item = daoArticle.selectByPrimaryKey(articleId);
		item.setAttr(findAttrcleAttr(articleId));
		return item;
	}

	@Cacheable(value = CacheUtils.CACHE_CMS, key = "'cms_article_'+#id", unless = "#result==null")
	public Article loadCached(Long id) {
		return load(id);
	}

	public Page<Article> find(Long channelId, Map<String, Object> filters, PageRequest pageable) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		if (StringUtils.hasText(Objects.toString(filters.get("sticky"), null))) {
			criteria.andEqualTo("sticky", BooleanUtils.toBoolean(Objects.toString(filters.get("sticky"))));
		}
		if (StringUtils.hasText(Objects.toString(filters.get("promote"), null))) {
			criteria.andEqualTo("promote", BooleanUtils.toBoolean(Objects.toString(filters.get("promote"))));
		}
		if (StringUtils.hasText(Objects.toString(filters.get("enabled"), null))) {
			criteria.andEqualTo("enabled", BooleanUtils.toBoolean(Objects.toString(filters.get("enabled"))));
		}
		if (StringUtils.hasText(Objects.toString(filters.get("title"), null))) {
			criteria.andLike("title", "%" + filters.get("title") + "%");
		}
		if (StringUtils.hasText(Objects.toString(filters.get("createBy"), null))) {
			criteria.andEqualTo("createBy", filters.get("createBy"));
		}
		if (StringUtils.hasText(Objects.toString(filters.get("canteenId"), null))) {
			criteria.andEqualTo("canteenId", filters.get("canteenId"));
		}
		criteria.andEqualTo("trashed", false);
		if (channelId != null)
			criteria.andEqualTo("channelId", channelId);
		// if (hasThumb != null && hasThumb)
		// criteria.andIsNotNull("thumbUri");
		example.setOrderByClause(ORDER_BY);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageable);
		// entities = this.attr2map(entities);
		return new PageImpl<Article>(entities, pageable);
	}

	@Override
	public Page<Article> find(Map<String, String> filters, PageRequest pageable, String orderBy) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("enabled", true);
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		if (StringUtils.hasText(filters.get("createTime"))) {
			now.setTime(date);
			now.add(Calendar.DATE, -7);
			date = now.getTime();
			criteria.andGreaterThan("createTime", date);
		}
		if (StringUtils.hasText(filters.get("sticky")))
			criteria.andEqualTo("sticky", BooleanUtils.toBooleanObject(filters.get("sticky")));
		if (StringUtils.hasText(filters.get("promote")))
			criteria.andEqualTo("promote", BooleanUtils.toBooleanObject(filters.get("promote")));
		if (StringUtils.hasText(filters.get("checked")))
			criteria.andEqualTo("checked", BooleanUtils.toBooleanObject(filters.get("checked")));
		if (StringUtils.hasText(filters.get("enabled")))
			criteria.andEqualTo("enabled", BooleanUtils.toBooleanObject(filters.get("enabled")));
		if (StringUtils.hasText(filters.get("hasThumb"))
				&& BooleanUtils.toBooleanObject(filters.get("hasThumb")) != null
				&& BooleanUtils.toBooleanObject(filters.get("hasThumb")))
			criteria.andIsNotNull("thumbUri");
		if (StringUtils.hasText(filters.get("keyword")))
			criteria.andLike("title", "%" + filters.get("keyword") + "%");

		if (StringUtils.hasText(filters.get("createBy"))) {
			criteria.andEqualTo("createBy", Long.parseLong(filters.get("createBy")));
		}

		if (StringUtils.hasText(filters.get("canteenIds"))) {
			List<Long> canteenIds = new ArrayList<>();
			for (String canteenId : filters.get("canteenIds").split(",")) {
				if (NumberUtils.isNumber(canteenId)) {
					canteenIds.add(Long.parseLong(canteenId));
				}
			}
			criteria.andIn("canteenId", canteenIds);
		}

		if (StringUtils.hasText(filters.get("canteenId"))) {
			criteria.andEqualTo("canteenId", Long.parseLong(filters.get("canteenId")));
		}

		long channelId = NumberUtils.toLong(filters.get("channelId"), -1L);
		if (channelId > 0L)
			criteria.andEqualTo("channelId", channelId);
		List<Long> channelIds1 = processChannelIds(filters.get("channelIds"));
		if (channelIds1 != null) {
			criteria.andIn("channelId", channelIds1);
		} else {
			List<Long> channelIds2 = processChannelNodes(filters.get("siteId"), filters.get("nodes"));
			if (channelIds2 != null)
				criteria.andIn("channelId", channelIds2);
		}

		if (StringUtils.hasText(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<Article> entity = daoArticle.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Article>(entity, pageable);
	}

	@Override
	public Page<Article> findArticles(Map<String, String> filters, PageRequest pageable, String orderBy) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("enabled", true);
		// TODO 处理filter条件
		if (StringUtils.hasText(filters.get("channelId"))) {
			criteria.andEqualTo("channelId", filters.get("channelId"));
		}
		if (StringUtils.hasText(orderBy)) {
			example.setOrderByClause(orderBy);
		} else {
			example.setOrderByClause(ORDER_BY);
		}
		List<Article> entity = daoArticle.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Article>(entity, pageable);
	}

	private List<Long> processChannelIds(String channelIds) {
		List<Long> result = null;
		if (StringUtils.hasText(channelIds)) {
			// String[] splited = StringUtils.split(channelIds, ",");
			String[] splited = channelIds.split(",");
			if (splited != null && splited.length > 0) {
				Long[] ids = new Long[splited.length];
				for (int i = 0; i < splited.length; i++)
					ids[i] = NumberUtils.toLong(splited[i]);
				result = Arrays.asList(ids);
			}
		}
		return result;
	}

	private List<Long> processChannelNodes(String siteId, String nodes) {
		List<Long> result = null;
		Long currSiteId = NumberUtils.toLong(siteId, -1L);
		if (currSiteId > 0 && StringUtils.hasText(nodes)) {
			String[] splited = StringUtils.split(nodes, ",");
			if (splited != null && splited.length > 0) {
				Long[] ids = new Long[splited.length];
				for (int i = 0; i < splited.length; i++) {
					Channel channel = bizChannel.loadCached(currSiteId, splited[i]);
					ids[i] = NumberUtils.toLong(splited[i]);
				}
				result = Arrays.asList(ids);
			}
		}
		return result;
	}

	public Page<Article> findAll(Long channelId, Boolean hasThumb, PageRequest pageable, String orderBy) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("enabled", true);
		if (channelId != null)
			criteria.andEqualTo("channelId", channelId);
		if (hasThumb != null && hasThumb)
			criteria.andIsNotNull("thumbUri");

		if (StringUtils.hasText(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageable);
		// entities = this.attr2map(entities);
		return new PageImpl<Article>(entities, pageable);
	}

	public Page<Article> findAllWithAttr(Long channelId, PageRequest pageable, String orderBy) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		if (channelId != null)
			criteria.andEqualTo("channelId", channelId);
		if (StringUtils.hasText(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageable);
		// this.attr2map(entities);
		return new PageImpl<Article>(entities, pageable);
	}

	// public List<Article> findAll(Long channelId, int page, int size, String
	// orderBy){
	// Example example = new Example(Article.class);
	// if (channelId!=null)
	// example.createCriteria().andEqualTo("channelId", channelId);
	// if (StringUtils.hasText(orderBy))
	// example.setOrderByClause(orderBy);
	// else
	// example.setOrderByClause(ORDER_BY);
	// PageRequest pageable = new PageRequest(page, size);
	// List<Article> content = daoArticle.selectByExampleAndRowBounds(example,
	// pageable);
	// return new PageImpl<Article>(content, pageable);
	// }

	public Page<Article> findAll(List<Long> channelIds, Boolean hasThumb, PageRequest pageable, String orderBy) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		criteria.andIn("channelId", channelIds);
		if (hasThumb != null && hasThumb)
			criteria.andIsNotNull("thumbUri");

		if (StringUtils.hasText(orderBy))
			example.setOrderByClause(orderBy);
		else
			example.setOrderByClause(ORDER_BY);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageable);
		// this.attr2map(entities);
		return new PageImpl<Article>(entities, pageable);
	}

	@Override
	public Map<String, Object> findAttrcleAttr(Long articleId) {
		Assert.notNull(articleId);
		Example example = new Example(ArticleAttr.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);
		List<ArticleAttr> items = daoArticleAttr.selectByExample(example);
		Map<String, Object> map = new HashMap<String, Object>();
		if (items != null && !items.isEmpty())
			for (ArticleAttr attr : items)
				map.put(attr.getField(), attr.getValue());

		return map;
	}

	private List<ArticleAttr> findAttrcleAttr(List<Long> ids) {
		Example example = new Example(ArticleAttr.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
		return daoArticleAttr.selectByExample(example);
	}

	public int create(Article entity) {
		if (entity.getAttr() != null && !entity.getAttr().isEmpty()) {
			for (String key : entity.getAttr().keySet()) {
				ArticleAttr attr = new ArticleAttr();
				attr.setId(entity.getId());
				attr.setField(key);
				attr.setValue(Objects.toString(entity.getAttr().get(key), null));
				daoArticleAttr.insert(attr);
			}
		}
		return daoArticle.insert(entity);
	}

	public int updateSelective(Article entity) {
		int cnt = daoArticle.updateByPrimaryKeySelective(entity);
		if (entity.getAttr() != null && !entity.getAttr().isEmpty()) {
			for (String key : entity.getAttr().keySet()) {
				ArticleAttr attr = new ArticleAttr();
				attr.setId(entity.getId());
				attr.setField(key);
				attr.setValue(Objects.toString(entity.getAttr().get(key), null));
				daoArticleAttr.updateByPrimaryKey(attr);
			}
		}
		return cnt;
	}

	// public int enable(Long id, boolean curr) {
	// Article entity = new Article();
	// entity.setId(id);
	// entity.setEnabled(curr);
	// return daoArticle.updateByPrimaryKeySelective(entity);
	// }

	public int remove(Long id) {
		Article entity = new Article();
		entity.setId(id);
		entity.setTrashed(true);
		return daoArticle.updateByPrimaryKeySelective(entity);
	}

	// XXX: 效率低，需考虑优化
	private List<Article> attr2map(List<Article> entities) {
		List<Long> ids = new ArrayList<Long>();
		for (Article entity : entities)
			ids.add(entity.getId());
		List<ArticleAttr> attrs = this.findAttrcleAttr(ids);

		for (int i = 0; i < entities.size(); i++) {
			for (ArticleAttr attr : attrs) {
				if (attr.getId() == entities.get(i).getId())
					entities.get(i).getAttr().put(attr.getField(), attr.getValue());
			}
		}
		return entities;
	}

	@Override
	public int delete(Long id) {
		return daoArticle.deleteByPrimaryKey(id);
	}

	// @Override
	// public Page<Article> search(Long channelId, String value, String key,
	// Boolean hasThumb, PageRequest pageable,
	// Map<String, String> map) {
	// Example example = new Example(Article.class);
	// Criteria criteria = example.createCriteria();
	// if (StringUtils.hasText(map.get("sticky"))) {
	// criteria.andEqualTo("sticky", BooleanUtils.toBoolean(map.get("sticky")));
	// }
	// if (StringUtils.hasText(map.get("promote"))) {
	// criteria.andEqualTo("promote",
	// BooleanUtils.toBoolean(map.get("promote")));
	// }
	// if (StringUtils.hasText(map.get("enabled"))) {
	// criteria.andEqualTo("enabled",
	// BooleanUtils.toBoolean(map.get("enabled")));
	// }
	// if (channelId != null) {
	// criteria.andEqualTo("channelId", channelId);
	// }
	// if (StringUtils.hasText(value))
	// criteria.andLike(key, "%" + value + "%");
	// if (hasThumb != null && hasThumb) {
	// criteria.andIsNotNull("thumbUri");
	// }
	// example.setOrderByClause(ORDER_BY);
	// List<Article> entities = daoArticle.selectByExampleAndRowBounds(example,
	// pageable);
	// return new PageImpl<Article>(entities, pageable);
	// }

	@Override
	public Article selected(Long id, String type, Long channelId) {
		if (StringUtils.hasText("type") && type.equalsIgnoreCase("provious")) {
			return daoArticle.selectProviousOne(id, channelId);

		} else if (StringUtils.hasText("type") && type.equalsIgnoreCase("next")) {
			return daoArticle.selectNextOne(id, channelId);
		}
		return null;

	}

	@Override
	public int count(Map<String, String> filter) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		// TODO 处理filter条件
		if (NumberUtils.isNumber(filter.get("channelId"))) {
			criteria.andEqualTo("channelId", NumberUtils.toLong(filter.get("channelId")));
		}
		if (NumberUtils.isNumber(filter.get("enabled"))) {
			criteria.andEqualTo("enabled", NumberUtils.toLong(filter.get("enabled")));
		}
		criteria.andEqualTo("trashed", false);
		return daoArticle.selectCountByExample(example);
	}

	@Override
	public Page<Article> searchList(String value, PageRequest pageRequest) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andLike("title", "%" + value + "%");
		example.setOrderByClause(ORDER_BY);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageRequest);
		return new PageImpl<Article>(entities, pageRequest);
	}

	@Override
	public Page<Article> findNewArticles(PageRequest pageRequest) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, -7);
		criteria.andGreaterThan("createTime", date);
		List<Article> entities = daoArticle.selectByExampleAndRowBounds(example, pageRequest);
		return new PageImpl<Article>(entities, pageRequest);
	}

	@Override
	public List<Article> findArticleByCIdAndUId(Long channelId, Long canteenId) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("channelId", channelId);
		criteria.andEqualTo("canteenId", canteenId);
		return daoArticle.selectByExample(example);
	}

	@Override
	public List<Map<String, Object>> findArticleBycanteenId(Long channelId, Map<String, Object> filters) {
		List<Map<String, Object>> entities = daoArticle.findArticleBycanteenId(channelId, filters);
		return entities;
	}
}