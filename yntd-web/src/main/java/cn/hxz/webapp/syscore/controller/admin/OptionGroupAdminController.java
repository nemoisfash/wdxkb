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

import cn.hxz.webapp.syscore.entity.OptionGroup;
import cn.hxz.webapp.syscore.service.OptionGroupService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

@Controller
@RequestMapping("${adminPath}/common/optiongroup")
public class OptionGroupAdminController extends BaseWorkbenchController {

	@Autowired
	private OptionGroupService bizOptionGroup;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap modelMap,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "12") int size) {


		Page<OptionGroup> entities = bizOptionGroup.find(null, new PageRequest(page, size));
		
		modelMap.put("entities", entities);
		return this.view("/common/optiongroup/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,
			@RequestParam(value = "id", required = false) Long id) {


		OptionGroup entity = null;
		if (id == null) {
			entity = new OptionGroup();
		} else {
			entity = bizOptionGroup.load(id);
		}

		modelMap.put("entity", entity);
		return this.view("/common/optiongroup/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ModelMap modelMap,
			@Valid @ModelAttribute("entity") OptionGroup entity, BindingResult binding) {

		if (binding.hasErrors()) {
			modelMap.put("entity", entity);
			return this.view("/common/optiongroup/edit");
		}

		if (entity.getId() == null)
			bizOptionGroup.create(entity);
		else
			bizOptionGroup.update(entity);


		return this.redirect("/common/optiongroup/list.html");
	}

	@RequestMapping(value="/enable/{id}", method = RequestMethod.GET)
	public String enable(
			@PathVariable("id") Long id,
			@RequestParam(value="curr", defaultValue="false") boolean curr) {

		bizOptionGroup.enable(id, !curr);
		
		return this.redirect("/common/optiongroup/list.html");
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(
			@PathVariable("id") Long id) {

		bizOptionGroup.remove(id);

		return this.redirect("/common/optiongroup/list.html");
	}
	
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public String removeBatch(ModelMap modelmap, Long[] ids
				){
		     for(Long id : ids){
		    	 
		    	 bizOptionGroup.remove(id);
		     }
			return this.redirect("/common/optiongroup/list.html");
	}


}
