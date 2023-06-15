package net.n2oapp.framework.boot;

import com.sun.security.auth.UserPrincipal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
public class N2oWebSocketHandshakeHandlerConfiguration {

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
