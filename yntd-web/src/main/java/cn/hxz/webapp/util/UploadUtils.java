package cn.hxz.webapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import cn.hxz.webapp.support.spring.EnvironmentBean;
import cn.hxz.webapp.syscore.entity.Site;

/**
 * 
 * @author cn.feeboo
 *
 */
public class UploadUtils {
	protected final static Logger logger = LoggerFactory.getLogger(UploadUtils.class);

	public static String uploadFile(String realPath, Site site, 
			String typeFolder, String filename, byte[] data, boolean isThumb) {
		Date now = new Date();
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		return uploadFile(realPath, site, typeFolder, now, extension, data, isThumb);
	}
	
	public static String uploadFile(String realPath, Site site, 
			String typeFolder, Date now, String extension, byte[] data, boolean isThumb) {
		String filePath = null;
		
		String relativeFolder = buildRelativeFolder(site, typeFolder, now);
		String relativeFilename = buildRelativeFilename(relativeFolder, now, extension, isThumb);
		String serverFolder = buildServerUri(realPath, relativeFolder);
		String serverFilename = buildServerUri(realPath, relativeFilename);
		
		try {

			File saveFolder = new File(serverFolder);
			if (!saveFolder.exists()) // 如果目录不存在就创建
				saveFolder.mkdirs();

			File saveFilename = new File(serverFilename);
			FileOutputStream fileoutstream = new FileOutputStream(saveFilename);
			fileoutstream.write(data);
			fileoutstream.close();
			filePath = relativeFilename;
			if (logger.isDebugEnabled())
				logger.debug("UPLOAD FILE: " + serverFilename);
		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("UPLOAD FILE FAILED: " + serverFilename, e);
		}
		return filePath.replace("\\", "/");
	}
	
	public static boolean deleteFile(HttpServletRequest request, String relativeFilename){
		boolean success = false;
		try {
			String realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
			success = UploadUtils.deleteFile(realPath, relativeFilename);
		}catch (FileNotFoundException e) {
			if (logger.isDebugEnabled()){
				logger.debug("删除旧图片失败：", e);
				e.printStackTrace();
			}
		}
		return success;
	}
	
	public static boolean deleteFile(String realPath, String relativeFilename) {
		boolean success = false;
		String serverFilename = FilenameUtils.normalize(String.format("/%s/%s", realPath, relativeFilename));
		try {
			File file = new File(serverFilename);
			if (file.exists())
				success = file.delete();
			if (logger.isDebugEnabled())
				logger.debug("DELETE FILE: " + serverFilename);
		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("DELETE FILE FAILED: " + serverFilename, e);
		}
		return success;
	}

	public static String buildRelativeFolder(final Site site, final String typeFolder, final Date now) {
		String tpl = "/data/%s%s/%s/%s/"; // "/data/001/article/2014/08/"
		String folder = (typeFolder==null) ? "" : typeFolder;
		if (StringUtils.isNotBlank(folder) && !StringUtils.startsWith(folder, "/"))
			folder = "/" + folder;
		String folderUri = String.format(tpl, 
				StringUtils.leftPad(Objects.toString(site.getId(), null), 3, "0"),
				folder,
				timeToString("yyyy", now),
				timeToString("MM", now));
		return FilenameUtils.normalize(folderUri);
	}
	
	public static String buildRelativeFilename(final String folderUri, final Date now, 
			final String extension, final boolean isThumb) {
		String tpl = "%s/%s%s.%s"; // "/data/001/article/2014/08/28120500000.png"
		String fileUri = String.format(tpl, 
				folderUri,
				timeToString("ddHHmmssS", now),
				isThumb ? "_thumb" : "",
				extension);
		return FilenameUtils.normalize(fileUri);
	}
	
	public static String buildServerUri(final String realPath, final String relativeUri) {
		String tpl = "/%s/%s"; // "/data/001/article/2014/08/28120500000.png"
		String uploadUri = String.format(tpl, 
				realPath,
				relativeUri);
		return FilenameUtils.normalize(uploadUri);
	}
	
	public static String timeToString(String pattern, final Date now) {
		SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.applyPattern(pattern);// 如：yyyyMMddHHmmssS=20140828120500000
		return dateFormat.format(now);
	}

	public static boolean isAllowedType(String filename, String allowedType) {
		String extension = FilenameUtils.getExtension(filename);
		if (extension!=null && allowedType!=null)
			return allowedType.toLowerCase().contains(extension.toLowerCase());
		else
			return false;
	}
	
	@Deprecated
	public static boolean isAllowedFileType(String filename, EnvironmentBean env) {
		return isAllowedType(filename, env.getProperty("allowedUploadFile"));
	}
	
	@Deprecated
	public static boolean isAllowedImageType(String filename, EnvironmentBean env) {
		return isAllowedType(filename, env.getProperty("allowedUploadImage"));
	}
}
