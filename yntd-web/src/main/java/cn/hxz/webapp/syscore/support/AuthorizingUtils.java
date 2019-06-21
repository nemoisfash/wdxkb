package cn.hxz.webapp.syscore.support;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.service.UserSerivce;
import cn.hxz.webapp.util.ShiroUtils;
import net.chenke.playweb.support.spring.ApplicationContextBean;
import net.chenke.playweb.util.CaptchaUtils;

/**
 * 
 * @author chenke
 * 
 */
public class AuthorizingUtils {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizingUtils.class);
	
	private final static String CAPTCHA_KEY = "__captcha_code";

	public static byte[] buildChallengeImage(int width, int height, int length, String format) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			String captcha = CaptchaUtils.randomCaptcha(length);
			BufferedImage challenge = CaptchaUtils.buildChallengeImage(width, height, captcha);
			ImageIO.write(challenge, format, byteArrayOutputStream);
			SecurityUtils.getSubject().getSession().setAttribute(CAPTCHA_KEY, captcha);
		} catch (IOException e) {
			if (logger.isDebugEnabled())
				e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}

	public static boolean validCaptcha(String captcha) {
		if (!StringUtils.hasText(captcha))
			return false;
		String curr = (String) SecurityUtils.getSubject().getSession().getAttribute(CAPTCHA_KEY);
		return captcha.equalsIgnoreCase(curr);
	}

	public static User loadCurrentUser(){
		String username = (String)ShiroUtils.getSubject().getPrincipal();
		if (username == null)
			return null;
		else
			return loadUser(username);
	}
	
	public static String loadRealmName(Long userId){
		UserSerivce bizUser = ApplicationContextBean.getBean(UserSerivce.class);
		User user = bizUser.load(userId);
		return user.getName()==null ? user.getUsername() : user.getName();
	}
	
	public static User loadUser(String username){
		UserSerivce bizUser = ApplicationContextBean.getBean(UserSerivce.class);
		return bizUser.load(username);
	}
	
	public static List<String> findRolesCode(String username){
		UserSerivce bizUser = ApplicationContextBean.getBean(UserSerivce.class);
		return bizUser.findRolesCode(username);
	}
	
	public static List<String> findPermissionsCode(String username){
		UserSerivce bizUser = ApplicationContextBean.getBean(UserSerivce.class);
		return bizUser.findPermissionsCode(username);
	}

}
