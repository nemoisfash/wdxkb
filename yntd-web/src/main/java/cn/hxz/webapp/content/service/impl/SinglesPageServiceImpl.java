package cn.hxz.webapp.content.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.SinglesPage;
import cn.hxz.webapp.content.mapper.SinglesPageMapper;
import cn.hxz.webapp.content.service.SinglesPageService;
import tk.mybatis.mapper.entity.Example;

/**
 * SinglesPageSerivceImpl.java Create on 2017-01-16 23:12:09
 * <p>
 *  业务实现类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Service
public class SinglesPageServiceImpl implements SinglesPageService {

	@Autowired
	private SinglesPageMapper daoSinglesPage;

	public int create(SinglesPage entity){
		return daoSinglesPage.insert(entity);
	}
	
	public int update(SinglesPage entity){
		return daoSinglesPage.updateByPrimaryKeySelective(entity);
	}
	
	public SinglesPage load(Long id){
		return daoSinglesPage.selectByPrimaryKey(id);
	}
	
	public SinglesPage loadByChannelId(Long channelId){
		SinglesPage entity = new SinglesPage();
		entity.setChannelId(channelId);
		return daoSinglesPage.selectOne(entity);
	}
	
	public List<SinglesPage> findAll(int page, int size){
		Example example = new Example(SinglesPage.class);
		return daoSinglesPage.selectByExampleAndRowBounds(example, new RowBounds(page, size));
	}
}
