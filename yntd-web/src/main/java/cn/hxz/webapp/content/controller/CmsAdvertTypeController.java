package cn.hxz.webapp.content.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.service.AdvertGroupService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/adverttype")
public class CmsAdvertTypeController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(CmsAdvertTypeController.class.getName());

	@Autowired
	private AdvertGroupService bizAdvertType;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,	HttpServletResponse response) {
		
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		
		Page<AdvertGroup> entities = bizAdvertType.findAll(getSite().getId(), null, pageable);
		
		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/adverttype/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, 
			@RequestParam(value = "id", required = false) Long id) {

		AdvertGroup entity = null;
		if (id == null) {
			entity = new AdvertGroup();
			entity.setSiteId(getSite().getId());
		} else {
			entity = bizAdvertType.load(id);
		}

		model.addAttribute("entity", entity);
		return this.view("/cms/adverttype/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, 
			@Valid @ModelAttribute("entity") AdvertGroup entity, BindingResult binding) {

		if (binding.hasErrors()) {
			model.addAttribute("entity", entity);
			return this.view("/cms/adverttype/edit");
		}

		if (entity.getId() == null) {
			entity.setTrashed(false);
			bizAdvertType.create(entity);
		} else {
			bizAdvertType.update(entity);
		}

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/adverttype/list.html");
	}

	@ResponseBody
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id", required = true) Long id) {

		bizAdvertType.enable(id);

		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object remove(@RequestParam(value = "id", required = true) Long id) {

		bizAdvertType.remove(id);

		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		for (Long id : ids) {
			bizAdvertType.remove(id);
		}
		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequestMapping(value = "/unique/code", method = RequestMethod.POST)
	public Object uniqueCode(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String code) {
		
		AdvertGroup entity = bizAdvertType.load(this.getSite().getId(), code);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}
}
