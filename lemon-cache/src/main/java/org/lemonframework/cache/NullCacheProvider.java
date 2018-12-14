package org.lemonframework.cache;

import java.util.Collection;
import java.util.Properties;

/**
 * 空缓存提供.
 * @author jiawei zhang
 * @since 0.0.1
 */
public class NullCacheProvider implements CacheProvider {

	private final static NullCache cache = new NullCache();

	@Override
	public String name() {
		return "none";
	}

	@Override
	public int level() {
		return CacheObject.LOCAL | CacheObject.ClUSTER;
	}

	@Override
	public Cache buildCache(String regionName, CacheExpiredListener listener) throws CacheException {
		return cache;
	}

	@Override
	public Cache buildCache(String region, long timeToLiveInSeconds, CacheExpiredListener listener) {
		return cache;
	}

	@Override
	public void start(Properties props) throws CacheException {
	}

	@Override
	public Collection<CacheChannel.Region> regions() {
		return null;
	}

	@Override
	public void stop() {
	}

}
