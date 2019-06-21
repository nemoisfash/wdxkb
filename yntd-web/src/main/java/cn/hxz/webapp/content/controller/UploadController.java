package cn.hxz.webapp.content.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/api")
public class UploadController extends BaseWorkbenchController {

	@RequestMapping(value = "/picture/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object save(HttpServletRequest request,
			@RequestParam(value="upload", required=true) MultipartFile multipartFile) {
		
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
        
        map.put("success", success);
        map.put("message", message);
        map.put("filename", filename);
        return map;
	}
	
	/**
	 * 文件上传(doc,docx,xls,xlsx,zip,rar,7z,pdf)
	 * @param request
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object saveFile(HttpServletRequest request,
			@RequestParam(value="upload", required=true) MultipartFile multipartFile) {
		
		Map<String, Object> map = new HashMap<String, Object>();

		boolean success = true;
		String message = "";
		String filename = "";
		
        if(multipartFile==null || multipartFile.isEmpty()) {
        	success = false;
            message = "请选择上传文件";
        }
		
        if(success && multipartFile!=null && !multipartFile.isEmpty()) {
            if(!UploadUtils.isAllowedFileType(multipartFile.getOriginalFilename(), this.env)) {
            	success = false;
            	message = "文件类型不支持";
            }
        	
            if (success){
            	filename = uploadFile(request, multipartFile);
            }
        }
        
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
				String typeFolder = "/tmp";
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
	
	
	@RequestMapping(value = "/picture/upload2", method = RequestMethod.POST)
	@ResponseBody
	public Object saveFile2(HttpServletRequest request,
			@RequestParam(value="upload2", required=true) MultipartFile multipartFile) {
		
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
        
        map.put("success", success);
        map.put("message", message);
        map.put("filename", filename);
        return map;
	}
	
	@RequestMapping(value = "/picture/upload3", method = RequestMethod.POST)
	@ResponseBody
	public Object saveFile3(HttpServletRequest request,
			@RequestParam(value="upload3", required=true) MultipartFile multipartFile) {
		
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
        
        map.put("success", success);
        map.put("message", message);
        map.put("filename", filename);
        return map;
	}
	
	@RequestMapping(value = "/picture/upload4", method = RequestMethod.POST)
	@ResponseBody
	public Object saveFile4(HttpServletRequest request,
			@RequestParam(value="upload4", required=true) MultipartFile multipartFile) {
		
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
        
        map.put("success", success);
        map.put("message", message);
        map.put("filename", filename);
        return map;
	}
	
}
