package cn.hxz.webapp.syscore.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import cn.hxz.webapp.support.spring.EnvironmentBean;
import cn.hxz.webapp.syscore.entity.Site;
import net.chenke.playweb.support.spring.BaseMultiActionController;
import net.chenke.playweb.support.spring.MessageSourceBean;

/**
 * 
 * @author chenke
 * 
 */
public abstract class BaseWorkbenchController extends BaseMultiActionController
		implements SiteAware {
	
	@Value("${adminPath}")
	private String adminPath;
	
	@Autowired
	protected EnvironmentBean env;
	@Autowired
	protected MessageSourceBean msg;
	
//	public String getAdminPath(){
//		return this.adminPath;
//	}
	
	@Override
	protected String view(String view) {
		return this.view(this.getSkin(), view);
	}
	
	@Override
	protected String redirect(String action) {
		Assert.notNull(action);
		return redirect(action, false);
	}

	protected String redirect(String action, boolean absolutePath) {
		Assert.notNull(action);
		if (absolutePath)
			return String.format("redirect:%s", action).replace("//", "/");
		else
			return String.format("redirect:%s/%s", adminPath, action).replace("//", "/");
	}

	
	// -----------------------------------------------------------------------
	// Implements DomainAware methods
	
	private String host;
	//private String skin;
	private Site site;
	
	@Override
	public String getHost() {
		return host;
	}

	@Override
	public void setHost(String domain) {
		this.host = domain;
	}

	@Override
	public String getSkin() {
		return "admin";
	}

	@Override
	public void setSkin(String skin) {
		//this.skin = skin;
	}
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void setSite(Site site) {
		this.site = site;
	}
}
