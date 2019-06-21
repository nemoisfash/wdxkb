package cn.hxz.webapp.syscore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.hxz.webapp.syscore.entity.Group;
import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.service.GroupSerivce;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/sys/group")
public class SysGroupController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(SysGroupController.class.getName());

	@Autowired
	private GroupSerivce bizGroup;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "p", required = false) Long parentId) {
		
		String uuid = HashUtils.MD5(request.getRequestURI() + Objects.toString(parentId, ""));
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		
		if (parentId != null) {
			Group parent = bizGroup.load(parentId);
			model.addAttribute("parent", parent);
		}

		Page<Group> entities = bizGroup.findAll(parentId, pageable);

		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/sys/group/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, 
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "p", required = false) Long parentId) {

		if (parentId != null) {
			Group parent = bizGroup.load(parentId);
			model.addAttribute("parent", parent);
		}

		List<Role> roles = new ArrayList<Role>();
		Group entity = null;
		if (id == null) {
			entity = new Group();
			if (parentId != null)
				entity.setParentId(parentId);
		} else {
			roles = bizGroup.findConnectedRoles(id);
			entity = bizGroup.load(id);
		}

		model.addAttribute("roles", roles);
		model.addAttribute("entity", entity);
		return this.view("/sys/group/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, 
			@Valid @ModelAttribute("entity") Group entity, BindingResult binding,
			@RequestParam(value = "roleIds", required = false) Long[] roleIds) {

		if (binding.hasErrors()) {
			if (entity.getParentId() != null) {
				Group parent = bizGroup.load(entity.getParentId());
				model.addAttribute("parent", parent);
			}
			if (entity.getId()!=null){
				List<Role> roles = bizGroup.findConnectedRoles(entity.getId());
				model.addAttribute("roles", roles);
			}
			model.addAttribute("entity", entity);
			return this.view("/sys/group/edit");
		}
		if (entity.getId() == null) {
			bizGroup.create(entity);
		} else {
			bizGroup.update(entity);
		}
		
		if (entity.getId()!=null && roleIds!=null && roleIds.length > 0)
			bizGroup.connectRoles(entity.getId(), roleIds);
		
		model.addFlashAttribute("message", "已保存");
		return this.redirect("/sys/group/list.html" + params(entity.getParentId()));
	}
	
	private String params(Long parentId) {
		StringBuffer sb = new StringBuffer();
		if (parentId != null)
			sb.append("&p=").append(parentId);
		String params = sb.toString();
		return (params == null || params.trim().length() < 1) ? "" : ("?" + params.substring(1));
	}

	@ResponseBody
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id", required = true) Long id) {
		boolean success = bizGroup.enable(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object remove(@RequestParam(value = "id", required = true) Long id) {
		boolean success = bizGroup.remove(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequiresPermissions(value="sys:group:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestParam(value = "id", required = true) Long id) {
		boolean success = bizGroup.delete(id);
		return new JsonResult(success);
	}
	
	@ResponseBody
	@RequiresPermissions(value="sys:group:remove:batch")
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		for (Long id : ids) {
			bizGroup.remove(id);
		}
		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequiresPermissions(value="sys:group:delete:batch")
	@RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
	public Object deleteBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		for (Long id : ids) {
			bizGroup.delete(id);
		}
		return new JsonResult(true);
	}
}
