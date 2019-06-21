package cn.hxz.webapp.content.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.RelatedLink;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.RelatedLinkService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/link")
public class CmsLinkController extends BaseWorkbenchController {
	public final static String uuid = HashUtils.MD5(CmsLinkController.class.getName());

	@Autowired
	private ChannelService bizChannel;
	@Autowired
	private RelatedLinkService bizLink;
	
	@RequestMapping(value = "/{code}/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(value = "code") String channelCode) {
		
		String uuid = HashUtils.MD5(request.getRequestURI());
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);

		Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);

		Page<RelatedLink> entities = bizLink.findAll(channel.getId(), pageable);
		
		model.addAttribute("channel", channel);
		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/link/list");
	}

	@RequestMapping(value = "/{code}/edit", method = RequestMethod.GET)
	public String edit(Model model,
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = false) Long id) {

		Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);

		RelatedLink entity = null;
		if (id == null) {
			entity = new RelatedLink();
			entity.setChannelId(channel.getId());
		} else {
			entity = bizLink.load(id);
		}

		model.addAttribute("channel", channel);
		model.addAttribute("entity", entity);
		return this.view("/cms/link/edit");
	}

	@RequestMapping(value = "/{code}/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model,
			HttpServletRequest request,
			@PathVariable(value = "code") String channelCode,
			@Valid @ModelAttribute("entity") RelatedLink entity, String logoUri,BindingResult binding) {

		if (binding.hasErrors()) {
			if (entity.getChannelId()!=null){
				Channel channel = bizChannel.load(entity.getChannelId());
				model.addAttribute("channel", channel);
			}
			model.addAttribute("entity", entity);
			return this.view("/cms/link/edit");
		}
		
		if (entity.getId() == null) {
			if (StringUtils.hasText(logoUri)) {
				entity.setLogo(logoUri);
			}
			bizLink.create(entity);
		} else {
			if (StringUtils.hasText(logoUri)) {
				RelatedLink persisted = bizLink.load(entity.getId());
				if (StringUtils.hasText(persisted.getLogo()))
					UploadUtils.deleteFile(request, persisted.getLogo());
				entity.setLogo(logoUri);
			}
			bizLink.update(entity);
		}
		
		if (StringUtils.hasText(logoUri)) {
			entity.setLogo(logoUri);
		}
		
		if (entity.getId() == null)
			bizLink.create(entity);
		else
			bizLink.update(entity);

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/link/{code}/list.html");
	}

	@ResponseBody
	@RequestMapping(value="/{code}/enable", method = RequestMethod.POST)
	public Object enable(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = true) Long id) {

		RelatedLink entity = bizLink.load(id);
		entity.setEnabled(!entity.getEnabled());
		bizLink.update(entity);
		
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/remove", method = RequestMethod.POST)
	public Object remove(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = true) Long id) {

		bizLink.remove(id);

		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{code}/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "ids", required = true) Long[] ids){
		
	     for(Long id : ids){
	    	 bizLink.remove(id);
	     }
	     
	     return new JsonResult(true);
	}
}
