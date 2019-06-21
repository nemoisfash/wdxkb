package cn.hxz.webapp.syscore.controller;

import java.util.List;
import java.util.Map;

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

import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.service.RoleSerivce;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/sys/role")
public class SysRoleController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(SysRoleController.class.getName());

	@Autowired
	private RoleSerivce bizRole;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,	HttpServletResponse response) {

		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);

		Page<Role> entities = bizRole.findAll(filters, pageable);

		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/sys/role/list");
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, 
			@RequestParam(value = "id", required = false) Long id) {

		Role entity = null;
		if (id == null) {
			entity = new Role();
		} else {
			entity = bizRole.load(id);
			List<Long> currIds = bizRole.findPermissionIds(id);
			model.addAttribute("currIds", currIds);
		}
		
		model.addAttribute("entity", entity);
		return this.view("/sys/role/edit");
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, 
			@Valid @ModelAttribute("entity") Role entity, BindingResult binding,
			@RequestParam(value = "permissionIds", required = false) Long[] permissionIds) {

		if (binding.hasErrors()) {
			model.addAttribute("entity", entity);
			return this.view("/sys/role/edit");
		}

		if (entity.getId() == null) {
			bizRole.create(entity);
		} else {
			bizRole.update(entity);
		}

		if (entity != null && permissionIds!=null && permissionIds.length > 0)
			bizRole.connectPermissions(entity.getId(), permissionIds);

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/sys/role/list.html");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestParam(value = "id") Long id) {
		boolean success = bizRole.delete(id);
		return new JsonResult(success);
	}
	
	@ResponseBody
	@RequestMapping(value = "/unique/code", method = RequestMethod.POST)
	public Object uniqueCode(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String code) {
		
		Role entity = bizRole.load(code);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}

	@RequestMapping(value = "/groups/{id}", method = RequestMethod.GET)
	public String groups(Model model,
			@PathVariable(value = "id") Long id) {
		List<Map<String, Object>> items = bizRole.findGroups(id);
		model.addAttribute("items", items);
		return this.view("/sys/role/groups");
	}

	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
	public String accounts(Model model,
			@PathVariable(value = "id") Long id) {
		List<Map<String, Object>> items = bizRole.findAccounts(id);
		model.addAttribute("items", items);
		return this.view("/sys/role/accounts");
	}
}
