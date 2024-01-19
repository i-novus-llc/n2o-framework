package net.n2oapp.cache.template;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@TestConfiguration
public class CacheTestConfig {

    @Bean
    public EhCacheManagerFactoryBean  ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("net/n2oapp/cache/template/ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);

        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        return new EhCacheCacheManager(Objects.requireNonNull(ehCacheManagerFactoryBean.getObject()));
    }
}
