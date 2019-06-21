package cn.hxz.webapp.syscore.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hxz.webapp.syscore.support.AuthorizingUtils;

/**
 * 
 * @author chenke
 *
 */
@RestController
public class CaptchaRestController {
	
	@RequestMapping(value = "/captcha.png", method = RequestMethod.GET, produces=MediaType.IMAGE_PNG_VALUE)
	public Object captchaPNG(HttpServletRequest request) {
		return AuthorizingUtils.buildChallengeImage(112, 32, 5, "png");
	}
	
	@RequestMapping(value = "/captcha.gif", method = RequestMethod.GET, produces=MediaType.IMAGE_GIF_VALUE)
	public Object captchaGIF(HttpServletRequest request) {
		return AuthorizingUtils.buildChallengeImage(112, 32, 5, "gif");
	}
	
	@RequestMapping(value = "/captcha.jpg", method = RequestMethod.GET, produces=MediaType.IMAGE_JPEG_VALUE)
	public Object captchaJPG(HttpServletRequest request) {
		return AuthorizingUtils.buildChallengeImage(112, 32, 5, "jpg");
	}
	
}
