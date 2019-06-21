package cn.hxz.webapp.content.service;

import java.util.List;

import cn.hxz.webapp.content.entity.SinglesPage;

/**
 * SinglesPageSerivce.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface SinglesPageService {

	int create(SinglesPage entity);
	
	int update(SinglesPage entity);
	
	SinglesPage load(Long id);
	
	SinglesPage loadByChannelId(Long channelId);
	
	List<SinglesPage> findAll(int page, int size);
}
