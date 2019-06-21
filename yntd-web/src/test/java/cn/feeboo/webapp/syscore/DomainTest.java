package cn.feeboo.webapp.syscore;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.hxz.webapp.syscore.entity.Domain;
import cn.hxz.webapp.syscore.mapper.DomainMapper;
import junit.framework.Assert;
import net.chenke.AbstractSqlMapperTest;

/**
 * DomainTest.java Create on 2017年1月16日 下午12:50:01
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class DomainTest extends AbstractSqlMapperTest {

	@Test
	public void insert() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取Mapper
			DomainMapper mapper = sqlSession.getMapper(DomainMapper.class);
			Domain entity = new Domain();
			entity.setId(1L);
			entity.setDomain("www.casttc.local");
			entity.setSkin("zh");
			entity.setSiteId(1L);
			// 新增一条数据
			Assert.assertEquals(1, mapper.insert(entity));
			// ID回写,不为空
			Assert.assertNotNull(entity.getId());
			// 通过主键删除新增的数据
//			Assert.assertEquals(1, mapper.deleteByPrimaryKey(entity.getId()));
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
}
