package net.n2oapp.framework.autotest.test;

import net.n2oapp.framework.boot.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

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
}
