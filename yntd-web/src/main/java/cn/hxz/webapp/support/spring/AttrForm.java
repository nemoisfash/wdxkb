package cn.hxz.webapp.support.spring;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * MapForm.java Create on 2017年4月1日 下午3:41:39
 *
 * @author cn.feeboo
 * @version 1.0
 */
public class AttrForm<T> {
	private Map<String, T> attr = new LinkedHashMap<String, T>();

	public Map<String, T> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, T> attr) {
		this.attr = attr;
	}
	
	public Set<String> keySet(){
		return this.attr.keySet();
	}
	
	public T get(String key){
		return this.attr.get(key);
	}
}
