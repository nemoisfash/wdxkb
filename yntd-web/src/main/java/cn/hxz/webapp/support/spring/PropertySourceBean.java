package cn.hxz.webapp.support.spring;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * It does not work with @Configure class when you point 
 * to xml with @ImportResource("classpath:properties.xml"), 
 * but works when you load it with applicationcontext.
 * 
 * @author chenke
 *
 */
public class PropertySourceBean implements InitializingBean, ApplicationContextAware {
	
	private Properties properties;

	private ApplicationContext applicationContext;

	public PropertySourceBean() {
    }

	public void afterPropertiesSet() throws Exception {

		PropertiesPropertySource propertySource = new PropertiesPropertySource("environmentProps", this.properties);

		ConfigurableEnvironment environment = (ConfigurableEnvironment) this.applicationContext.getEnvironment();

		environment.getPropertySources().addFirst(propertySource);

	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;

	}
}
