package cn.hxz.webapp.syscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.syscore.entity.Region;
import cn.hxz.webapp.syscore.mapper.RegionMapper;
import cn.hxz.webapp.syscore.service.RegionService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * RegionServiceImpl.java Create on 2017-03-20 16:03:58
 * <p>
 * 所在地区 业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class RegionServiceImpl implements RegionService {

	public static final String ORDER_BY = "priority ASC";

	@Autowired
	private RegionMapper daoRegion;

	@Override
	public Region load(Long id){
		return daoRegion.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'sys_region_'+#id", unless="#result==null")
	public Region loadCached(Long id){
		return load(id);
	}

	@Override
	public Page<Region> find(Long parentId, PageRequest pageable){
		return find(parentId, pageable, ORDER_BY);
	}

	@Override
	public Page<Region> find(Long parentId, PageRequest pageable, String orderBy){
		Example example = new Example(Region.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("trashed", false);
		if (parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		example.setOrderByClause(orderBy);
		List<Region> entities = daoRegion.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Region>(entities, pageable);
	}

	@Override
	public int create(Region entity){
		return daoRegion.insertSelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_region_'+#entity.id")
	public int update(Region entity){
		return daoRegion.updateByPrimaryKeySelective(entity);
	}

	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_region_'+#id")
	public int enable(Long id, boolean curr){
		Region entity = new Region();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoRegion.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(value=CacheUtils.CACHE_CMS, key="'sys_region_'+#id")
	public int remove(Long id){
		Region entity = new Region();
		entity.setId(id);
		entity.setTrashed(true);
		return daoRegion.updateByPrimaryKeySelective(entity);
	}

}
