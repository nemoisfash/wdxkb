package cn.feeboo.webapp.content;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.mapper.ChannelMapper;
import junit.framework.Assert;
import net.chenke.AbstractSqlMapperTest;

/**
 * ChannelTest.java Create on 2017年1月16日 上午11:37:18
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class ChannelTest extends AbstractSqlMapperTest {


	@Test
	public void insert() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取Mapper
			ChannelMapper mapper = sqlSession.getMapper(ChannelMapper.class);
			Channel entity = new Channel();
			entity.setName("测试栏目");
			entity.setNode("test");
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
