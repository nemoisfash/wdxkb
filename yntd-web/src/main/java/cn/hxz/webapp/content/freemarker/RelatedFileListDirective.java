package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.RelatedFile;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.RelatedFileService;
import cn.hxz.webapp.content.service.impl.ChannelServiceImpl;
import cn.hxz.webapp.content.service.impl.RelatedFileServiceImpl;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class RelatedFileListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_SITE = "site";
	public static final String PARAM_NODE = "node";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_FILTER = "filters";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_CACHED = "isCache";
	public static final String PARAM_ITERATE = "isIterate";
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 6;
	public static final String  PARAM_ORDERBY_DEFAULT = null;
	public static final boolean PARAM_CACHED_DEFAULT = true;
	public static final boolean PARAM_ITERATE_DEFAULT = true;
	public static final boolean PARAM_ENABLED_DEFAULT = true;
	public static final String PARAM_FILTER_KEYS = "channelId,keyword,promote";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		String[] nodes = DirectiveUtils.getStringArray(params, PARAM_NODE);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		String orderBy = DirectiveUtils.getString(params, PARAM_ORDERBY, PARAM_ORDERBY_DEFAULT);
		boolean cached = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);
		boolean iterate = DirectiveUtils.getBoolean(params, PARAM_ITERATE, PARAM_ITERATE_DEFAULT);
		Map<String,String> filters = DirectiveUtils.getHashMap(params, PARAM_FILTER, PARAM_FILTER_KEYS);
    	filters.put("enabled", "1");
		ApplicationContext ctx = ApplicationContextBean.getApplicationContext();
		
		ChannelService bizChannel = ctx.getBean(ChannelService.class);
		RelatedFileService bizDownload = ctx.getBean(RelatedFileService.class);

		Page<RelatedFile> items = null;
		
		if (filters.keySet().size() == 0){
			List<Long> channelIds = new ArrayList<Long>();
			for (String node : nodes){
				Channel channel = null;
				if (cached)
					channel = bizChannel.loadCached(siteId, node);
				else
					channel = bizChannel.load(siteId, node);
				channelIds.add(channel.getId());
			}
			
			if (channelIds.size()==1){
				items = bizDownload.find(channelIds.get(0), new PageRequest(page, size), orderBy);
			} else {
				items = bizDownload.find(channelIds, new PageRequest(page, size), orderBy);
			}
		} else {
			List<Long> channelIds = new ArrayList<Long>();
			for (String node : nodes){
				Channel channel = null;
				if (cached)
					channel = bizChannel.loadCached(siteId, node);
				else
					channel = bizChannel.load(siteId, node);
				channelIds.add(channel.getId());
			}
			filters.put("channelIds", StringUtils.join(channelIds, ","));
			if (cached)
				items = bizDownload.find(filters, new PageRequest(page, size), orderBy);
			else
				items = bizDownload.find(filters, new PageRequest(page, size), orderBy);
		}

		if (iterate){
			for (RelatedFile item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
