package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.AdditionalField;
import cn.hxz.webapp.content.entity.BuiltinModelEnum;
import cn.hxz.webapp.content.entity.Channel;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;


/**
 * ChannelSerivce.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ChannelService {
	
	Channel load(Long id);
	
	Channel loadCached(Long id);
	
	Channel load(Long siteId, String node);
	
	Channel loadCached(Long siteId, String node);
	
	Page<Channel> find(Long siteId, Long parentId, PageRequest pageable);
	
	Page<Channel> find(Long siteId, Long parentId, PageRequest pageable, String orderBy);
	
	List<Channel> findRootChannels(Long siteId, String orderBy);
	
	List<Channel> findChildren(Long parentId, String orderBy);
	
	List<Channel> findByModel(Long siteId, Long modelId);
	
	List<Channel> findAll(Long siteId);
	
	int create(Channel entity);
	
	int update(Channel entity);
	
	int enable(Long id, boolean curr);
	
	int remove(Long id);
	
	boolean isSpecificModel(Long channelId, BuiltinModelEnum model);
	
	List<AdditionalField> findAdditionalField(final Long channelId);
	
	List<AdditionalField> findAdditionalFieldCached(Long channelId);

}
