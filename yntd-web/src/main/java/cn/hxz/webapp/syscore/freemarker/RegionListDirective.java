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
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

/**
 * 
 * @author chenke
 *
 */
public class RegionListDirective implements TemplateDirectiveModel {

	public static final String PARAM_PARENT_ID = "parentId";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";

	public static final String PARAM_ITERATE = "iterate";

	public static final Long PARAM_PARENT_ID_DEFAULT = null;
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 100;
	public static final boolean PARAM_ITERATE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		Long parentId = DirectiveUtils.getLong(params, PARAM_PARENT_ID, PARAM_PARENT_ID_DEFAULT);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		RegionService bizRegion = ctx.getBean(RegionService.class);

		Page<Region> items = bizRegion.find(parentId, new PageRequest(page, size));

		if (iterate) {
			for (Region item : items) {
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
	}
}
