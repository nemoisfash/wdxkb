package cn.hxz.webapp.syscore.freemarker; 

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Option;
import cn.hxz.webapp.syscore.service.OptionService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class OptionDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_CACHED = "isCache";
	public static final boolean PARAM_CACHED_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		boolean cached = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		OptionService bizOption = ctx.getBean(OptionService.class);
		
		Option item = null;
		
		if (id!=null){
			if (cached)
				item = bizOption.loadCached(id);
			else
				item = bizOption.load(id);
		}
		
		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
    }
	
}
