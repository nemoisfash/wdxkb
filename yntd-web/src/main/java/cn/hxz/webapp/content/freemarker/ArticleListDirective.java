package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

 
import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.Article;
import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.service.ArticleService;
import cn.hxz.webapp.content.service.ChannelService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class ArticleListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_SITE = "site";
	public static final String PARAM_NODE = "node";	
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_USERID = "userId";
	//这是我（lh）标记通知公告的一个标识，
	public static final String PARAM_TAG = "tag";
	public static final String PARAM_CREATE_TIME = "createTime";
	public static final String PARAM_FILTER = "filters";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_KEYWORD = "keyword";
	public static final String PARAM_IS_CACHE = "isCache";
	public static final String PARAM_IS_ITERATE = "isIterate";
	public static final String PARAM_ENABLED = "enabled";

	public static final String PARAM_HAS_THUMB = "thumb";
	
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 6;
	public static final Integer PARAM_TAG_DEFAULT = 2;
	public static final String PARAM_ORDERBY_DEFAULT = null;
	public static final boolean PARAM_ENABLED_DEFAULT = true;
	public static final boolean PARAM_IS_CACHE_DEFAULT = true;
	public static final boolean PARAM_PARAM_CREATE_TIME_DEFAULT = true;
	public static final boolean PARAM_IS_ITERATE_DEFAULT = true;
	public static final boolean PARAM_HAS_THUMB_DEFAULT = false;
	public static final String PARAM_FILTER_KEYS = "sticky,promote,createTime,checked,enabled,keyword,channelId,channelIds,nodes,hasThumb";
//	public static final String PARAM_IS_CASCADE = "isCascade";
//	public static final boolean PARAM_IS_CASCADE_DEFAULT = false;
	
//	public static final String PARAM_HAS_IMAGE = "hasImage";
//	public static final boolean PARAM_HAS_IMAGE_DEFAULT = false;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		Long userId = DirectiveUtils.getLong(params, PARAM_USERID);
		String[] nodes = DirectiveUtils.getStringArray(params, PARAM_NODE);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer tag = DirectiveUtils.getInteger(params, PARAM_TAG, PARAM_TAG_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		String orderBy = DirectiveUtils.getString(params, PARAM_ORDERBY, PARAM_ORDERBY_DEFAULT);
		boolean isIterate = DirectiveUtils.getBoolean(params, PARAM_IS_ITERATE,PARAM_IS_ITERATE_DEFAULT);
		boolean createTime = DirectiveUtils.getBoolean(params, PARAM_CREATE_TIME,PARAM_PARAM_CREATE_TIME_DEFAULT);
		boolean useCache = DirectiveUtils.getBoolean(params, PARAM_IS_CACHE, PARAM_IS_CACHE_DEFAULT);
		boolean hasThumb = DirectiveUtils.getBoolean(params, PARAM_HAS_THUMB,PARAM_HAS_THUMB_DEFAULT);
		boolean enabled = DirectiveUtils.getBoolean(params, PARAM_ENABLED,PARAM_ENABLED_DEFAULT);
		
		Map<String,String> filters = DirectiveUtils.getHashMap(params, PARAM_FILTER, PARAM_FILTER_KEYS);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ChannelService bizChannel = ctx.getBean(ChannelService.class);
		ArticleService bizArticle = ctx.getBean(ArticleService.class);
		Page<Article> items = null;
		if (filters.keySet().size() == 0){
			List<Long> channelIds = new ArrayList<Long>();
			for (String node : nodes){
				Channel channel = null;
				if (useCache)
					channel = bizChannel.loadCached(siteId, node);
				else
					channel = bizChannel.load(siteId, node);
				channelIds.add(channel.getId());
			}
			
			if (channelIds.size()==1){
				items = bizArticle.findAll(channelIds.get(0), hasThumb, new PageRequest(page, size), orderBy);
			} else {
				items = bizArticle.findAll(channelIds, hasThumb, new PageRequest(page, size), orderBy);
			}
		} else {
			filters.put("hasThumb", Boolean.toString(hasThumb));
			if (useCache)
				items = bizArticle.find(filters, new PageRequest(page, size), orderBy);
			else
				items = bizArticle.find(filters, new PageRequest(page, size), orderBy);
		}

		if (isIterate){
			for (Article item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
