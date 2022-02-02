package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.ui.context.ConcurrentMapContextEngine;
import net.n2oapp.framework.ui.context.SessionContextEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Конфигурация пользовательского контекста
 */
@Configuration
@ImportResource("classpath*:META-INF/n2o-context-ext-context.xml")
public class N2oContextConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public ContextEngine contextEngine() {
        return new ConcurrentMapContextEngine();
    }

    @Bean
    @ConditionalOnBean(SessionContextEngine.class)
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Bean
    public StaticUserContext staticUserContext(ContextEngine contextEngine) {
        return new StaticUserContext(contextEngine);
    }
}
