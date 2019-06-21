package cn.hxz.webapp.syscore.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.util.FiltersUtils;

@RestController
@RequestMapping("/api")
public class FilterCriteriaPortalRestController extends BaseWorkbenchController {
	
	
	@RequestMapping(value = {"/filter/update"}, method = RequestMethod.POST)
	public Object ajaxUpdate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "u") String uuid,
			@RequestParam(value = "k") String key,
			@RequestParam(value = "v") String value,
			@RequestParam(value = "r", required=false) String redirect) {
		
		FiltersUtils.updateFilter(request, response, uuid, key, value);
		
		Map<String, Object> data = new HashMap<String, Object>();		
		data.put("success", true);
		if (redirect!=null)
			data.put("redirect", redirect);
		
		return data;
	}
	
	@RequestMapping(value = {"/filter/remove"}, method = RequestMethod.POST)
	public Object ajaxRemove(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "u") String uuid,
			@RequestParam(value = "k") String key,
			@RequestParam(value = "r", required=false) String redirect) {

		FiltersUtils.removeFilter(request, response, uuid, key);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", true);
		if (redirect!=null)
			data.put("redirect", redirect);
		
		return data;
	}
	
	@RequestMapping(value = { "/filters/clear" }, method = RequestMethod.POST)
	public Object clearFilter(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "u") String uuid) {
		FiltersUtils.clearFilters(request, response, uuid);
		return true;
	}
}
