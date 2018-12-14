package org.lemonframework.cache.ehcache;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.xml.XmlConfiguration;
import org.lemonframework.cache.CacheChannel;
import org.lemonframework.cache.CacheExpiredListener;
import org.lemonframework.cache.CacheObject;
import org.lemonframework.cache.CacheProvider;

/**
 * EhCache 3.x 缓存管理器的封装，用来管理多个缓存区域
 *
 * @author Winter Lau(javayou@gmail.com)
 */
public class EhCacheProvider implements CacheProvider {

    private final static Logger log = LoggerFactory.getLogger(EhCacheProvider.class);

    private final static String DEFAULT_TPL = "default";
    private CacheManager manager;
    private ConcurrentHashMap<String, EhCache> caches = new ConcurrentHashMap<>();
    private long defaultHeapSize = 1000;

    @Override
    public String name() {
        return "ehcache3";
    }

    @Override
    public int level() {
        return CacheObject.LOCAL;
    }

    @Override
    public Collection<CacheChannel.Region> regions() {
        Collection<CacheChannel.Region> regions = new ArrayList<>();
        caches.forEach((k,c) -> regions.add(new CacheChannel.Region(k, c.size(), c.ttl())));
        return regions;
    }

    @Override
    public EhCache buildCache(String region, CacheExpiredListener listener) {
        return caches.computeIfAbsent(region, v -> {
            org.ehcache.Cache cache = manager.getCache(region, String.class, Serializable.class);
            if (cache == null) {
                CacheConfiguration defaultCacheConfig = manager.getRuntimeConfiguration().getCacheConfigurations().get(DEFAULT_TPL);
                CacheConfiguration<String, Serializable> cacheCfg = CacheConfigurationBuilder.newCacheConfigurationBuilder(defaultCacheConfig).build();
                cache = manager.createCache(region, cacheCfg);
                Duration dura = cache.getRuntimeConfiguration().getExpiry().getExpiryForCreation(null, null);
                long ttl = dura.isInfinite()?-1:dura.getTimeUnit().toSeconds(dura.getLength());
                log.warn("Could not find configuration [{}]; using defaults (TTL:{} seconds).", region, ttl);
            }
            return new EhCache(region, cache, listener);
        });
    }

    @Override
    public EhCache buildCache(String region, long timeToLiveInSeconds, CacheExpiredListener listener) {
        EhCache ehcache = caches.computeIfAbsent(region, v -> {
            CacheConfiguration<String, Object> conf = CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    String.class, Object.class, ResourcePoolsBuilder.heap(defaultHeapSize))
                    .withExpiry(Expirations.timeToLiveExpiration(Duration.of(timeToLiveInSeconds, TimeUnit.SECONDS)))
                    .build();
            org.ehcache.Cache cache = manager.createCache(region, conf);
            log.info("Started Ehcache region [{}] with TTL: {}", region, timeToLiveInSeconds);
            return new EhCache(region, cache, listener);
        });

        if (ehcache.ttl() != timeToLiveInSeconds) {
            throw new IllegalArgumentException(String.format("Region [%s] TTL %d not match with %d", region, ehcache.ttl(), timeToLiveInSeconds));
        }

        return ehcache;
    }

    @Override
    public void removeCache(String region) {
        caches.remove(region);
        manager.removeCache(region);
    }

    @Override
    public void start(Properties props) {
        String sDefaultHeapSize = props.getProperty("defaultHeapSize");
        try {
            this.defaultHeapSize = Long.parseLong(sDefaultHeapSize);
        }catch(Exception e) {
            log.warn("Failed to read ehcache3.defaultHeapSize = {} , use default {}", sDefaultHeapSize, defaultHeapSize);
        }
        String configXml = props.getProperty("configXml");
        if(configXml == null || configXml.trim().length() == 0) {
            configXml = "/ehcache3.xml";
        }
        URL myUrl = getClass().getResource(configXml);
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        manager = CacheManagerBuilder.newCacheManager(xmlConfig);
        manager.init();
    }

    @Override
    public void stop() {
        if (manager != null) {
            manager.close();
            caches.clear();
            manager = null;
        }
    }

}
