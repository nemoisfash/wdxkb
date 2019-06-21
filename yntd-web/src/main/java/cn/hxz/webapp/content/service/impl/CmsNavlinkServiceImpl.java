package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.Navlink;
import cn.hxz.webapp.content.mapper.NavlinkMapper;
import cn.hxz.webapp.content.service.CmsNavlinkService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @author chenke
 * 
 */
@Service
public class CmsNavlinkServiceImpl implements CmsNavlinkService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private NavlinkMapper daoSiteNav;

	@Override
	@Cacheable(value=CacheUtils.CACHE_SYS, key="'sys_navlink_'+#id", unless="#result==null")
	public Navlink load(Long id){
		if(id==null)
			return null;
		return daoSiteNav.selectByPrimaryKey(id);
	}

	@Override
	public Navlink create(Navlink entity){
		if (entity == null)
			return null;
		daoSiteNav.insertSelective(entity);
		return entity;
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_navlink_'+#entity.id")
	public Navlink update(Navlink entity){
		if (entity == null || entity.getId() == null)
			return null;
		daoSiteNav.updateByPrimaryKeySelective(entity);
		return entity;
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_navlink_'+#id")
	public boolean delete(Long id){
		if (id==null)
			return false;
		
		int cnt = daoSiteNav.deleteByPrimaryKey(id);
		
		return cnt > 0;
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_SYS, key="'sys_navlink_'+#id")
	public boolean enable(Long id){
		if (id==null)
			return false;

		Navlink navLink = daoSiteNav.selectByPrimaryKey(id);
		if (navLink==null)
			return false;
		
		Navlink entity = new Navlink();
		entity.setId(id);
		entity.setEnabled(!navLink.getEnabled());
		int cnt = daoSiteNav.updateByPrimaryKeySelective(entity);
		
		return cnt > 0;
	}

	@Override
	public List<Navlink> find(Long typeId, Long parentId){
		Example example = new Example(Navlink.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("enabled", true);
//		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("typeId", typeId);
		if (parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		example.setOrderByClause(ORDER_BY);
		return daoSiteNav.selectByExample(example);
	}

	@Override
	public Page<Navlink> findAll(Long typeId, Long parentId, PageRequest pageable){
		Example example = new Example(Navlink.class);
		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("typeId", typeId);
		if (parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		example.setOrderByClause(ORDER_BY);
		List<Navlink> entities = daoSiteNav.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Navlink>(entities, pageable);
	}
}
