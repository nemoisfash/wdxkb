package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.service.ArticleService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class ArticleCountDirective implements TemplateDirectiveModel {

	public static final String PARAM_FILTER = "filters";
	
	public static final String PARAM_FILTER_KEYS = "channelId,enabled";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Map<String,String> filter = DirectiveUtils.getHashMap(params, PARAM_FILTER, PARAM_FILTER_KEYS);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ArticleService bizArticle = ctx.getBean(ArticleService.class);
		
		int count = bizArticle.count(filter);
		
		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(count);
		body.render(env.getOut());
    }
}
