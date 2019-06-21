package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.SiteAttr;
import cn.hxz.webapp.syscore.service.SiteAttrService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class SiteAttrDirective implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";
	public static final String PARAM_FIELD = "field";
	public static final String PARAM_IS_CACHE = "cached";
	public static final boolean PARAM_IS_CACHE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		// 取参数
		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		String field = DirectiveUtils.getString(params, PARAM_FIELD);
		boolean cached = DirectiveUtils.getBoolean(params, PARAM_IS_CACHE, PARAM_IS_CACHE_DEFAULT);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();

		SiteAttrService bizSiteAttr = ctx.getBean(SiteAttrService.class);

		SiteAttr item = null;

		if (cached) {
			item = bizSiteAttr.load(id, field);
		} else {
			item = bizSiteAttr.loadCached(id, field);
		}

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
	}

}
