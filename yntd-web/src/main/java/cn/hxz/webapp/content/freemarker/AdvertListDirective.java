package cn.hxz.webapp.content.freemarker; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hxz.webapp.content.entity.Advert;
import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.service.AdvertGroupService;
import cn.hxz.webapp.content.service.AdvertService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.chenke.playweb.support.freemarker.DirectiveUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.support.spring.ApplicationContextBean;

public class AdvertListDirective implements TemplateDirectiveModel {
	
	public static final String PARAM_SITE = "site";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_LOOP = "loop";
	public static final String PARAM_CACHED = "cached";
	public static final String PARAM_ORDERBY = "orderBy";
	public static final String PARAM_FILTERS = "filters";
	
	public static final Integer PARAM_PAGE_DEFAULT = 1;
	public static final Integer PARAM_SIZE_DEFAULT = 6;
	public static final boolean PARAM_LOOP_DEFAULT = true;
	public static final boolean PARAM_CACHED_DEFAULT = true;
	//public static final String PARAM_FILTERS_KEYS = "sticky,promote,checked,enabled,keyword,channelId,channelIds,code,hasThumb";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, 
			TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
    	
		// 取参数
		Long siteId = DirectiveUtils.getLong(params, PARAM_SITE);
		String code = DirectiveUtils.getString(params, PARAM_CODE);
		Integer page = DirectiveUtils.getInteger(params, PARAM_PAGE, PARAM_PAGE_DEFAULT);
		Integer size = DirectiveUtils.getInteger(params, PARAM_SIZE, PARAM_SIZE_DEFAULT);
		boolean loop = DirectiveUtils.getBoolean(params, PARAM_LOOP, PARAM_LOOP_DEFAULT);
		//boolean cached = DirectiveUtils.getBoolean(params, PARAM_CACHED, PARAM_CACHED_DEFAULT);
		//String orderBy = DirectiveUtils.getString(params, PARAM_ORDERBY);
		
		//Map<String, String> filters = DirectiveUtils.getHashMap(params, PARAM_FILTERS, PARAM_FILTERS_KEYS);
		
		AdvertGroupService bizAdvertGroup = ApplicationContextBean.getBean(AdvertGroupService.class);
		AdvertService bizAdvert = ApplicationContextBean.getBean(AdvertService.class);

		AdvertGroup advertType = bizAdvertGroup.load(siteId, code);
		
		Page<Advert> items = bizAdvert.find(advertType.getId(), new PageRequest(page, size), null);;

		if (loop){
			for (Advert item : items){
				loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(item);
				body.render(env.getOut());
			}
		} else {
			loopVars[0] = DirectiveUtils.getBeansWrapper().wrap(items);
			body.render(env.getOut());
		}
    }
}
