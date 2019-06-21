package cn.hxz.webapp.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.ModelField;
import cn.hxz.webapp.content.mapper.ModelFieldMapper;
import cn.hxz.webapp.content.service.ModelFieldService;

/**
 * ModelFieldSerivceImpl.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class ModelFieldServiceImpl implements ModelFieldService {

	@Autowired
	private ModelFieldMapper daoModelField;

	public ModelField load(Long key){
		return daoModelField.selectByPrimaryKey(key);
	}
}
