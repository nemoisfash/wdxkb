package cn.hxz.webapp.content.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hxz.webapp.content.entity.Article;
import cn.hxz.webapp.content.entity.BuiltinModelEnum;
import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.SinglesPage;
import cn.hxz.webapp.content.service.ArticleService;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.SinglesPageService;
import cn.hxz.webapp.syscore.support.BasePortalController;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.ResourceNotFoundException;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
public class ContentPortalController extends BasePortalController {

	@Autowired
	private ChannelService bizChannel;
	@Autowired
	private ArticleService bizArticle;
	@Autowired
	private SinglesPageService bizSinglesPage;

	private static final String modulePath = HashUtils.MD5(ContentPortalController.class.getName());

	/**
	 * 获取栏目页
	 * 
	 * @param modelMap
	 * @param skin
	 * @param node
	 * @param page
	 * @return
	 */
	@RequestMapping(value = { "/node/{node}", "/node/{node}/", "/node/{node}/index",
			"/wap/node/{node}" }, method = RequestMethod.GET)
	public String node(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,
			@PathVariable String node, @RequestParam(value = "skin", required = false) String skin) {
		String uuid = HashUtils.MD5(String.format(modulePath, "/list"));
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		Channel channel = bizChannel.loadCached(getSite().getId(), node);
		if (channel == null || !(channel.getEnabled() != null && channel.getEnabled())) {
			throw new ResourceNotFoundException();
		}
		SinglesPage content = null;
		Boolean isSpecificModel = bizChannel.isSpecificModel(channel.getId(), BuiltinModelEnum.SinglesPAGE);
		if (isSpecificModel) {
			content = bizSinglesPage.loadByChannelId(channel.getId());
			if (content.getVisits() == null) {
				content.setVisits((long) 0);
			}
			content.setVisits(content.getVisits() + 1);
			bizSinglesPage.update(content);
		}
		modelMap.addAttribute("node", node);
		modelMap.addAttribute("channel", channel);
		modelMap.addAttribute("content", content);

		if (isSpecificModel) {
			return this.view(skin + "/aboutme");
		}
		modelMap.addAttribute("page", pageable.getPageNumber());
		modelMap.addAttribute("filters", filters);
		String view = skin + "/list";
		if (node.equalsIgnoreCase("tzgglb") && skin.equalsIgnoreCase("wap")) {
			view = skin + "/tzgg";
		}
		return this.view(view);
	}

	/**
	 * 获取内容页
	 * 
	 * @param modelMap
	 * @param skin
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value = { "/article/{id}" }, method = RequestMethod.GET)
	public String article(ModelMap modelMap, HttpServletRequest request, @PathVariable Long id,
			@RequestParam(value = "skin", required = false) Integer skin) {
		Article content = bizArticle.load(id);
		if (content.getHits() == null) {
			content.setVisits((long) 0);
		}
		content.setVisits(content.getHits() + 1);
		bizArticle.updateSelective(content);

		if (content == null || !(content.getEnabled() != null && content.getEnabled())) {
			throw new ResourceNotFoundException();
		}

		Channel channel = bizChannel.loadCached(content.getChannelId());
		if (channel == null || !(channel.getEnabled() != null && channel.getEnabled())) {
			throw new ResourceNotFoundException();
		}

		String node = channel.getNode();
		modelMap.addAttribute("node", node);
		modelMap.addAttribute("content", content);
		modelMap.addAttribute("channel", channel);
		if (skin != null) {
			return this.view("/www/item");
		}
		return this.view(channel.getTplContent());
	}

}
