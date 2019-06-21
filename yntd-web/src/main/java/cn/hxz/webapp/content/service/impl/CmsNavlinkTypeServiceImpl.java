package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hxz.webapp.content.entity.AdvertGroup;
import cn.hxz.webapp.content.entity.NavlinkType;
import cn.hxz.webapp.content.mapper.NavlinkTypeMapper;
import cn.hxz.webapp.content.service.CmsNavlinkTypeService;
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
public class CmsNavlinkTypeServiceImpl implements CmsNavlinkTypeService {

	public static final String ORDER_BY = "priority DESC";

	@Autowired
	private NavlinkTypeMapper daoSiteNavGroup;

	@Override
	@Cacheable(value = CacheUtils.CACHE_SYS, key = "'sys_navtype_'+#id", unless = "#result==null")
	public NavlinkType load(Long id) {
		if (id == null)
			return null;
		return daoSiteNavGroup.selectByPrimaryKey(id);
	}

	@Override
	public NavlinkType load(Long siteId, String code) {
		if (siteId == null || !StringUtils.hasText(code))
			return null;

		NavlinkType record = new NavlinkType();
		record.setSiteId(siteId);
		record.setCode(code);
		return daoSiteNavGroup.selectOne(record);
	}

	@Override
	public NavlinkType create(NavlinkType entity) {
		if (entity == null)
			return null;
		daoSiteNavGroup.insertSelective(entity);
		return entity;
	}

	@Override
	@CacheEvict(value = CacheUtils.CACHE_SYS, key = "'sys_navtype_'+#entity.id")
	public NavlinkType update(NavlinkType entity) {
		if (entity == null || entity.getId() == null)
			return null;
		daoSiteNavGroup.updateByPrimaryKeySelective(entity);
		return entity;
	}

	@Override
	@CacheEvict(value = CacheUtils.CACHE_SYS, key = "'sys_navtype_'+#entity.id")
	public boolean delete(Long id) {
		if (id == null)
			return false;
		int cnt = daoSiteNavGroup.deleteByPrimaryKey(id);
		return cnt > 0;
	}

//	@Override
//	@CacheEvict(value = CacheUtils.CACHE_SYS, key = "'sys_nav_type_'+#id")
//	public boolean remove(Long id) {
//		if (id == null)
//			return false;
//		
//		SysNavtype entity = new SysNavtype();
//		entity.setId(id);
//		entity.setTrashed(true);
//		int cnt = daoSiteNavGroup.updateByPrimaryKeySelective(entity);
//		
//		return cnt > 0;
//	}

	@Override
	@CacheEvict(value = CacheUtils.CACHE_SYS, key = "'sys_navtype_'+#id")
	public boolean enable(Long id) {
		if (id == null)
			return false;
		
		NavlinkType navType = daoSiteNavGroup.selectByPrimaryKey(id);
		if (navType == null)
			return false;
		
		NavlinkType entity = new NavlinkType();
		entity.setId(id);
		entity.setEnabled(!navType.getEnabled());
		int cnt = daoSiteNavGroup.updateByPrimaryKeySelective(entity);
		
		return cnt > 0;
	}
	
	@Override
	public List<NavlinkType> find(Long siteId){
		Example example = new Example(AdvertGroup.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("enabled", true);
//		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("siteId", siteId);
		example.setOrderByClause(ORDER_BY);
		return daoSiteNavGroup.selectByExample(example);
	}

	@Override
	public Page<NavlinkType> findAll(Long siteId, PageRequest pageable) {
		Example example = new Example(NavlinkType.class);
		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("trashed", false);
		criteria.andEqualTo("siteId", siteId);
		example.setOrderByClause(ORDER_BY);
		List<NavlinkType> entities = daoSiteNavGroup.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<NavlinkType>(entities, pageable);
	}
}
