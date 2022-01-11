package net.n2oapp.framework.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan("net.n2oapp.framework.boot.websocket")
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
}
