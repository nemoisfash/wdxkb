package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.service.AdvertGroupService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class AdvertTypeListDirective implements TemplateDirectiveModel {

	public static final String PARAM_SITE = "site";
	public static final String PARAM_IS_CACHE = "isCache";
	public static final String PARAM_IS_ITERATE = "isIterate";

	public static final boolean PARAM_IS_CACHE_DEFAULT = true;
	public static final boolean PARAM_IS_ITERATE_DEFAULT = true;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_IS_CACHE, PARAM_IS_CACHE_DEFAULT);
		boolean isIterate = DirectiveUtils.getBoolean(params, PARAM_IS_ITERATE, PARAM_IS_ITERATE_DEFAULT);
		
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();

		AdvertGroupService bizAdvertGroup = ctx.getBean(AdvertGroupService.class);
		
		List<AdvertGroup> items = bizAdvertGroup.find(siteId);
		
		if (isIterate){
			for (AdvertGroup item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
	
}
