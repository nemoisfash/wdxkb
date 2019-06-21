package cn.hxz.webapp.syscore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.JsonResult;

/**
 * 
 * @author chenke
 *
 */
@RestController
@RequestMapping("${adminPath}/cache")
public class CacheRestController {

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public Object clearCache(String name) {
		boolean success = false;
		String message = "未找到对应缓存";
		if ("sys".equalsIgnoreCase(name)){
			CacheUtils.clear(CacheUtils.CACHE_SYS);
			success = true;
			message = "系统缓存已清理";
		}
		if ("cms".equalsIgnoreCase(name)){
			CacheUtils.clear(CacheUtils.CACHE_CMS);
			success = true;
			message = "内容缓存已清理";
		}
		if ("app".equalsIgnoreCase(name)){
			CacheUtils.clear(CacheUtils.CACHE_EXT);
			success = true;
			message = "应用缓存已清理";
		}
		return new JsonResult(success, message);
	}

}
