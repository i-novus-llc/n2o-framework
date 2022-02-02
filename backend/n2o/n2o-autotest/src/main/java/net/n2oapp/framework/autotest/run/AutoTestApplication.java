package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

/**
 * Веб сервер для прогона автотестов
 */
@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class})
@SpringBootApplication(exclude = {N2oFrameworkAutoConfiguration.class, N2oWebSocketAutoConfiguration.class})
@ComponentScan("net/n2oapp/framework/autotest")
public class AutoTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoTestApplication.class, args);
    }

    @Bean
    AlertMessageBuilder messageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }
}
