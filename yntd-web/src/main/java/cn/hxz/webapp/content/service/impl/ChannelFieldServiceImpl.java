package cn.hxz.webapp.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.ChannelField;
import cn.hxz.webapp.content.mapper.ChannelFieldMapper;
import cn.hxz.webapp.content.service.ChannelFieldService;

/**
 * ChannelFieldSerivceImpl.java Create on 2017-02-24 22:20:04
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ChannelFieldServiceImpl implements ChannelFieldService {

	@Autowired
	private ChannelFieldMapper daoChannelField;

	public int create(ChannelField entity){
		return daoChannelField.insert(entity);
	}
	
	public int update(ChannelField entity){
		return daoChannelField.updateByPrimaryKeySelective(entity);
	}
	
	public ChannelField load(Long key){
		return daoChannelField.selectByPrimaryKey(key);
	}
}
