package cn.feeboo.webapp.syscore;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;

import net.chenke.AbstractSqlMapperTest;

/**
 * SysCoreTest.java Create on 2017年4月25日 下午3:30:55
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class SysCoreTest extends AbstractSqlMapperTest {
	
	public void createSchema() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Connection connection = session.getConnection();
			Reader reader = Resources.getResourceAsReader("casttc.sql");
			ScriptRunner runner = new ScriptRunner(connection);
			runner.setLogWriter(null);
			runner.runScript(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
