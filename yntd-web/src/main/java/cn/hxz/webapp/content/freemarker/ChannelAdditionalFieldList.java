package cn.hxz.webapp.content.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.AdditionalField;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.impl.ChannelServiceImpl;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class ChannelAdditionalFieldList implements TemplateDirectiveModel {

	public static final String PARAM_ID = "id";

	public static final String PARAM_USE_CACHE = "isCache";
	public static final String PARAM_IS_ITERATE = "isIterate";

	public static final boolean PARAM_USE_CACHE_DEFAULT = true;
	public static final boolean PARAM_IS_ITERATE_DEFAULT = true;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long channelId = DirectiveUtils.getLong(params, PARAM_ID);
		boolean isIterate = DirectiveUtils.getBoolean(params, PARAM_IS_ITERATE, PARAM_IS_ITERATE_DEFAULT);
		boolean isCache = DirectiveUtils.getBoolean(params, PARAM_USE_CACHE, PARAM_USE_CACHE_DEFAULT);
		
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ChannelService bizChannel = ctx.getBean(ChannelService.class);
		
		List<AdditionalField> items = null;
		
		if (isCache)
			items = bizChannel.findAdditionalFieldCached(channelId);
		else
			items = bizChannel.findAdditionalField(channelId);
		
		if (isIterate){
			for (AdditionalField item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
