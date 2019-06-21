package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Permission;
import cn.hxz.webapp.syscore.service.PermissionSerivce;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class PermissionListDirective implements TemplateDirectiveModel {

	public static final String PARAM_PARENT_ID = "parentId";
	public static final String PARAM_ITERATE = "iterate";
	public static final String PARAM_PARENT_ID_DEFAULT = null;
	public static final boolean PARAM_ITERATE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		String parentId = DirectiveUtils.getString(params, PARAM_PARENT_ID, PARAM_PARENT_ID_DEFAULT);
		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		PermissionSerivce bizPermission = ctx.getBean(PermissionSerivce.class);

		List<Permission> items = bizPermission.findAll(parentId);

		if (iterate) {
			for (Permission item : items) {
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
	}
}
