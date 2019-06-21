package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
import java.util.Map;
import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.service.SiteService;
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
public class SiteDirective implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		Long id = DirectiveUtils.getLong(params, PARAM_ID);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		SiteService bizSite = ctx.getBean(SiteService.class);

		Site item = bizSite.load(id);

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
	}

}
