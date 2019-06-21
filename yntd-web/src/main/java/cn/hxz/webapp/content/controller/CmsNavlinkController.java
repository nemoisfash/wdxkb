package cn.hxz.webapp.content.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.content.entity.Navlink;
import cn.hxz.webapp.content.entity.NavlinkType;
import cn.hxz.webapp.content.service.CmsNavlinkService;
import cn.hxz.webapp.content.service.CmsNavlinkTypeService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/navlink")
public class CmsNavlinkController extends BaseWorkbenchController {

	@Autowired
	private CmsNavlinkService bizNavlink;
	@Autowired
	private CmsNavlinkTypeService bizNavlinkType;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String none() {
		List<NavlinkType> types = bizNavlinkType.find(this.getSite().getId());
		if (types.size() == 0) {
			return this.view("/cms/navlink/none");
		} else {
			String code = types.get(0).getCode();
			return this.redirect(String.format("/cms/navlink/%s/list.html", code));
		}
	}
	
	@RequestMapping(value = "/{code}/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "code") String typeCode,
			@RequestParam(value = "p", required = false) Long parentId) {

		NavlinkType type = bizNavlinkType.load(this.getSite().getId(), typeCode);
		
		String uuid = HashUtils.MD5(request.getRequestURI());
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);

		Page<Navlink> entities = bizNavlink.findAll(type.getId(), parentId, pageable);

		if (parentId!=null){
			Navlink parent = bizNavlink.load(parentId);
			model.addAttribute("parent", parent);
		}

		List<NavlinkType> types = bizNavlinkType.find(this.getSite().getId());
		
		model.addAttribute("type", type);
		model.addAttribute("types", types);
		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/navlink/list");
	}

	@RequestMapping(value = "/{code}/edit", method = RequestMethod.GET)
	public String edit(Model model,
			@PathVariable(value = "code") String typeCode,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "p", required = false) Long parentId) {

		NavlinkType type = bizNavlinkType.load(this.getSite().getId(), typeCode);
		
		if (parentId!=null){
			Navlink parent = bizNavlink.load(parentId);
			model.addAttribute("parent", parent);
		}
		
		Navlink entity = null;
		if (id == null) {
			entity = new Navlink();
			if (parentId!=null)
				entity.setParentId(parentId);
			entity.setTypeId(type.getId());
		} else {
			entity = bizNavlink.load(id);
		}
		
		if (entity.getParentId()!=null){
			Navlink parent = bizNavlink.load(entity.getParentId());
			model.addAttribute("parent", parent);
		}

		model.addAttribute("type", type);
		model.addAttribute("entity", entity);
		return this.view("/cms/navlink/edit");
	}

	@RequestMapping(value = "/{code}/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model,
			@PathVariable(value = "code") String typeCode,
			@Valid @ModelAttribute("entity") Navlink entity, String logoUri,BindingResult binding) {

		if (binding.hasErrors()) {
			if (entity.getParentId()!=null){
				Navlink parent = bizNavlink.load(entity.getParentId());
				model.addAttribute("parent", parent);
			}
			model.addAttribute("entity", entity);
			return this.view("/cms/navlink/edit");
		}

		if (entity.getId() == null)
			bizNavlink.create(entity);
		else
			bizNavlink.update(entity);

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/navlink/{code}/list.html" + params(entity.getParentId()));
	}
	
	@ResponseBody
	@RequestMapping(value = "/{code}/delete", method = RequestMethod.POST)
	public Object delete(@RequestParam(value = "id") Long id) {
		boolean success = bizNavlink.delete(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id") Long id) {
		boolean success = bizNavlink.enable(id);
		return new JsonResult(success);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{code}/delete/batch", method = RequestMethod.POST)
	public Object deleteBatch(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "ids", required = true) Long[] ids){
	     for(Long id : ids){
	    	 bizNavlink.delete(id);
	     }
	     return new JsonResult(true);
	}

	private String params(Long parentId) {
		StringBuffer sb = new StringBuffer();
		if (parentId != null)
			sb.append("&p=").append(parentId);
		String params = sb.toString();
		return (params == null || params.trim().length() < 1) ? "" : ("?" + params.substring(1));
	}
}
