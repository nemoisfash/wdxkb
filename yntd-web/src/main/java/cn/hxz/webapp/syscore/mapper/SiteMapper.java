package cn.hxz.webapp.syscore.mapper;

import cn.hxz.webapp.syscore.entity.Site;
import net.chenke.playweb.support.mybatis.DynaMapper;

/**
 * 
 * @author chenke
 * 
 */
public interface SiteMapper extends DynaMapper<Site> {

	Site loadByServerName(String host);
}