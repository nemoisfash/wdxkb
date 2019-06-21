package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.RelatedLink;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * RelatedLinkService.java Create on 2017-03-22 15:09:27
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface RelatedLinkService {

	RelatedLink load(Long id);

	RelatedLink loadCached(Long id);
	
	Page<RelatedLink> findAll(Long channelId,PageRequest pageable, String orderBy);
	
	Page<RelatedLink> findAll(Long channelId, PageRequest pageable);

	Page<RelatedLink> find(Long channelId, PageRequest pageable);

	Page<RelatedLink> find(Long channelId,Boolean enable, PageRequest pageable, String orderBy);
	
	Page<RelatedLink> find(List<Long> channelIds, PageRequest pageable);
	
	Page<RelatedLink> find(List<Long> channelIds,Boolean enable, PageRequest pageable, String orderBy);

	int create(RelatedLink entity);
	
	int update(RelatedLink entity);

	int enable(Long id, boolean curr);
	
	int remove(Long id);

}
