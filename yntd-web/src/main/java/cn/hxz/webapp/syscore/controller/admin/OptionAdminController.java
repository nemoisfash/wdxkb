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

import cn.hxz.webapp.syscore.entity.Option;
import cn.hxz.webapp.syscore.entity.OptionGroup;
import cn.hxz.webapp.syscore.service.OptionGroupService;
import cn.hxz.webapp.syscore.service.OptionService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

@Controller
@RequestMapping("${adminPath}/common/option")
public class OptionAdminController extends BaseWorkbenchController {

	@Autowired
	private OptionService bizOption;
	@Autowired
	private OptionGroupService bizOptionGroup;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap modelMap,
			@RequestParam(value = "t", required = false) Long groupId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "12") int size) {

		Page<OptionGroup> groups = bizOptionGroup.find(true, new PageRequest(1,30));
		OptionGroup group = null;
		if (groupId!=null){
			group = bizOptionGroup.load(groupId);
		} else {
			if (groups.getTotalElements() > 0)
				group = groups.getContent().get(0);
			groupId = group.getId();
		}
		
		if (groupId!=null){
			modelMap.put("groupId", groupId);
		}

		Page<Option> entities = bizOption.find(groupId, null, new PageRequest(page, size));

		modelMap.put("groups", groups);
		modelMap.put("entities", entities);
		return this.view("/common/option/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,
			@RequestParam(value = "t", required = false) Long groupId,
			@RequestParam(value = "id", required = false) Long id) {

		if (groupId!=null){
			modelMap.put("groupId", groupId);
		}

		Option entity = null;
		if (id == null) {
			entity = new Option();
			if (groupId!=null)
				entity.setGroupId(groupId);
		} else {
			entity = bizOption.load(id);
		}

		modelMap.put("entity", entity);
		return this.view("/common/option/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ModelMap modelMap,
			@Valid @ModelAttribute("entity") Option entity, BindingResult binding) {

		if (binding.hasErrors()) {
			if (entity.getGroupId()!=null){
				modelMap.put("groupId", entity.getGroupId());
			}
			modelMap.put("entity", entity);
			return this.view("/common/option/edit");
		}

		if (entity.getId() == null)
			bizOption.create(entity);
		else
			bizOption.update(entity);

		Long groupId = entity.getGroupId();

		return this.redirect("/common/option/list.html" + params(groupId, null));
	}

	@RequestMapping(value="/enable/{id}", method = RequestMethod.GET)
	public String enable(
			@RequestParam(value = "t", required = false) Long groupId,
			@PathVariable("id") Long id,
			@RequestParam(value="curr", defaultValue="false") boolean curr) {

		bizOption.enable(id, !curr);
		
		return this.redirect("/common/option/list.html" + params(groupId, null));
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(
			@RequestParam(value = "t", required = false) Long groupId,
			@PathVariable("id") Long id) {

		bizOption.remove(id);

		return this.redirect("/common/option/list.html" + params(groupId, null));
	}

	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public String removeBatch(ModelMap modelmap, Long[] ids,
			@RequestParam(value = "t", required = false) Long groupId
				){
		     for(Long id : ids){
		    	 bizOption.remove(id);
		     }
				return this.redirect("/common/option/list.html" + params(groupId, null));
	}
	
	private String params(Long groupId, Long id){
		StringBuffer sb = new StringBuffer();
		if (groupId!=null)
			sb.append("&t=").append(groupId);
		if (id!=null)
			sb.append("&id=").append(id);
		String params = sb.toString();
		return (params==null || params.trim().length()<1) ? "" : ("?"+params.substring(1));
	}
}
