package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.boot.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * Веб сервер для прогона автотестов
 */
@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oCommonConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class})
@SpringBootApplication(exclude = N2oFrameworkAutoConfiguration.class)
public class AutoTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoTestApplication.class, args);
    }

    @Bean
    ErrorMessageBuilder errorMessageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor) {
        return new ErrorMessageBuilder(messageSourceAccessor);
    }
}
