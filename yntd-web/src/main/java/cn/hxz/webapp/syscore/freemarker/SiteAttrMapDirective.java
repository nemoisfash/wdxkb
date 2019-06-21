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

public class SiteAttrMapDirective implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";
	public static final String PARAM_CACHED = "cached";

	public static final boolean PARAM_CACHED_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] vars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_ID, null);
		boolean cached = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		SiteAttrService bizSiteAttr = ctx.getBean(SiteAttrService.class);

		Map<String, SiteAttr> items = bizSiteAttr.findCached(siteId);

		if (cached)
			items = bizSiteAttr.find(siteId);
		else
			items = bizSiteAttr.findCached(siteId);

		vars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
		body.render(env.getOut());
	}

}
