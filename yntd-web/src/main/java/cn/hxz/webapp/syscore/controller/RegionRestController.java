package cn.hxz.webapp.syscore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hxz.webapp.syscore.entity.Region;
import cn.hxz.webapp.syscore.service.RegionService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author cn.feeboo
 * 
 */
@Controller
@RequestMapping("/api")
public class RegionRestController extends BaseWorkbenchController {
	
	@Autowired
	private RegionService bizRegion;

	@ResponseBody
	@RequestMapping(value = {"/options/region"}, method = RequestMethod.POST)
	public Object ajaxOptionsRegion(@RequestParam(value = "parentId", required=false) Long parentId) {
		
		Page<Region> entities = bizRegion.find(parentId, new PageRequest(1, 100));
		
		List<String[]> result = new ArrayList<String[]>();
		
		for(Region entity : entities){
			String[] data = new String[3];
			data[0] = entity.getId().toString();
			data[1] = entity.getName();
			data[2] = entity.getNameEn();
			result.add(data);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/region/{parentId}/children"}, method = RequestMethod.GET)
	public Object ajaxRegion(@PathVariable(value = "parentId") Long parentId) {
		
		Page<Region> entities = bizRegion.find(parentId, new PageRequest(1, 100));
		
		List<String[]> result = new ArrayList<String[]>();
		
		for(Region entity : entities){
			String[] data = new String[3];
			data[0] = entity.getId().toString();
			data[1] = entity.getName();
			data[2] = entity.getNameEn();
			result.add(data);
		}
		
		return result;
	}
}
