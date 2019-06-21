package cn.hxz.webapp.syscore.controller.admin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.support.spring.AttrForm;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.entity.SiteAttr;
import cn.hxz.webapp.syscore.service.SiteAttrService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;

@Controller
@RequestMapping("${adminPath}/content/siteattr")
public class SiteAttrAdminController extends BaseWorkbenchController {

	@Autowired
	private SiteAttrService bizSiteAttr;


	@RequestMapping(value = "/{section}/edit", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,
			@PathVariable(value = "section") String section,
			@RequestParam(value = "id", required = false) Long id) {

		Long siteId = this.getSite().getId();
		Map<String, SiteAttr> attr = bizSiteAttr.find(siteId, section);

		modelMap.put("attr", attr);
		return this.view("/content/siteattr/"+section);
	}

	@RequestMapping(value = "/{section}/save", method = RequestMethod.POST)
	public String save(RedirectAttributesModelMap modelMap,
			@PathVariable(value = "section") String section,
			AttrForm<String> attr) {

		Long siteId = this.getSite().getId();
		for (String key : attr.keySet()){
			SiteAttr entity = new SiteAttr();
			entity.setId(siteId);
			entity.setField(key);
			entity.setValue(attr.get(key));
			entity.setSection(section);
			bizSiteAttr.save(entity);
		}

		modelMap.addFlashAttribute("message", "已保存");
		return this.redirect("/content/siteattr/{section}/edit.html");
	}
	
	@RequestMapping(value = "/savesImg", method = RequestMethod.POST)
	public String saveSimg(RedirectAttributesModelMap modelMap,
			String Mobilelogo) {

		Long siteId = this.getSite().getId();
		SiteAttr entity = new SiteAttr();
		entity.setId(siteId);
		entity.setField("logo_qr_mobile");
		entity.setValue(Mobilelogo);
		entity.setSection("s-img");
		bizSiteAttr.save(entity);
		modelMap.addFlashAttribute("message", "已保存");
		return this.redirect("/content/siteattr/s-img/edit.html");
	}
	
	@RequestMapping(value = "/{section}/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object save(HttpServletRequest request,
			@PathVariable(value = "section") String section,
			@RequestParam(value="field", required=true) String field,
			@RequestParam(value="fileupload", required=true) MultipartFile multipartFile) {
		
		Map<String, Object> map = new HashMap<String, Object>();

		boolean success = true;
		String message = "";
		String filename = "";
				
        if(multipartFile==null || multipartFile.isEmpty()) {
        	success = false;
            message = "请选择上传文件";
        }
		
        if(success && multipartFile!=null && !multipartFile.isEmpty()) {
            if(!UploadUtils.isAllowedImageType(multipartFile.getOriginalFilename(), this.env)) {
            	success = false;
            	message = "文件类型不支持";
            }
        	
            if (success){
            	filename = uploadFile(request, multipartFile);
            }
        }
        
		Long siteId = this.getSite().getId();
		SiteAttr entity = new SiteAttr();
		entity.setId(siteId);
		entity.setField(field);
		entity.setValue(filename);
		entity.setSection(section);
		bizSiteAttr.save(entity);
        
        map.put("success", success);
        map.put("message", message);
        map.put("filename", filename);
        return map;
	}
	
	private String uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws UploadException {
		String fileUri = null;
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String typeFolder = "/siteattr";
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

}
