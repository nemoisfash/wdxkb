package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.Navlink;
import cn.hxz.webapp.content.entity.NavlinkType;
import cn.hxz.webapp.content.service.CmsNavlinkService;
import cn.hxz.webapp.content.service.CmsNavlinkTypeService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class NavlinkListDirective implements TemplateDirectiveModel {

	public static final String PARAM_SITE = "site";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_PARENT_ID = "parentId";
	public static final String PARAM_ITERATE = "iterate";

	public static final Long    PARAM_PARENT_ID_DEFAULT = null;
	public static final boolean PARAM_ITERATE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		String code = DirectiveUtils.getString(params, PARAM_CODE);
		Long parentId = DirectiveUtils.getLong(params, PARAM_PARENT_ID, PARAM_PARENT_ID_DEFAULT);
		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		CmsNavlinkService bizNavlink = ctx.getBean(CmsNavlinkService.class);
		CmsNavlinkTypeService bizNavlinkType = ctx.getBean(CmsNavlinkTypeService.class);
		
		if (siteId==null || code==null){
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap("parameters error");
			body.render(env.getOut());
		}
		
		NavlinkType navlinkType = bizNavlinkType.load(siteId, code);
		
		List<Navlink> items = bizNavlink.find(navlinkType.getId(), parentId);

		if (iterate){
			for (Navlink item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
