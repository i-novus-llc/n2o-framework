package net.n2oapp.framework.config.ehcache;

import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.framework.config.ehcache.memory.N2oMemoryService;
import net.n2oapp.framework.config.ehcache.monitoring.CacheLogger;
import net.n2oapp.framework.config.ehcache.monitoring.CacheTuner;
import net.n2oapp.framework.config.ehcache.monitoring.service.CacheConfigService;
import net.n2oapp.framework.config.ehcache.monitoring.service.CacheStatisticService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;

@Configuration
public class EhcacheConfiguration {


    @Bean
    @Primary
    public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager cacheManager) {
        EhCacheCacheManager manager = new EhCacheCacheManager();
        manager.setCacheManager(cacheManager);
        return manager;
    }

    @Bean
    public FactoryBean ehcache(@Value("${n2o.config.ehcache.resource}") Resource path) throws MalformedURLException {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(path);
        factoryBean.setShared(false);
        return factoryBean;
    }

    @Bean
    public CollectionPageService cacheStatisticService(CacheTuner tuner) {
        return new CacheStatisticService(tuner);
    }

    @Bean
    public CollectionPageService cacheConfigService(CacheTuner tuner) {
        return new CacheConfigService(tuner);
    }

    @Bean
    public CacheLogger cacheLogger(CacheTuner cacheTuner) {
        return new CacheLogger(cacheTuner);
    }

    @Bean
    public CacheTuner cacheTuner(CacheManager cacheManager, @Value("${n2o.config.ehcache.monitoring.on}") String monitoring) {
        return new CacheTuner(cacheManager, monitoring);
    }

    @Bean
    public CollectionPageService n2oMemoryService() {
        return new N2oMemoryService();
    }
}
