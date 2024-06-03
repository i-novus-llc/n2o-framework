package net.n2oapp.framework.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.jackson.SingletonTypeIdHandlerInstantiator;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import net.n2oapp.framework.boot.stomp.WebSocketController;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@AutoConfiguration
@EnableWebSocketMessageBroker
@ConditionalOnClass(WebSocketMessageBrokerConfigurer.class)
@Import(N2oWebSocketHandshakeHandlerConfiguration.class)
public class N2oWebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private DefaultHandshakeHandler handshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/n2o/ws")
                .setHandshakeHandler(handshakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    public WebSocketController wsController(MetadataEnvironment environment, SingletonTypeIdHandlerInstantiator instantiator) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new Jdk8Module(), new JavaTimeModule());
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setHandlerInstantiator(instantiator);
        return new N2oWebSocketController(N2oPipelineSupport.readPipeline(environment), environment, mapper);
    }
}
