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

import cn.hxz.webapp.content.entity.NavlinkType;
import cn.hxz.webapp.content.service.CmsNavlinkTypeService;
import cn.hxz.webapp.syscore.controller.SysRoleController;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/navlinktype")
public class CmsNavlinkTypeController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(SysRoleController.class.getName());
	
	@Autowired
	private CmsNavlinkTypeService bizNavlinkType;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,	HttpServletResponse response) {

		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);

		Page<NavlinkType> entities = bizNavlinkType.findAll(this.getSite().getId(), pageable);

		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/navlinktype/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model,
			@RequestParam(value = "id", required = false) Long id) {

		NavlinkType entity = null;
		if (id == null) {
			entity = new NavlinkType();
			entity.setSiteId(this.getSite().getId());
		} else {
			entity = bizNavlinkType.load(id);
		}

		model.addAttribute("entity", entity);
		return this.view("/cms/navlinktype/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model,
			@Valid @ModelAttribute("entity") NavlinkType entity, BindingResult binding) {

		if (binding.hasErrors()) {
			model.addAttribute("entity", entity);
			return this.view("/cms/navlinktype/edit");
		}

		if (entity.getId() == null)
			bizNavlinkType.create(entity);
		else
			bizNavlinkType.update(entity);

		return this.redirect("/cms/navlinktype/list.html");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestParam(value = "id") Long id) {
		boolean success = bizNavlinkType.delete(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequestMapping(value="/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id") Long id) {
		boolean success = bizNavlinkType.enable(id);
		return new JsonResult(success);
	}
	
	@ResponseBody
	@RequestMapping(value = "/unique/code", method = RequestMethod.POST)
	public Object uniqueCode(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String code) {
		
		NavlinkType entity = bizNavlinkType.load(this.getSite().getId(), code);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}
}
