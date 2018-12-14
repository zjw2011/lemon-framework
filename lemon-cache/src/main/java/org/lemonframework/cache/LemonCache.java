/**
 * Copyright (c) 2015-2017, Winter Lau (javayou@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lemonframework.cache;

import java.io.IOException;

/**
 * J2Cache 的缓存入口
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LemonCache {

	private final static String CONFIG_FILE = "/lemoncache.properties";

	private final static LemonCacheBuilder builder;

	static {
		try {
            LemonCacheConfig config = LemonCacheConfig.initFromConfig(CONFIG_FILE);
            builder = LemonCacheBuilder.init(config);
		} catch (IOException e) {
			throw new CacheException("Failed to load lemoncache configuration " + CONFIG_FILE, e);
		}
	}

	/**
	 * 返回缓存操作接口.
	 * @return CacheChannel
	 */
	public static CacheChannel getChannel(){
		return builder.getChannel();
	}

    /**
     * 关闭 J2Cache
     */
	public static void close() {
	    builder.close();
    }
}
