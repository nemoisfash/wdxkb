package cn.hxz.webapp.content.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.content.entity.Article;
import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.service.ArticleService;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/news")
public class CmsNewsController extends BaseWorkbenchController {
	// private static final String uuid =
	// HashUtils.MD5(CmsNewsController.class.getName());

	@Value("${allowedUploadImage}")
	private String allowedUploadImage;

	@Autowired
	private ChannelService bizChannel;

	@Autowired
	private ArticleService bizNews;
	
	@RequestMapping(value = "/{code}/list", method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "code") String channelCode) {
		User user = new User();
		try {
			user = AuthorizingUtils.loadCurrentUser();
			if (user == null) {
				return this.view("/wap/login");
			}
		} catch (Exception e) {
			return this.view("/403");
		}
		String uuid = HashUtils.MD5(request.getRequestURI());
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);
		Page<Article> entities = bizNews.find(channel.getId(), filters, pageable);
		model.addAttribute("channel", channel);
		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		model.addAttribute("groupId", user.getGroupId());
		return this.view("/cms/news/list");
	}

	@RequestMapping(value = "/{code}/edit", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = false) Long id) {

		Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);

		Article entity = null;
		if (id == null) {
			entity = new Article();
			entity.setChannelId(channel.getId());
		} else {
			entity = bizNews.load(id);
		}

		model.addAttribute("entity", entity);
		model.addAttribute("channel", channel);
		return this.view("/cms/news/edit");
	}

	@RequestMapping(value = "/{code}/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, HttpServletRequest request,
			@PathVariable(value = "code") String channelCode, @Valid @ModelAttribute("entity") Article entity,
			BindingResult binding) {
		User user = AuthorizingUtils.loadCurrentUser();
		if (binding.hasErrors()) {
			Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);
			model.addAttribute("entity", entity);
			model.addAttribute("channel", channel);
			return this.view("/cms/news/edit");
		}
		if (user.getGroupId() == 4) {
			if (entity.getId() == null) {
				entity.setTrashed(false);
				entity.setEnabled(true);
				entity.setChecked(false);
				entity.setPromote(false);
				entity.setSticky(false);
				bizNews.create(entity);
			} else {
				entity.setUpdateBy(user.getId());
				entity.setUpdateTime(new Date());
				bizNews.updateSelective(entity);
			}
		} else {
			if ("tzgglb".equals(channelCode)) {
				if (entity.getId() == null) {
					entity.setTrashed(false);
					entity.setEnabled(true);
					entity.setChecked(false);
					entity.setPromote(false);
					entity.setSticky(false);
					entity.setVisits(0L);
					bizNews.create(entity);
				} else {
					entity.setUpdateBy(user.getId());
					entity.setUpdateTime(new Date());
					bizNews.updateSelective(entity);
				}
			} else {
				if (entity.getId() == null) {
					entity.setTrashed(false);
					entity.setEnabled(false);
					entity.setChecked(false);
					entity.setPromote(false);
					entity.setSticky(false);
					entity.setVisits(0L);
					bizNews.create(entity);
				} else {
					entity.setUpdateBy(user.getId());
					entity.setUpdateTime(new Date());
					bizNews.updateSelective(entity);
				}
			}
		}
		return this.redirect("/cms/news/{code}/list.html");
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/sticky", method = RequestMethod.POST)
	public Object sticky(@PathVariable(value = "code") String channelCode, @RequestParam(value = "id") Long id) {
		Article entity = bizNews.load(id);
		entity.setSticky(!entity.getSticky());
		bizNews.updateSelective(entity);
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/promote", method = RequestMethod.POST)
	public Object promote(@PathVariable(value = "code") String channelCode, @RequestParam(value = "id") Long id) {

		Article entity = bizNews.load(id);
		entity.setPromote(!entity.getPromote());
		bizNews.updateSelective(entity);

		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/enable", method = RequestMethod.POST)
	public Object enable(@PathVariable(value = "code") String channelCode, @RequestParam(value = "id") Long id) {
		Article entity = bizNews.load(id);
		if ("tzgg".equals(channelCode)) {
			entity.setEnabled(!entity.getEnabled());
			bizNews.updateSelective(entity);
		} else {
			List<Article> entities = bizNews.findArticleByCIdAndUId(entity.getChannelId(), entity.getCanteenId());
			for (Article art : entities) {
				if (art.getEnabled()) {
					art.setEnabled(false);
					bizNews.updateSelective(art);
				}
			}
			entity.setEnabled(!entity.getEnabled());
			bizNews.updateSelective(entity);
		}
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/remove", method = RequestMethod.POST)
	public Object remove(@PathVariable(value = "code") String channelCode, @RequestParam(value = "id") Long id) {

		bizNews.remove(id);

		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/remove/batch", method = RequestMethod.POST)
	public Object removeBentch(@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "ids", required = true) Long[] ids) {

		for (Long id : ids) {
			bizNews.remove(id);
		}

		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/uploadImage", method = RequestMethod.POST)
	private Object uploadFile(HttpServletRequest request, @RequestParam(value = "image") MultipartFile multipartFile)
			throws UploadException {
		Map<String, Object> map = new HashMap<>();
		String imageUri = null;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String typeFolder = "/news";
				String filename = multipartFile.getOriginalFilename();
				Date now = new Date();
				String extension = FilenameUtils.getExtension(filename).toLowerCase();
				byte[] imageData = multipartFile.getBytes();
				imageUri = UploadUtils.uploadFile(realPath, site, typeFolder, now, extension, imageData, false);
				map.put("success", true);
				map.put("imageUri", imageUri);
			} catch (FileNotFoundException e) {
				if (logger.isDebugEnabled()) {
					map.put("success", false);
					map.put("message", "上传失败");
					e.printStackTrace();
				}
				throw new UploadException("图片上传失败", e);
			} catch (IOException e) {
				if (logger.isDebugEnabled()) {
					map.put("success", false);
					map.put("message", "上传失败");
					e.printStackTrace();
				}
				throw new UploadException("图片上传失败", e);
			}
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/deleteImage", method = RequestMethod.POST)
	public Object deleteFile(HttpServletRequest request, @RequestParam("fileUri") String fileUri) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", UploadUtils.deleteFile(request, fileUri));
		return map;
	}
}
