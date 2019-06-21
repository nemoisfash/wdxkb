package cn.hxz.webapp.syscore.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hxz.webapp.syscore.support.BasePortalController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
public class SyscorePortalController extends BasePortalController {

	/**
	 * 根据域名获取站点
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "", "/", "/index" }, method = RequestMethod.GET)
	public String home() {
		return this.view(this.getSite().getTemplate());
	}

	/**
	 * 获取静态页
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/html/**" }, method = RequestMethod.GET)
	public String html(Model model, HttpServletRequest request, HttpServletResponse response) {
		String uuid = HashUtils.MD5(request.getRequestURI());
		String template = extractPathFromPattern(request, model);
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		model.addAttribute("filters", filters);
		return this.view("/_html/" + template);
	}

	@ResponseBody
	@RequestMapping(value = "/api/cookie", method = RequestMethod.POST)
	public Object setCookie(HttpServletResponse response, 
			@RequestParam("name") String name,
			@RequestParam("value") String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		response.addCookie(cookie);
		return new JsonResult(true);
	}
}
