package cn.hxz.webapp.syscore.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author chenke
 * 
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger log = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);
	
    private Map<String, String> loginUrlByUserAgent = new HashMap<>();
    
    public void setLoginUrls(final Map<String, String> loginUrlByUserAgent) {
        this.loginUrlByUserAgent = loginUrlByUserAgent;
    }
 
    protected void redirectToLogin(final ServletRequest request, final ServletResponse response) throws IOException {
        final String loginUrl = getLoginUrl(request);
        WebUtils.issueRedirect(request, response, loginUrl);
    }
 
    private String getLoginUrl(final ServletRequest request) {
        // check user agent
        final String userAgent = getUserAgent(request);
        // and return appropriate login url
        return userAgent != null && loginUrlByUserAgent.containsKey(userAgent) ?
                loginUrlByUserAgent.get(userAgent) :
                getLoginUrl();
    }
 
    private String getUserAgent(final ServletRequest request) {
    	String userAgent = null;
        // get "User-Agent" header
    	if (log.isDebugEnabled())
    		log.debug("Need UserAgent");
    	return userAgent;
    }
}
