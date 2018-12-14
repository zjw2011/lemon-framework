package org.lemonframework.cache;

/**
 * When cached data expired in ehcache, this listener will be invoked.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface CacheExpiredListener {

	/**
	 * 缓存因为超时失效后触发的通知
	 * @param region 缓存 region
	 * @param key 缓存 key
	 */
	void notifyElementExpired(String region, String key) ;

}
