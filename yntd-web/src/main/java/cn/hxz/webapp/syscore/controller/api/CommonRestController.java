package cn.hxz.webapp.syscore.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import net.chenke.playweb.support.spring.BaseMultiActionController;

@RestController
@RequestMapping("/api")
public class CommonRestController extends BaseMultiActionController {
	
	@Autowired
	private UserSerivce bizUser;

	@RequestMapping(value = "/exist/account", method = RequestMethod.POST)
	public Object existAccount(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = bizUser.load(username);
		
		map.put("success", (user!=null));
		
		return map;
	}
	
	@RequestMapping(value = "/exist/email", method = RequestMethod.POST)
	public Object existEmail(String email) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = bizUser.loadByUsernameOrEmail(email);
		
		map.put("success", (user!=null));
		
		return map;
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/exist/accounts", method = RequestMethod.POST)
//	public Object existAccounts(String username) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		User user = bizUser.load(username);
//		
//		map.put("success", (user!=null));
//		
//		return map;
//	}
	


}
