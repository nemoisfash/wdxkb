package cn.hxz.webapp.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.hxz.webapp.content.entity.AdditionalField;
import cn.hxz.webapp.content.entity.Model;
import cn.hxz.webapp.content.entity.ModelField;
import cn.hxz.webapp.content.mapper.ModelFieldMapper;
import cn.hxz.webapp.content.mapper.ModelMapper;
import cn.hxz.webapp.content.service.ModelService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ModelSerivceImpl.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	private ModelMapper daoModel;
	@Autowired
	private ModelFieldMapper daoModelField;
	
	public static final String FIELD_ORDER_BY = "priority ASC";
	
	public Model load(Long id){
		return daoModel.selectByPrimaryKey(id);
	}
	
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'model_'+#id", unless="#result==null")
	public Model loadCached(Long id){
		return load(id);
	}

	@Override
	public List<AdditionalField> findAdditionalField(Long modelId) {
		Assert.notNull(modelId);
		
		List<AdditionalField> fields = new ArrayList<AdditionalField>();
		Model model = loadCached(modelId);
		
		if (model==null)
			return fields;
		
		Example example = new Example(ModelField.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("modelId", modelId);
		example.setOrderByClause(FIELD_ORDER_BY);
		List<ModelField> items = daoModelField.selectByExample(example);
		
		if (items!=null && !items.isEmpty())
			fields.addAll(items);
		
		return fields;
	}
	
	@Override
	public Page<Model> find(PageRequest pageable){
		Example example = new Example(Model.class);
		Criteria criteria = example.createCriteria();
		example.setOrderByClause("id asc");
		List<Model> entities = daoModel.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Model>(entities, pageable);
	}
}
