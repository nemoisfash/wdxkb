package cn.feeboo.webapp.content;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.hxz.webapp.content.mapper.ArticleMapper;
import net.chenke.AbstractSqlMapperTest;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * ArticleAttrTest.java Create on 2017年2月24日 下午11:58:19
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class ArticleTest extends AbstractSqlMapperTest {
	
	@Test
	public void selectWithJoinAndResultTypeIsTypedMapTest() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		//第3页，每页12条记录
		PageRequest pageable = new PageRequest(3, 12);
		try {
			// 获取Mapper
			ArticleMapper mapper = sqlSession.getMapper(ArticleMapper.class);
			// 使用分页插件
			//PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
			
			List<Map<String,Object>> items = mapper.selectJoinTest(pageable);
			Page<Map<String,Object>> page = new PageImpl<Map<String,Object>>(items, pageable);			
			System.out.println(page.getTotalElements());
			System.out.println(items);
			for (Map<String,Object> item : page.getContent()){
				System.out.println(item);
			}
			
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
}
