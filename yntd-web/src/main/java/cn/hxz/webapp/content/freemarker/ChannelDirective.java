package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
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

public class ChannelDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_SITE = "site";
	public static final String PARAM_NODE = "node";
	public static final String PARAM_USE_CACHE = "isCache";
	public static final boolean PARAM_USE_CACHE_DEFAULT = true;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long id = DirectiveUtils.getLong(params, PARAM_ID);
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		String node = DirectiveUtils.getString(params, PARAM_NODE);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_USE_CACHE, PARAM_USE_CACHE_DEFAULT);
    	
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ChannelService bizChannel = ctx.getBean(ChannelService.class);
		
		Channel item = null;
		
		if (id!=null){
			if (isCache)
				item = bizChannel.loadCached(id);
			else
				item = bizChannel.load(id);
		} else {
			if (isCache)
				item = bizChannel.loadCached(siteId, node);
			else
				item = bizChannel.load(siteId, node);
		}

		loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
		body.render(env.getOut());
    }
	
}
