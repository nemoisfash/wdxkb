package cn.hxz.webapp.syscore.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import cn.hxz.webapp.syscore.entity.Manager;
import cn.hxz.webapp.syscore.entity.Role;
import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.GroupSerivce;
import cn.hxz.webapp.syscore.service.ManagerSerivce;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.TreeNode;
import net.chenke.playweb.TreeState;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/sys/user")
public class SysUserController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(SysUserController.class.getName());

	@Autowired
	private ManagerSerivce bizManager;
	@Autowired
	private GroupSerivce bizGroup;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,	HttpServletResponse response) {

		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		
		Page<Map<String, Object>> entities = bizManager.findAll(filters, pageable);

		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/sys/user/list");
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String load(Model model, 
			@RequestParam(value = "id", required = false) Long id) {

		List<Role> roles = new ArrayList<Role>();
		Manager entity = null;
		if (id == null) {
			entity = new Manager();
			entity.setAccount(new User());
		} else {
			roles = bizManager.findConnectedRoles(id);
			entity = bizManager.load(id);
		}

		model.addAttribute("roles", roles);
		model.addAttribute("entity", entity);
		return this.view("/sys/user/edit");
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, 
			@Valid @ModelAttribute("entity") Manager entity, BindingResult binding,
			@RequestParam(value = "roleIds", required = false) Long[] roleIds) {
		
		if (binding.hasErrors()) {
			if (entity.getId()!=null){
				List<Role> roles = bizManager.findConnectedRoles(entity.getId());
				model.addAttribute("roles", roles);
			}
			model.addAttribute("entity", entity);
			return this.view("/cms/user/edit");
		}

		if (entity.getId() == null) {
			entity.getAccount().setEnabled(true);
			entity.getAccount().setTrashed(false);
			entity.getAccount().setPassword("000000");//添加管理员默认密码
			bizManager.create(entity);
		} else {
			bizManager.update(entity);
		}
		
		if (entity.getId()!=null && roleIds!=null && roleIds.length > 0)
			bizManager.connectRoles(entity.getId(), roleIds);

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/sys/user/list.html");
	}

	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object remove(@RequestParam(value = "id", required = true) Long id) {
		boolean success = bizManager.remove(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestParam(value = "id", required = true) Long id) {
		boolean success = bizManager.remove(id);
		return new JsonResult(success);
	}

	@ResponseBody
	@RequestMapping(value="/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id", required = true) Long id) {
		bizManager.enable(id);
		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequiresPermissions(value="sys:admin:remove:batch")
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		for (Long id : ids) {
			bizManager.remove(id);
		}
		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequiresPermissions(value="sys:admin:delete:batch")
	@RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
	public Object deleteBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		for (Long id : ids) {
			bizManager.delete(id);
		}
		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequestMapping(value = "/unique/username", method = RequestMethod.POST)
	public Object uniqueUsername(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String username) {
		
		Manager entity = bizManager.load(username);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/unique/email", method = RequestMethod.POST)
	public Object uniqueEmail(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String email) {
		
		Manager entity = bizManager.loadByUsernameOrEmail(email);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/group/tree", method = RequestMethod.POST)
	public Object tree(@RequestParam(value = "currIds", required = false) Long currIds) {
		List<TreeNode> tree = new ArrayList<TreeNode>();
		List<Group> entities = bizGroup.find();
		List<Long> _currIds = (currIds==null) ? null : Arrays.asList(currIds);
		for (Group entity : entities){
			TreeState state = new TreeState();
			state.setSelected(_currIds!=null && _currIds.contains(entity.getId()));
			TreeNode node = new TreeNode();
			node.setId(Objects.toString(entity.getId(), ""));
			node.setParent(Objects.toString(entity.getParentId(), "#"));
			node.setText(entity.getName());
			node.setState(state);
			tree.add(node);
		}
		return tree;
	}
}
