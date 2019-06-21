package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.HashMap;
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

public class ArticleAttrDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_IS_CACHE = "isCache";

	public static final boolean PARAM_IS_CACHE_DEFAULT = true;
	
	public static final String PARAM_HAS_IMAGE = "hasImage";
	public static final boolean PARAM_HAS_IMAGE_DEFAULT = false;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long articleId = DirectiveUtils.getLong(params, PARAM_ID);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_IS_CACHE, PARAM_IS_CACHE_DEFAULT);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		ArticleService bizArticle = ctx.getBean(ArticleService.class);

		Map<String, Object> attrMap = null;
		
		if (articleId==null){
			attrMap = new HashMap<String, Object>();
		}else{
			if (isCache)
				attrMap = bizArticle.findAttrcleAttr(articleId);
			else
				attrMap = bizArticle.findAttrcleAttr(articleId);
		}
		
		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(attrMap);
		body.render(env.getOut());
    }
}
