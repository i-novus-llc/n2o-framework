package net.n2oapp.framework.config.ehcache;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
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

}
