package cn.hxz.webapp.syscore.freemarker;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
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
public class UserDirective implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";
	public static final String PARAM_USERNAME = "username";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		String username = DirectiveUtils.getString(params, PARAM_USERNAME);

		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		UserSerivce bizUser = ctx.getBean(UserSerivce.class);

		User item = null;

		if (id != null) {
			item = bizUser.load(id);
		} else {
			item = bizUser.load(username);
		}

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
	}
}
