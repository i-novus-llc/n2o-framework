package net.n2oapp.framework.boot;

import com.sun.security.auth.UserPrincipal;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import net.n2oapp.framework.boot.stomp.WebSocketController;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnClass(WebSocketMessageBrokerConfigurer.class)
public class N2oWebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private DefaultHandshakeHandler handshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/n2o/ws")
                .setHandshakeHandler(handshakeHandler)
                .withSockJS();
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

    @Bean
    public WebSocketController wsController(MetadataEnvironment environment) {
        return new N2oWebSocketController(N2oPipelineSupport.readPipeline(environment), environment);
    }
}
