package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.service.RoleSerivce;
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
public class RoleListDirective implements TemplateDirectiveModel {

	public static final String PARAM_ITERATE = "iterate";
	public static final boolean PARAM_ITERATE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		RoleSerivce bizRole = ctx.getBean(RoleSerivce.class);

		List<Role> items = bizRole.find();

		if (iterate) {
			for (Role item : items) {
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
	}

}
