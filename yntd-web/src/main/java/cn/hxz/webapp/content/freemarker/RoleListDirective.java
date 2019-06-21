package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
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
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class RoleListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_CODE = "code";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_FILTERS = "filters";
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 100;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数

		String code = DirectiveUtils.getString(params, PARAM_CODE);
		String type = DirectiveUtils.getString(params, PARAM_TYPE);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		RoleSerivce bizRole = ctx.getBean(RoleSerivce.class);
		Page<Role> items = bizRole.findAll(null, new PageRequest(page, size));
		
		for (Role item : items){
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
			body.render(env.getOut());
		}
    }
}
