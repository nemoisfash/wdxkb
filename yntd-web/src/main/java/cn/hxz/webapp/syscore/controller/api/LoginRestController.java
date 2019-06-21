package cn.hxz.webapp.syscore.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import cn.hxz.webapp.syscore.support.BasePortalController;
import cn.hxz.webapp.util.ShiroUtils;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.util.HashUtils;

@RestController
@RequestMapping("/api")
public class LoginRestController extends BasePortalController {
	@Autowired
	private UserSerivce bizUser;
	/**
	 *  ajax 登录。只支持 POST 请求
	 * @param request
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = {"/login"}, method = RequestMethod.POST)
	public Object login(HttpServletRequest request,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "rememberMe", defaultValue="false") boolean rememberMe,
			@RequestParam(value = "captcha") String captcha) {
		
		if (!AuthorizingUtils.validCaptcha(captcha)){
			return new JsonResult(false, "验证码错误");
		}
		User account = bizUser.load(username);
		String credentials = HashUtils.MD5(password, account.getSalt());
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, credentials, rememberMe, ShiroUtils.getHost());
			ShiroUtils.login(token);
			return new JsonResult(true);
		} catch (AuthenticationException e) {
			return new JsonResult(true, e.getMessage());
		}
	}
	
	/**
	 * ajax 登出。支持 GET POST 请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/logout" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Object logout(HttpServletRequest request) {
		JsonResult data;
		try	{
			Subject subject = ShiroUtils.getSubject();
			subject.logout();
			data = new JsonResult(true);
		} catch(Exception e){
			data = new JsonResult(false);
		}
		return data;
	}
}
