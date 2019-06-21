package cn.hxz.webapp.content.service;

import java.util.List;


import cn.hxz.webapp.content.entity.AdditionalField;
import cn.hxz.webapp.content.entity.Model;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * ModelSerivce.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ModelService {
	
	Model load(Long id);

	Model loadCached(Long id);
	
	List<AdditionalField> findAdditionalField(final Long modelId);
	
	Page<Model> find(PageRequest pageable);
}
