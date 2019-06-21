package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.Article;
import cn.hxz.webapp.content.service.ArticleService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class ArticlesListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_FILTER = "filters";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_CACHED = "cached";
	public static final String PARAM_ITERATE = "iterate";
	
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 6;
	public static final String PARAM_ORDERBY_DEFAULT = null;
	public static final boolean PARAM_CACHED_DEFAULT = true;
	public static final boolean PARAM_ITERATE_DEFAULT = true;
	
	public static final String PARAM_FILTER_KEYS = "channelId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		String orderBy = DirectiveUtils.getString(params, PARAM_ORDERBY, PARAM_ORDERBY_DEFAULT);
		boolean cached = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);
		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);
		
		Map<String,String> filters = DirectiveUtils.getHashMap(params, PARAM_FILTER, PARAM_FILTER_KEYS);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ArticleService bizArticle = ctx.getBean(ArticleService.class);
		
		Page<Article> items = null;
		
		if (cached){
			items = bizArticle.findArticles(filters, new PageRequest(page, size), orderBy);
		} else {
			items = bizArticle.findArticles(filters, new PageRequest(page, size), orderBy);
		}

		if (iterate){
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
