package net.n2oapp.framework.context;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.context.smart.impl.SmartContextEngine;
import net.n2oapp.framework.context.smart.impl.cache.ContextCacheTemplateImpl;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ContextConfiguration {

    @Bean
    public ContextEngine n2oContext(ContextCacheTemplateImpl cacheTemplate) {
        return new SmartContextEngine(cacheTemplate);
    }

    @Bean
    public ContextCacheTemplateImpl contextCacheTemplate(CacheManager cacheManager) {
        return new ContextCacheTemplateImpl(cacheManager);
    }
}
