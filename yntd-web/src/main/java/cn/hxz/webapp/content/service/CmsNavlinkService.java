package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.Navlink;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * 
 * @author chenke
 * 
 */
public interface CmsNavlinkService {

	Navlink load(Long id);

	Navlink create(Navlink entity);
	
	Navlink update(Navlink entity);
	
	boolean delete(Long id);

	boolean enable(Long id);
	
	List<Navlink> find(Long typeId, Long parentId);

	Page<Navlink> findAll(Long typeId, Long parentId, PageRequest pageable);

}
