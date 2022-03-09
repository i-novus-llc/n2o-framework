package net.n2oapp.framework.autotest.run;

import com.sun.security.auth.UserPrincipal;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Веб сервер для прогона автотестов
 */
@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class})
@SpringBootApplication(exclude = {N2oFrameworkAutoConfiguration.class})
@ComponentScan("net/n2oapp/framework/autotest")
public class AutoTestApplication {

    @Value("${n2o.stomp.session-id}")
    private String sessionId;

    public static void main(String[] args) {
        SpringApplication.run(AutoTestApplication.class, args);
    }

    @Bean
    AlertMessageBuilder messageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                return new UserPrincipal(sessionId);
            }
        };
    }
}
