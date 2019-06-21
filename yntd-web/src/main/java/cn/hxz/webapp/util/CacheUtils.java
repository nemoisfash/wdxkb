package cn.hxz.webapp.util;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import net.chenke.playweb.support.spring.ApplicationContextBean;

/**
 * 
 * @author chenke
 *
 */
public class CacheUtils {

	public final static String CACHE_SYS = "cache_sys";
	public final static String CACHE_CMS = "cache_cms";
	public final static String CACHE_EXT = "cache_ext";

	private static CacheManager cacheManager;

	private static CacheManager getCacheManager() {
		if (cacheManager == null) {
			cacheManager = ApplicationContextBean.getBean(CacheManager.class);
		}
		return cacheManager;
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> T get(String name, String key) {
		ValueWrapper valueWrapper = getCacheManager().getCache(name).get(key);
		if (valueWrapper == null)
			return null;
		else
			return (T) valueWrapper.get();
	}

	public static void put(String cache, String key, Object value) {
		getCacheManager().getCache(cache).put(key, value);
	}

	public static void evict(String cache, String key) {
		getCacheManager().getCache(cache).evict(key);
	}

	public static void clear(String cache) {
		getCacheManager().getCache(cache).clear();
	}

	public static void evictUser(Long id) {
		evict(CACHE_SYS, String.format("sys_account_%s", id));
	}

	public static void evictUser(String username) {
		evict(CACHE_SYS, String.format("sys_account_username_%s", username));
	}
}
