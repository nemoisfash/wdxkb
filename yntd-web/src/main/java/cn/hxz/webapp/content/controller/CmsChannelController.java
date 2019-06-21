package cn.hxz.webapp.content.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/channel")
public class CmsChannelController extends BaseWorkbenchController {
//	public final static String uuid = HashUtils.MD5(CmsChannelController.class.getName());
	
	@Autowired
	private ChannelService bizChannel;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "p", required = false) Long parentId) {
		
		String uuid = HashUtils.MD5(request.getRequestURI() + Objects.toString(parentId, ""));
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		
		if (parentId != null) {
			Channel parent = bizChannel.load(parentId);
			model.addAttribute("parent", parent);
		}

		Page<Channel> entities = bizChannel.find(this.getSite().getId(), parentId, pageable);

		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/channel/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, 
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "p", required = false) Long parentId) {

		if (parentId != null) {
			Channel parent = bizChannel.load(parentId);
			model.addAttribute("parent", parent);
		}

		Channel entity = null;
		if (id == null) {
			entity = new Channel();
			if (parentId != null)
				entity.setParentId(parentId);
		} else {
			entity = bizChannel.load(id);
		}

		model.addAttribute("entity", entity);
		return this.view("/cms/channel/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, 
			HttpServletRequest request,
			@Valid @ModelAttribute("entity") Channel entity, BindingResult binding,
			@RequestParam(value = "logoUri", required = false) String logoUri) {

		if (binding.hasErrors()) {
			if (entity.getParentId() != null) {
				Channel parent = bizChannel.load(entity.getParentId());
				model.addAttribute("parent", parent);
			}
			model.addAttribute("entity", entity);
			return this.view("/cms/channel/edit");
		}
		if (entity.getId() == null) {
			if (StringUtils.hasText(logoUri)) {
				entity.setLogo(logoUri);
			}
			entity.setSiteId(this.getSite().getId());
			bizChannel.create(entity);
		} else {
			if (StringUtils.hasText(logoUri)) {
				Channel persisted = bizChannel.load(entity.getId());
				if (StringUtils.hasText(persisted.getLogo()))
					UploadUtils.deleteFile(request, persisted.getLogo());
				entity.setLogo(logoUri);
			}
			bizChannel.update(entity);
		}
		
		model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/channel/list.html" + params(entity.getParentId()));
	}

	@ResponseBody
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	public Object enable(@RequestParam(value = "id", required = true) Long id) {

		Channel entity = bizChannel.load(id);
		entity.setEnabled(!entity.getEnabled());
		bizChannel.update(entity);
		
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object remove(@RequestParam(value = "id", required = true) Long id) {

		bizChannel.remove(id);
		
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(@RequestParam(value = "ids", required = true) Long[] ids) {
		
		for (Long id : ids) {
			bizChannel.remove(id);
		}
		
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/unique/code", method = RequestMethod.POST)
	public Object uniqueCode(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "code", required = true) String code) {
		
		Channel entity = bizChannel.load(this.getSite().getId(), code);
		JsonResult data = null;
		if (entity==null || (id!=null && id.equals(entity.getId())))
			data = new JsonResult(true);
		else
			data = new JsonResult(false);
		return data;
	}

	private String params(Long parentId) {
		StringBuffer sb = new StringBuffer();
		if (parentId != null)
			sb.append("&p=").append(parentId);
		String params = sb.toString();
		return (params == null || params.trim().length() < 1) ? "" : ("?" + params.substring(1));
	}
}
