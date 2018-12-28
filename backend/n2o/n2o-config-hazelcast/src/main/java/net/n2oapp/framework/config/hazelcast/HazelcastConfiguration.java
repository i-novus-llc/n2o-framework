package net.n2oapp.framework.config.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

@ImportResource("classpath:net/n2oapp/framework/config/hazelcast/hazelcast-local-config.xml")
@Configuration
public class HazelcastConfiguration {

    @Bean
    @Primary
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        HazelcastCacheManager manager = new HazelcastCacheManager();
        manager.setHazelcastInstance(hazelcastInstance);
        return manager;
    }

}
