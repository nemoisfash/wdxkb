package net.chenke;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;

import net.chenke.playweb.support.mybatis.DynaMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * AbstractDynaMapperTest.java Create on 2017年1月16日 下午12:50:53
 *
 * @author cn.feeboo
 * @version 1.0
 */
public abstract class AbstractSqlMapperTest {

	protected static SqlSessionFactory sqlSessionFactory;

	@BeforeClass
	public static void setUp() throws Exception {
		// create a SqlSessionFactory
		Reader reader = Resources.getResourceAsReader("sqlmap-config.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		reader.close();
		
		MapperHelper mapperHelper = new MapperHelper();
		//特殊配置
		Config config = new Config();
		config.setEnableMethodAnnotation(true);
		mapperHelper.setConfig(config);
		//注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
		mapperHelper.registerMapper(DynaMapper.class);
		//配置完成后，执行下面的操作
		mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
	}
}
