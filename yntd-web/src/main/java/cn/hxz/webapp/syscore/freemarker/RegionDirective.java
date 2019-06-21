package cn.hxz.webapp.syscore.freemarker; 

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Region;
import cn.hxz.webapp.syscore.service.RegionService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

/**
 * 
 * @author chenke
 *
 */
public class RegionDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_ID = "id";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		Long id = DirectiveUtils.getLong(params, PARAM_ID);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		RegionService bizRegion = ctx.getBean(RegionService.class);
		
		Region item = bizRegion.load(id);

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
    }
}
