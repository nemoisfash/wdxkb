package cn.hxz.webapp.syscore.service;

import cn.hxz.webapp.syscore.entity.OptionGroup;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * OptionGroupService.java Create on 2017-03-20 16:03:58
 * <p>
 *  业务接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface OptionGroupService {

	OptionGroup load(Long id);

	OptionGroup loadCached(Long id);
	
	OptionGroup load(String code);

	Page<OptionGroup> find(Boolean enabled, PageRequest pageable);

	Page<OptionGroup> find(Boolean enabled, PageRequest pageable, String orderBy);

	int create(OptionGroup entity);
	
	int update(OptionGroup entity);

	int enable(Long id, boolean curr);
	
	int remove(Long id);
}
