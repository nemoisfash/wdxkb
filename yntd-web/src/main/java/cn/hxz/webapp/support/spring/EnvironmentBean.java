package cn.hxz.webapp.support.spring;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 
 * @author chenke
 * 
 */
public class EnvironmentBean implements EnvironmentAware {
	
	private static Environment env;

	public void setEnvironment(Environment environment) {
		EnvironmentBean.env = environment;		
	}
	
	public static Environment getEnvironment() {
		return EnvironmentBean.env;
	}
	
	//~========================================================================
	// methods	
	public boolean isDebug(){
		return env.getProperty("isDebug", Boolean.class, false);
	}
	
	public String getAdminPath(){
		return env.getProperty("adminPath", String.class, "/admin");
	}
	
	//~========================================================================
	// Environment methods
	
	/**
	 * Return the property value associated with the given key, or {@code null}
	 * if the key cannot be resolved.
	 * @param key the property name to resolve
	 * @see #getProperty(String, String)
	 * @see #getProperty(String, Class)
	 * @see #getRequiredProperty(String)
	 */
	public String getProperty(String key){
		return env.getProperty(key);
	}

	/**
	 * Return the property value associated with the given key, or
	 * {@code defaultValue} if the key cannot be resolved.
	 * @param key the property name to resolve
	 * @param defaultValue the default value to return if no value is found
	 * @see #getRequiredProperty(String)
	 * @see #getProperty(String, Class)
	 */
	public String getProperty(String key, String defaultValue){
		return env.getProperty(key, defaultValue);
	}

	/**
	 * Return the property value associated with the given key, or {@code null}
	 * if the key cannot be resolved.
	 * @param key the property name to resolve
	 * @param T the expected type of the property value
	 * @see #getRequiredProperty(String, Class)
	 */
	public <T> T getProperty(String key, Class<T> targetType){
		return env.getProperty(key, targetType);
	}

	/**
	 * Return the property value associated with the given key, or
	 * {@code defaultValue} if the key cannot be resolved.
	 * @param key the property name to resolve
	 * @param T the expected type of the property value
	 * @param defaultValue the default value to return if no value is found
	 * @see #getRequiredProperty(String, Class)
	 */
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue){
		return env.getProperty(key, targetType, defaultValue);
	}

}
