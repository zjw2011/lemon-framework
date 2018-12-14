package org.lemonframework.cache;

/**
 * J2Cache exception.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class CacheException extends RuntimeException {

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Throwable e) {
		super(s, e);
	}

	public CacheException(Throwable e) {
		super(e);
	}
	
}
