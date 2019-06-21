package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.impl.ChannelServiceImpl;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class ChannelListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_SITE = "site";
	public static final String PARAM_NODE = "node";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_USE_CACHE = "isCache";
	public static final String PARAM_IS_ITERATE = "isIterate";
	
	public static final String PARAM_ORDERBY_DEFAULT = ChannelServiceImpl.ORDER_BY;
	public static final boolean PARAM_USE_CACHE_DEFAULT = true;
	public static final boolean PARAM_IS_ITERATE_DEFAULT = true;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		String node = DirectiveUtils.getString(params, PARAM_NODE);
		String orderBy = DirectiveUtils.getString(params, PARAM_ORDERBY, PARAM_ORDERBY_DEFAULT);
		boolean isIterate = DirectiveUtils.getBoolean(params, PARAM_IS_ITERATE, PARAM_IS_ITERATE_DEFAULT);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_USE_CACHE, PARAM_USE_CACHE_DEFAULT);
		
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		//SiteSerivceImpl bizWebsite = ctx.getBean(SiteSerivce.class);
		ChannelService bizChannel = ctx.getBean(ChannelService.class);
		
		Channel parent = null;
		
		if (id!=null){
			if (isCache)
				parent = bizChannel.loadCached(id);
			else
				parent = bizChannel.load(id);
		} else {
			if (!"root".equalsIgnoreCase(node)) {
				if (isCache)
					parent = bizChannel.loadCached(siteId, node);
				else
					parent = bizChannel.load(siteId, node);
			}
		}
		
		List<Channel> items = null;
		if (parent==null)
			items = bizChannel.findRootChannels(siteId, orderBy);
		else
			items = bizChannel.findChildren(parent.getId(), orderBy);
		
		if (isIterate){
			for (Channel item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
	
}
