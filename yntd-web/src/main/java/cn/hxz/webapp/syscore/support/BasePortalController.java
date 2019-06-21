package cn.hxz.webapp.syscore.support;

import org.springframework.beans.factory.annotation.Autowired;

import cn.hxz.webapp.support.spring.EnvironmentBean;
import cn.hxz.webapp.syscore.entity.Site;
import net.chenke.playweb.support.spring.BaseMultiActionController;
import net.chenke.playweb.support.spring.MessageSourceBean;

/**
 * 
 * @author chenke
 * 
 */
public abstract class BasePortalController extends BaseMultiActionController
		implements SiteAware {
	
	@Autowired
	protected EnvironmentBean env;
	@Autowired
	protected MessageSourceBean msg;

	@Override
	protected String view(String view) {
		return this.view(this.getSkin(), view);
	}

	// -----------------------------------------------------------------------
	// Implements DomainAware methods
	
	private String host;
	private String skin;
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
		return skin;
	}

	@Override
	public void setSkin(String skin) {
		this.skin = skin;
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
