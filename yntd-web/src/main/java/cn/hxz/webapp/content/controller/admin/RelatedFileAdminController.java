package cn.hxz.webapp.content.controller.admin;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.RelatedFile;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.RelatedFileService;
import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

@Controller
@RequestMapping("${adminPath}/content/relatedfile")
public class RelatedFileAdminController extends BaseWorkbenchController {

	@Autowired
	private RelatedFileService bizRelatedFile;
	@Autowired
	private ChannelService bizChannel;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap modelMap,
			@RequestParam(value = "n", required = true) Long channelId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "12") int size) {

		if (channelId!=null){
			Channel channel = bizChannel.load(channelId);
			modelMap.put("channel", channel);
		}

		Page<RelatedFile> entities = bizRelatedFile.find(channelId, new PageRequest(page, size));
		
		modelMap.put("entities", entities);
		return this.view("/content/relatedfile/list");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,
			@RequestParam(value = "n", required = true) Long channelId,
			@RequestParam(value = "id", required = false) Long id) {

		if (channelId!=null){
			Channel channel = bizChannel.load(channelId);
			modelMap.put("channel", channel);
		}

		RelatedFile entity = null;
		if (id == null) {
			entity = new RelatedFile();
			if (channelId != null)
				entity.setChannelId(channelId);
		} else {
			entity = bizRelatedFile.load(id);
		}

		modelMap.put("entity", entity);
		return this.view("/content/relatedfile/edit");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ModelMap modelMap, HttpServletRequest request,
			@Valid @ModelAttribute("entity") RelatedFile entity, BindingResult binding,
			@RequestParam(value="uploadfile", required=false) MultipartFile multipartFile) {

		if (binding.hasErrors()) {
			if (entity.getChannelId()!=null){
				Channel channel = bizChannel.load(entity.getChannelId());
				modelMap.put("channel", channel);
			}
			modelMap.put("entity", entity);
			return this.view("/content/relatedfile/edit");
		}
		
        if(multipartFile!=null && !multipartFile.isEmpty()) {
			String filename = uploadFile(request, multipartFile);
			// 当update时，保存新图后，删除旧图
			if (entity.getId()!=null){
				deleteFile(request, entity.getFilename());
			}
			entity.setFilename(filename);
        }

		if (entity.getId() == null)
			bizRelatedFile.create(entity);
		else
			bizRelatedFile.update(entity);

		Long channelId = entity.getChannelId();

		return this.redirect("/content/relatedfile/list.html" + params(channelId, null));
	}

	@RequestMapping(value="/enable/{id}", method = RequestMethod.GET)
	public String enable(
			@RequestParam(value = "n", required = true) Long channelId,
			@PathVariable("id") Long id,
			@RequestParam(value="curr", defaultValue="false") boolean curr) {

		bizRelatedFile.enable(id, !curr);
		
		return this.redirect("/content/relatedfile/list.html" + params(channelId, null));
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(
			@RequestParam(value = "n", required = true) Long channelId,
			@PathVariable("id") Long id) {

		bizRelatedFile.remove(id);

		return this.redirect("/content/relatedfile/list.html" + params(channelId, null));
	}
//	@RequestMapping(value = "/removeBatch/{id}", method = RequestMethod.GET)
	@RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
	public String removeBatch(
			@RequestParam(value = "n", required = true) Long channelId,
			Long[] ids) {
		 for(Long id : ids){
			 bizRelatedFile.remove(id);
		 }

		return this.redirect("/content/relatedfile/list.html" + params(channelId, null));
	}


	private String params(Long channelId, Long id){
		StringBuffer sb = new StringBuffer();
		if (channelId!=null)
			sb.append("&n=").append(channelId);
		if (id!=null)
			sb.append("&id=").append(id);
		String params = sb.toString();
		return (params==null || params.trim().length()<1) ? "" : ("?"+params.substring(1));
	}
	
	private String uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws UploadException {
		String fileUri = null;
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String typeFolder = "/files";
				String filename = multipartFile.getOriginalFilename();
				byte[] data = multipartFile.getBytes();	
				fileUri = UploadUtils.uploadFile(realPath, site, typeFolder, filename, data, false);
			}catch (FileNotFoundException e) {
				if (logger.isDebugEnabled()){
					logger.debug("上传失败：", e);
					e.printStackTrace();
				}
				throw new UploadException("文件上传失败", e);
			} catch (IOException e) {
				if (logger.isDebugEnabled()){
					logger.debug("上传失败：", e);
					e.printStackTrace();
				}
				throw new UploadException("文件上传失败", e);
			}
		}
		return fileUri;
	}
	
	private boolean deleteFile(HttpServletRequest request, String relativeFilename){
		boolean success = false;
		try {
			String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
			success = UploadUtils.deleteFile(realPath, relativeFilename);
		}catch (FileNotFoundException e) {
			if (logger.isDebugEnabled()){
				logger.debug("删除旧附件失败：", e);
				e.printStackTrace();
			}
		}
		return success;
	}
}
