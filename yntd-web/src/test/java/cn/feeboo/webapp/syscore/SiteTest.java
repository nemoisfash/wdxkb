package cn.feeboo.webapp.syscore;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.mapper.SiteMapper;
import junit.framework.Assert;
import net.chenke.AbstractSqlMapperTest;

/**
 * SiteTest.java Create on 2017年1月16日 下午12:53:38
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class SiteTest extends AbstractSqlMapperTest {
	
	@Test
	public void insert() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取Mapper
			SiteMapper mapper = sqlSession.getMapper(SiteMapper.class);
			Site entity = new Site();
			entity.setName("测试站点");
			// 新增一条数据
			Assert.assertEquals(1, mapper.insert(entity));
			// ID回写,不为空
			Assert.assertNotNull(entity.getId());
			// 通过主键删除新增的数据
//			Assert.assertEquals(1, mapper.deleteByPrimaryKey(entity.getId()));
		} finally {
			sqlSession.close();
		}
	}
}
