package cn.hxz.webapp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.hxz.webapp.support.UploadException;
import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;

public class AjaxFileUploadUtil extends BaseWorkbenchController {

	public static Map<String, Object> uploadFile(String realPath,Site site, String typeFolder, MultipartFile file) throws UploadException {
		Boolean success = true;
		String imageUri = null;
		Map<String, Object> map = new HashMap<>();
		if (file != null && !file.isEmpty()) {
			try {
				String filename = file.getOriginalFilename();
				Date now = new Date();
				String extension = FilenameUtils.getExtension(filename).toLowerCase();
				byte[] imageData = file.getBytes();
				imageUri = UploadUtils.uploadFile(realPath, site, typeFolder, now, extension, imageData, false);
				if (imageUri != "") {
					map.put("imageUri", imageUri);
				} else {
					map.put("success", false);
				}
			} catch (FileNotFoundException e) {
				success = false;
				throw new UploadException("图片上传失败", e);
			} catch (IOException e) {
				success = false;
				throw new UploadException("图片上传失败", e);
			}
		} else {
			success = false;
		}
		map.put("success", success);
		return map;
	}

}
