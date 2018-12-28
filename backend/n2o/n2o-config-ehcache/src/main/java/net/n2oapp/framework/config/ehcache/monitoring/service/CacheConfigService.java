package net.n2oapp.framework.config.ehcache.monitoring.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.framework.config.ehcache.monitoring.CacheTuner;
import net.n2oapp.framework.config.ehcache.monitoring.api.CacheConfig;
import net.n2oapp.framework.config.ehcache.monitoring.api.CacheCriteria;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;

import java.util.Collections;

/**
 * @author V. Alexeev.
 */
public class CacheConfigService implements CollectionPageService<CacheCriteria, CacheConfig> {

    private final CacheTuner tuner;

    public CacheConfigService(CacheTuner tuner) {
        this.tuner = tuner;
    }

    @Override
    public CollectionPage<CacheConfig> getCollectionPage(CacheCriteria cacheCriteria) {
        CacheConfig cacheConfig = map(tuner.cache(cacheCriteria.name));
        return new FilteredCollectionPage<>(Collections.singletonList(cacheConfig), cacheCriteria);
    }

    public CacheConfig map(Ehcache ehcache) {
        CacheConfig config = new CacheConfig();
        config.name = ehcache.getName();
        CacheConfiguration configuration = ehcache.getCacheConfiguration();
        config.isFrozen = configuration.isFrozen();
        config.timeToLive = configuration.getTimeToLiveSeconds() != 0 ? configuration.getTimeToLiveSeconds() : null;
        config.timeToIdle = configuration.getTimeToIdleSeconds() != 0 ? configuration.getTimeToIdleSeconds() : null;
        config.maxEntriesLocalHeap = configuration.getMaxEntriesLocalHeap() != 0 ? configuration.getMaxEntriesLocalHeap() : null;
        config.maxEntriesLocalDisk = configuration.getMaxEntriesLocalDisk() != 0 ? configuration.getMaxEntriesLocalDisk() : null;
        config.maxBytesLocalHeap = configuration.getMaxBytesLocalHeap() != 0 ? configuration.getMaxBytesLocalHeap() : null;
        config.maxBytesLocalDisk = configuration.getMaxBytesLocalDisk() != 0 ? configuration.getMaxBytesLocalDisk() : null;
//        config.percentOfTotal = configuration
        config.isOverflowToOffHeap = configuration.isOverflowToOffHeap();
        return config;
    }
}
