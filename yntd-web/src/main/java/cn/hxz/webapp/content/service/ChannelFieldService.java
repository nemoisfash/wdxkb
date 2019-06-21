package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.ChannelField;

/**
 * ChannelFieldSerivce.java Create on 2017-02-24 22:20:04
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ChannelFieldService {

	int create(ChannelField entity);
	
	int update(ChannelField entity);
	
	ChannelField load(Long key);
}
