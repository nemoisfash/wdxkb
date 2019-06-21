package cn.hxz.webapp.content.service;

import java.util.List;
import java.util.Map;

import cn.hxz.webapp.content.entity.RelatedFile;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * RelatedFileService.java Create on 2017-03-22 15:25:43
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface RelatedFileService {

	RelatedFile load(Long id);

	RelatedFile loadCached(Long id);
	
	Page<RelatedFile> find(Map<String, String> filters, PageRequest pageable, String orderBy);

	Page<RelatedFile> find(Long channelId, PageRequest pageable);

	Page<RelatedFile> find(Long channelId, PageRequest pageable, String orderBy);
	
	Page<RelatedFile> find(List<Long> channelIds, PageRequest pageable);
	
	Page<RelatedFile> find(List<Long> channelIds, PageRequest pageable, String orderBy);
	
	Page<RelatedFile> filter(List<Long> channelIds, Map<String,String> filters, PageRequest pageable);
	
	Page<RelatedFile> filter(List<Long> channelIds, Map<String,String> filters, PageRequest pageable, String orderBy);

	int create(RelatedFile entity);
	
	int update(RelatedFile entity);

	int enable(Long id, boolean curr);
	
	int remove(Long id);

}
