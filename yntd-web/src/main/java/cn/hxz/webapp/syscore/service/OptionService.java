package cn.hxz.webapp.syscore.service;

import cn.hxz.webapp.syscore.entity.Option;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * OptionService.java Create on 2017-03-20 16:03:57
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface OptionService {

	Option load(Long id);

	Option loadCached(Long id);

	Page<Option> find(Long groupId, Boolean enabled, PageRequest pageable);

	Page<Option> find(Long groupId, Boolean enabled, PageRequest pageable, String orderBy);
	
	Page<Option> find(String code, Boolean enabled, PageRequest pageable);
	
	Page<Option> find(String code, Boolean enabled, PageRequest pageable, String orderBy);

	int create(Option entity);
	
	int update(Option entity);

	int enable(Long id, boolean curr);
	
	int remove(Long id);

}
