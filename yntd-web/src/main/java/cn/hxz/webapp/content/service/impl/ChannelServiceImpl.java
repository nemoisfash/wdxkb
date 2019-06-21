package cn.hxz.webapp.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.hxz.webapp.content.entity.AdditionalField;
import cn.hxz.webapp.content.entity.BuiltinModelEnum;
import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.ChannelField;
import cn.hxz.webapp.content.entity.Model;
import cn.hxz.webapp.content.mapper.ChannelFieldMapper;
import cn.hxz.webapp.content.mapper.ChannelMapper;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.ModelService;
import cn.hxz.webapp.util.CacheUtils;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * ChannelSerivceImpl.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ChannelServiceImpl implements ChannelService {

	@Autowired
	private ModelService bizModel;
	@Autowired
	private ChannelMapper daoChannel;
	@Autowired
	private ChannelFieldMapper daoChannelField;
	
	public static final String ORDER_BY = "priority DESC";
	public static final String FIELD_ORDER_BY = "priority ASC";
	
	@Override
	public Channel load(Long id){
		return daoChannel.selectByPrimaryKey(id);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_channel_'+#id", unless="#result==null")
	public Channel loadCached(Long id){
		return load(id);
	}
	
	@Override
	public Channel load(Long siteId, String code){
		Channel entity = new Channel();
		entity.setSiteId(siteId);
		entity.setNode(code);
		return daoChannel.selectOne(entity);
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_channel_code'+#code+'sys_site_'+#siteId", unless="#result==null")
	public Channel loadCached(Long siteId, String code){
		return load(siteId, code);
	}
	
	@Override
	public Page<Channel> find(Long siteId, Long parentId, PageRequest pageable) {
		return this.find(siteId, parentId, pageable, ORDER_BY);
	}
	
	@Override
	public Page<Channel> find(Long siteId, Long parentId, PageRequest pageable, String orderBy) {
		Example example = new Example(Channel.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("siteId", siteId);
		criteria.andEqualTo("trashed", false);
		if (parentId==null)
			criteria.andIsNull("parentId");
		else
			criteria.andEqualTo("parentId", parentId);
		example.setOrderByClause(ORDER_BY);
		List<Channel> entities = daoChannel.selectByExampleAndRowBounds(example, pageable); 
		return new PageImpl<Channel>(entities, pageable);
	}
	
	@Override
	public List<Channel> findRootChannels(Long siteId,String orderBy){
		Example example = new Example(Channel.class);
		example.createCriteria().andEqualTo("siteId", siteId).andIsNull("parentId").andEqualTo("enabled",true).andEqualTo("trashed",false);
		example.setOrderByClause(orderBy);
		return daoChannel.selectByExample(example);
	}
	
	@Override
	public List<Channel> findChildren(Long parentId, String orderBy){
		Example example = new Example(Channel.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("parentId", parentId);
		criteria.andEqualTo("enabled", true);
		criteria.andEqualTo("trashed", false);
		example.setOrderByClause(orderBy);
		return daoChannel.selectByExample(example);
	}
	
	@Override
	public List<Channel> findByModel(Long siteId, Long modelId){
		Example example = new Example(Channel.class);
		example.createCriteria().andEqualTo("siteId", siteId).andEqualTo("modelId", modelId);
		example.setOrderByClause("parent_id asc, priority asc");
		return daoChannel.selectByExample(example);
	}
	
	@Override
	public List<Channel> findAll(Long siteId){
		Example example = new Example(Channel.class);
		example.createCriteria().andEqualTo("siteId", siteId);
		return daoChannel.selectByExample(example);
	}


	@Override
	public int create(Channel entity){
		return daoChannel.insertSelective(entity);
	}
	
	@Override
	public int update(Channel entity){
		return daoChannel.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	public int enable(Long id, boolean curr){
		Channel entity = new Channel();
		entity.setId(id);
		entity.setEnabled(curr);
		return daoChannel.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	public int remove(Long id){
		Channel entity = new Channel();
		entity.setId(id);
		entity.setTrashed(true);
		return daoChannel.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 判断是否指定的内容模型
	 * @param channel
	 * @param contentModel ContentModel中定义的常量
	 * @return
	 */
	public boolean isSpecificModel(Long channelId, BuiltinModelEnum modelEnum){
		boolean success = false;

		Channel channel = loadCached(channelId);
		Model model = bizModel.loadCached(channel.getModelId());
		Model modelParent = bizModel.loadCached(model.getParentId());
		
		if ((model!=null && model.getId()==modelEnum.getValue())
				|| (modelParent!=null && modelParent.getId()==modelEnum.getValue()))
			success = true;
		
		return success;
	}

	@Override
	public List<AdditionalField> findAdditionalField(Long channelId) {
		Assert.notNull(channelId);
		
		List<AdditionalField> fields = new ArrayList<AdditionalField>();
		Channel channel = loadCached(channelId);

		if (channel==null)
			return fields;
		
		Model model = bizModel.load(channel.getModelId());
		
		if (model.getBuiltin()!=null && !model.getBuiltin())
			fields.addAll(bizModel.findAdditionalField(model.getId()));
		
		Example example = new Example(ChannelField.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("channelId", channelId);
		example.setOrderByClause(FIELD_ORDER_BY);
		List<ChannelField> items = daoChannelField.selectByExample(example);
		
		if (items!=null && !items.isEmpty())
			fields.addAll(items);

		return fields;
	}
	
	@Override
	@Cacheable(value=CacheUtils.CACHE_CMS, key="'cms_channel_additional_fields_'+#channelId", unless="#result==null")
	public List<AdditionalField> findAdditionalFieldCached(Long channelId) {
		return this.findAdditionalField(channelId);
	}
}
