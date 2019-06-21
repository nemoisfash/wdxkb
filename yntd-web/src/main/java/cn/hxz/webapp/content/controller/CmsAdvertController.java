package cn.hxz.webapp.content.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import cn.hxz.webapp.content.entity.Advert;
import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.service.AdvertGroupService;
import cn.hxz.webapp.content.service.AdvertService;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.JsonResult;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.FileUploadException;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("${adminPath}/cms/advert")
public class CmsAdvertController extends BaseWorkbenchController {
	@Value("${allowedUploadImage}")
	private String allowedImageType;

	@Autowired
	private AdvertService bizAdvert;
	@Autowired
	private AdvertGroupService bizAdvertType;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String none() {
		List<AdvertGroup> types = bizAdvertType.find(this.getSite().getId());
		if (types.size() == 0) {
			return this.view("/cms/advert/none");
		} else {
			String code = types.get(0).getCode();
			return this.redirect(String.format("/cms/advert/%s/list.html", code));
		}
	}
	
	@RequestMapping(value = "/{code}/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(value = "code") String typeCode) {
		
		AdvertGroup type = bizAdvertType.load(this.getSite().getId(), typeCode);

		String uuid = HashUtils.MD5(request.getRequestURI());
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		
		Page<Advert> entities = bizAdvert.find(type.getId(), pageable, filters);
		
		model.addAttribute("type", type);
		model.addAttribute("filters", filters);
		model.addAttribute("entities", entities);
		return this.view("/cms/advert/list");
	}
	
	@RequestMapping(value = "/{code}/edit", method = RequestMethod.GET)
	public String edit(Model model,
			@PathVariable(value = "code") String typeCode,
			@RequestParam(value = "id", required = false) Long id) {

		AdvertGroup type = bizAdvertType.load(this.getSite().getId(), typeCode);
		
		Advert entity = null;
		if (id == null) {
			entity = new Advert();
			entity.setTypeId(type.getId());
		} else {
			entity = bizAdvert.load(id);
		}

		model.addAttribute("type", type);
		model.addAttribute("entity", entity);
		return this.view("/cms/advert/edit");
	}

	@RequestMapping(value = "/{code}/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model, HttpServletRequest request,
			@Valid @ModelAttribute("entity") Advert entity, BindingResult binding,
			@RequestParam(value="uploadfile", required=false) MultipartFile multipartFile) {

		boolean success = true;
        if(multipartFile==null || multipartFile.isEmpty()) {
        	// 当create时，必须传图
        	if (entity.getId()==null) {
        		success = false;
        		model.addAttribute("message", "请上传图片");
        	}
    	} else {
            if(!UploadUtils.isAllowedType(multipartFile.getOriginalFilename(), allowedImageType)) {
            	success = false;
        		model.addAttribute("message", "文件类型不支持");
            }
        }
        
		if (binding.hasErrors() || !success) {
			model.addAttribute("entity", entity);
			return this.view("/content/advert/edit");
		}
		
        if(multipartFile!=null && !multipartFile.isEmpty()) {
			String filename = uploadFile(request, multipartFile);
			// 当update时，保存新图后，删除旧图
			if (entity.getId()!=null){
				deleteFile(request, entity.getFilename());
			}
			entity.setFilename(filename);
        }

		if (entity.getId() == null){
			entity.setTrashed(false);
			bizAdvert.create(entity);
			}
		else{
			bizAdvert.update(entity);
			}

		model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/advert/{code}/list.html");
	}
	
	@ResponseBody
	@RequestMapping(value="/{code}/enable", method = RequestMethod.POST)
	public Object enable(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = true) Long id) {

		Advert entity = bizAdvert.load(id);
		entity.setEnabled(!entity.getEnabled());
		bizAdvert.update(entity);
		
		return new JsonResult(true);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/remove", method = RequestMethod.POST)
	public Object remove(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "id", required = true) Long id) {

		bizAdvert.remove(id);

		return new JsonResult(true);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{code}/remove/batch", method = RequestMethod.POST)
	public Object removeBatch(
			@PathVariable(value = "code") String channelCode,
			@RequestParam(value = "ids", required = true) Long[] ids){
		
	     for(Long id : ids){
	    	 bizAdvert.remove(id);
	     }
	     
	     return new JsonResult(true);
	}
	
	private String uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws FileUploadException {
		String fileUri = null;
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String typeFolder = "/advert";
				String filename = multipartFile.getOriginalFilename();
				byte[] data = multipartFile.getBytes();	
				fileUri = UploadUtils.uploadFile(realPath, site, typeFolder, filename, data, false);
			}catch (FileNotFoundException e) {
				if (logger.isDebugEnabled()){
					logger.debug("上传失败：", e);
					e.printStackTrace();
				}
				throw new FileUploadException("文件上传失败", e);
			} catch (IOException e) {
				if (logger.isDebugEnabled()){
					logger.debug("上传失败：", e);
					e.printStackTrace();
				}
				throw new FileUploadException("文件上传失败", e);
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
