package cn.hxz.webapp.syscore.freemarker; 

import java.io.IOException;
import java.util.Map;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;

public class CurrentUserDirective implements TemplateDirectiveModel {

	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

		User user = AuthorizingUtils.loadCurrentUser();

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(user);
		body.render(env.getOut());
    }
	
}
