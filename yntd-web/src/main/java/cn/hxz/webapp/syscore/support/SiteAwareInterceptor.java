package cn.hxz.webapp.syscore.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.hxz.webapp.syscore.entity.Domain;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.service.SiteService;
import net.chenke.playweb.support.ResourceNotFoundException;

/**
 * 
 * @author chenke
 * 
 */
public class SiteAwareInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private SiteService bizSite;
	
	@Value("${adminPath}")
	private String adminPath;
	@Value("${defaultHost}")
	private String defaultHost;
	
	/**
	 * 在处理请求之前被调用
	 * <p>如果返回false，从当前的拦截器往回执行所有拦截器的afterCompletion()，再退出拦截器链。
	 * <p>如果返回true，执行下一个拦截器，直到所有的拦截器都执行完毕，再执行被拦截的Controller 然后进入拦截器链。
	 * <p>从最后一个拦截器往回执行所有的postHandle()，接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		
		Object controller = getController(handler);
		
		if (controller instanceof SiteAware){
			
			String host = request.getServerName();
			if("localhost".equalsIgnoreCase(host))
				host = defaultHost;
			
			Domain domain = bizSite.loadDomain(host);
			if (domain==null || domain.getSiteId()==null)
				throw new ResourceNotFoundException();
			
			Site site = bizSite.load(domain.getSiteId());
			if (site==null)
				throw new ResourceNotFoundException();

			SiteAware aware = (SiteAware) controller;
			aware.setSite(site);
			aware.setHost(host);
			aware.setSkin(domain.getSkin());
		}
		
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 在处理请求执行完成后,生成视图之前执行的动作
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {
		
		if (modelAndView!=null){
			
			String viewName = modelAndView.getViewName();
			if (viewName != null && viewName.startsWith("redirect:"))
				return;
			
			Object controller = getController(handler);
			
			if (controller instanceof SiteAware){
				SiteAware aware = (SiteAware) controller;

				modelAndView.addObject("host", aware.getHost());
				modelAndView.addObject("skin", aware.getSkin());
				modelAndView.addObject("site", aware.getSite());
			}
			
			if (controller instanceof BaseWorkbenchController){
				modelAndView.addObject("adminPath", "/admin");
			}
		}
	}
	
	private Object getController(Object handler){
		Object controller = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			controller = handlerMethod.getBean();
		}else{
			controller = handler;
		}
		return controller;
	}
}
