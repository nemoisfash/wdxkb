package cn.hxz.webapp.syscore.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.ShiroUtils;
import net.chenke.playweb.support.spring.MessageSourceBean;
import net.chenke.playweb.util.HashUtils;

/**
 * 
 * @author chenke
 *
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class SysLoginController extends BaseWorkbenchController {
	@Autowired
	private UserSerivce bizUser;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return this.view("/login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(RedirectAttributes model, HttpServletRequest request,
			@RequestParam(value = "username") String username, 
			@RequestParam(value = "password") String password,
			@RequestParam(value = "captcha") String captcha,
			@RequestParam(value = "rememberMe", defaultValue = "true") boolean rememberMe) {
		
		User account=null;
		if (!AuthorizingUtils.validCaptcha(captcha)) {
			model.addFlashAttribute("message", MessageSourceBean.getMessage("err.captcha", "验证码错误"));
			return this.redirect("/login.html");
		}else{
			account = bizUser.load(username);
			if (account == null){
				model.addFlashAttribute("message","账号不存在");
				return this.redirect("/login.html");
			}
			if(account!=null && !account.getEnabled()){//锁定用户不能登录
				model.addFlashAttribute("message","账号已锁定");
				return this.redirect("/login.html");
			}
			
		}
		
		String credentials = HashUtils.MD5(password, account.getSalt());
		UsernamePasswordToken token = new UsernamePasswordToken(username, credentials, rememberMe, ShiroUtils.getHost());
		try {
		 	ShiroUtils.login(token);
			return this.redirect("/dashboard.html");
		} catch (AuthenticationException e) {
			token.clear();
			model.addFlashAttribute("message", e.getMessage());
			return this.redirect("/login.html");
		}
	}

	@RequestMapping(value = { "/logout" }, method = RequestMethod.GET)
	public String logout() {
		try {
			Subject subject = ShiroUtils.getSubject();
			subject.logout();
		} catch (Exception e) {
			;
		}
		return this.redirect("/index.html", true);
	}
}
