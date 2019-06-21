package cn.hxz.webapp.syscore.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hxz.webapp.syscore.entity.Region;
import cn.hxz.webapp.syscore.service.RegionService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

@Controller
@RequestMapping("${adminPath}/common/region")
public class RegionAdminController extends BaseWorkbenchController {

	@Autowired
	private RegionService bizRegion;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap modelMap,
			@RequestParam(value = "p", required = false) Long parentId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "12") int size) {
		
		Page<Region> entities = bizRegion.find(parentId, new PageRequest(page, size));

		if (parentId!=null){
			Region parent = bizRegion.load(parentId);
			modelMap.put("parent", parent);
		}
		
		modelMap.put("entities", entities);
		return this.view("/common/region/list");
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,
			@RequestParam(value = "p", required = false) Long parentId,
			@RequestParam(value = "id", required = false) Long id) {

		if (parentId!=null){
			Region parent = bizRegion.load(parentId);
			modelMap.put("parent", parent);
		}
		
		Region entity = null;
		if (id == null) {
			entity = new Region();
			if (parentId != null)
				entity.setParentId(parentId);
		} else {
			entity = bizRegion.load(id);
		}

		modelMap.put("entity", entity);
		return this.view("/common/region/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ModelMap modelMap,
			@Valid @ModelAttribute("entity") Region entity, BindingResult binding) {

		if (binding.hasErrors()) {
			if (entity.getParentId()!=null){
				Region parent = bizRegion.load(entity.getParentId());
				modelMap.put("parent", parent);
			}
			modelMap.put("entity", entity);
			return this.view("/common/region/edit");
		}

		if (entity.getId() == null)
			bizRegion.create(entity);
		else
			bizRegion.update(entity);

		return this.redirect("/common/region/list.html" + getParams(entity.getParentId()));
	}
	
	@RequestMapping(value="/enable/{id}", method = RequestMethod.GET)
	public String enable(
			@RequestParam(value = "p", required = false) Long parentId,
			@PathVariable("id") Long id,
			@RequestParam(value="curr", defaultValue="false") boolean curr) {

		bizRegion.enable(id, !curr);
		
		return this.redirect("/common/region/list.html" + getParams(parentId));
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(
			@RequestParam(value = "p", required = false) Long parentId,
			@PathVariable("id") Long id) {

		bizRegion.remove(id);

		return this.redirect("/common/region/list.html" + getParams(parentId));
	}
	
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public String removeBatch(ModelMap modelmap, Long[] ids,
			@RequestParam(value = "p", required = false) Long parentId
				){
		     for(Long id : ids){
		    	 
			     bizRegion.remove(id);
		     }
		     return this.redirect("/common/region/list.html" + getParams(parentId));
	}
	
	private String getParams(Object obj){
		return obj!=null ? "?p="+obj.toString() : "";
	}
}
