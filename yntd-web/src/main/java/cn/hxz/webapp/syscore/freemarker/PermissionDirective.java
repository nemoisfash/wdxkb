package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
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

/**
 * 
 * @author chenke
 *
 */
public class PermissionDirective implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";
	public static final String PARAM_CODE = "code";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		String code = DirectiveUtils.getString(params, PARAM_CODE);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		PermissionSerivce bizPermission = ctx.getBean(PermissionSerivce.class);

		Permission item = null;

		if (id != null) {
			item = bizPermission.load(id);
		} else {
			item = bizPermission.load(code);
		}

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
	}
}
