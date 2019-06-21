package cn.hxz.webapp.syscore.support;

import cn.hxz.webapp.syscore.entity.Site;

/**
 * 
 * @author chenke
 * 
 */
public interface SiteAware {
	
	String getHost();

	void setHost(String host);

	String getSkin();
	
	void setSkin(String skin);
	
	Site getSite();
	
	void setSite(Site site);

}
