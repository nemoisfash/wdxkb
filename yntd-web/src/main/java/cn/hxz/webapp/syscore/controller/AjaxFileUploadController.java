package cn.hxz.webapp.syscore.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.JsonResult;

@Controller
@RequestMapping("${adminPath}/")
public class AjaxFileUploadController extends BaseWorkbenchController {

	@Value("${allowedUploadFile}")
	private String allowedUploadFile;
	@Value("${allowedUploadImage}")
	private String allowedUploadImage;
	
	@ResponseBody
	@RequestMapping(value = "/{module}/upload/img", method = RequestMethod.POST)
	public Object save(HttpServletRequest request,
			@PathVariable(value = "module") String module,
			@RequestParam(value="currUri", required=false) String currentUri,
			@RequestParam(value="uploadFile", required=true) MultipartFile uploadFile) {

		boolean success = true;
		String message = "";
		String fileUri = "";
		
        if(uploadFile==null || uploadFile.isEmpty()) {
        	success = false;
            message = "请选择上传文件";
        }
		
        if(success && uploadFile!=null && !uploadFile.isEmpty()) {
            if(!UploadUtils.isAllowedType(uploadFile.getOriginalFilename(), allowedUploadImage)) {
            	success = false;
            	message = "文件类型不支持";
            }
        }
        
        if (success){
        	fileUri = uploadFile(request, uploadFile, module);
        	if (StringUtils.hasText(fileUri) && StringUtils.hasText(currentUri))
        		UploadUtils.deleteFile(request, currentUri);
        }
        
        JsonResult data = new JsonResult(success, message);
        data.put("fileUri", fileUri);
        return data;
	}
	
	/**
	 * 文件上传(doc,docx,xls,xlsx,zip,rar,7z,pdf)
	 * @param request
	 * @param uploadFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{module}/upload/doc", method = RequestMethod.POST)
	public Object saveFile(HttpServletRequest request,
			@PathVariable(value = "module") String module,
			@RequestParam(value="currUri", required=false) String currentUri,
			@RequestParam(value="uploadFile", required=true) MultipartFile uploadFile) {
		
		boolean success = true;
		String message = "";
		String fileuri = "";
		
        if(uploadFile==null || uploadFile.isEmpty()) {
        	success = false;
            message = "请选择上传文件";
        }
		
        if(success && uploadFile!=null && !uploadFile.isEmpty()) {
            if(!UploadUtils.isAllowedType(uploadFile.getOriginalFilename(), allowedUploadFile)) {
            	success = false;
            	message = "文件类型不支持";
            }
        }
        
        if (success){
        	fileuri = uploadFile(request, uploadFile, module);
        }

        JsonResult data = new JsonResult(success, message);
        data.put("fileuri", fileuri);
        return data;
	}
	
	private String uploadFile(HttpServletRequest request, MultipartFile multipartFile, String module) throws UploadException {
		String fileUri = null;
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String filename = multipartFile.getOriginalFilename();
				byte[] data = multipartFile.getBytes();	
				fileUri = UploadUtils.uploadFile(realPath, site, module, filename, data, false);
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
