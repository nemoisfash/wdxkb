package cn.feeboo.webapp.content;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.hxz.webapp.content.entity.ArticleAttr;
import cn.hxz.webapp.content.mapper.ArticleAttrMapper;
import net.chenke.AbstractSqlMapperTest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ArticleAttrTest.java Create on 2017年2月24日 下午11:58:19
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class ArticleAttrTest extends AbstractSqlMapperTest {
	
	@Test
	public void selectOneByCompositePrimaryKey() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取Mapper
			ArticleAttrMapper mapper = sqlSession.getMapper(ArticleAttrMapper.class);
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("id", 99);
//			map.put("field", "field1");
			ArticleAttr pk = new ArticleAttr();
			pk.setId(99L);
			pk.setField("field1");
			ArticleAttr item = mapper.selectByPrimaryKey(pk);
			System.out.println(item);
			if (item != null){
				System.out.print("\""+item.getField()+"\":");
				System.out.println(item.getValue());
			}
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
	
	@Test
	public void selectByArticleId() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取Mapper
			ArticleAttrMapper mapper = sqlSession.getMapper(ArticleAttrMapper.class);
			Example example = new Example(ArticleAttr.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("id", 99);
			List<ArticleAttr> items = mapper.selectByExample(example);
			System.out.println(items);
			for (ArticleAttr item : items){
				System.out.print("\""+item.getField()+"\":");
				System.out.println(item.getValue());
			}
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

}
