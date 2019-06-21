package cn.hxz.webapp.syscore.freemarker; 

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.syscore.entity.Option;
import cn.hxz.webapp.syscore.service.OptionService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class OptionListDirective implements TemplateDirectiveModel {

	public static final String PARAM_GROUP_ID = "groupId";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_CACHED = "isCache";
	public static final String PARAM_ITERATE = "isIterate";

	public static final Long    PARAM_GROUP_ID_DEFAULT = null;
	public static final String  PARAM_CODE_DEFAULT = null;
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 100;
	public static final boolean PARAM_CACHED_DEFAULT = true;
	public static final boolean PARAM_ITERATE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		String code = DirectiveUtils.getString(params, PARAM_CODE, PARAM_CODE_DEFAULT);
		Long groupId = DirectiveUtils.getLong(params, PARAM_GROUP_ID, PARAM_GROUP_ID_DEFAULT);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);
		boolean isIterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		OptionService bizOption = ctx.getBean(OptionService.class);
		
		Page<Option> items = null;
		
		if (groupId!=null){
			if (isCache)
				items = bizOption.find(groupId, true, new PageRequest(page, size));
			else
				items = bizOption.find(groupId, true, new PageRequest(page, size));
		} else {
			if (isCache)
				items = bizOption.find(code, true, new PageRequest(page, size));
			else
				items = bizOption.find(code, true, new PageRequest(page, size));
		}

		if (isIterate){
			for (Option item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
