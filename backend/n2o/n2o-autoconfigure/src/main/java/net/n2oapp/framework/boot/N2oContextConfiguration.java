package net.n2oapp.framework.boot;

import com.sun.security.auth.UserPrincipal;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.ui.context.ConcurrentMapContextEngine;
import net.n2oapp.framework.ui.context.SessionContextEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

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

    @Bean
    @ConditionalOnMissingBean(DefaultHandshakeHandler.class)
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                return new UserPrincipal(UUID.randomUUID().toString());
            }
        };
    }
}
