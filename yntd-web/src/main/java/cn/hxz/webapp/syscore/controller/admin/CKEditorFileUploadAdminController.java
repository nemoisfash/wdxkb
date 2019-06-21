package cn.hxz.webapp.syscore.controller.admin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.UploadUtils;

@Controller
@RequestMapping("${adminPath}/ckeditor")
public class CKEditorFileUploadAdminController extends BaseWorkbenchController {

	@RequestMapping(value = "/upload/images", method = RequestMethod.POST)
	public String save(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="upload", required=true) MultipartFile multipartFile) {

		boolean success = true;
		String message = "";
		String filename = "";
		
		String callback = request.getParameter("CKEditorFuncNum");
		
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

        editorCallback(response, callback, filename, message);
        return null;
	}
	
	private String uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws UploadException {
		String fileUri = null;
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			try {
				String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
				Site site = this.getSite();
				String typeFolder = "/content";
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
	
    private void editorCallback(HttpServletResponse response, String callback, String filename, String message){
        response.setCharacterEncoding("UTF-8");
        if (filename!=null)
        	filename = filename.replace("\\", "/");
        PrintWriter out;
		try {
			out = response.getWriter();
	        out.println("<script type=\"text/javascript\">");
	        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",\"" + filename + "\",\"" + message + "\");");
	        out.println("</script>");
		} catch (IOException e) {
			if (logger.isDebugEnabled()){
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
		} 
    }
}
