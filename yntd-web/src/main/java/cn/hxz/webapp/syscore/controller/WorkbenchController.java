package cn.hxz.webapp.syscore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

/**
 * 
 * @author chenke
 *
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class WorkbenchController extends BaseWorkbenchController {
	@Autowired
	private UserSerivce bizUser;
	
	@RequestMapping(value = "/workbench", method = RequestMethod.GET)
	public String workbench(ModelMap modelMap) {
		return this.view("/workbench");
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return this.view("/dashboard1");
	}
	
	@RequestMapping(value = "/aboutme", method = RequestMethod.GET)
	public String info(ModelMap modelMap) {
		return this.view("/aboutme");
	}
	
	@RequestMapping(value = "/passwd", method = RequestMethod.GET)
	public String passwd() {
		return this.view("/passwd");
	}
	
	@RequestMapping(value = "/passwd", method = RequestMethod.POST)
	public String passwd(RedirectAttributes model, 
			String oldpasswd, String newpasswd) {

		User currentUser = AuthorizingUtils.loadCurrentUser();

		String oldpasswdHash = HashUtils.MD5(oldpasswd, currentUser.getSalt());
		
		if (!currentUser.getPassword().equalsIgnoreCase(oldpasswdHash)) {
			model.addFlashAttribute("message", "原密码错误");
			return this.redirect("/passwd.html");
		}
		
//		String newpasswdHash = HashUtils.MD5(oldpasswd, currentUser.getSalt());
//		User account = new User();
//		account.setId(currentUser.getId());
//		account.setPassword(newpasswdHash);
		bizUser.passwd(currentUser.getId(), newpasswd);
		
		model.addFlashAttribute("message", "请牢记新密码");
		return this.redirect("/passwd.html");
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
}
