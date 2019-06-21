package cn.hxz.webapp.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;

public class ShiroUtils {
	
	public static void login(AuthenticationToken token){
		ShiroUtils.getSubject().login(token);
	}
	
	public static String getSessionId(){
		return (String) ShiroUtils.getSubject().getSession(true).getId();
	}
	
	public static String getHost(){
		return ShiroUtils.getSubject().getSession().getHost();
	}

	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	public static Subject getSubject(String sessionId){
		Session session = SecurityUtils.getSecurityManager()
				.getSession(new DefaultSessionKey(sessionId));
		Subject subject = new Subject.Builder().session(session)
				.sessionId(new DefaultSessionKey(session.getId())).buildSubject();
		return subject;
	}
}
