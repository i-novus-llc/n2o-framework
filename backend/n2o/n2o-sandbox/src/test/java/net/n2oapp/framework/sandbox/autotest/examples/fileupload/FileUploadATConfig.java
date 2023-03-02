package net.n2oapp.framework.sandbox.autotest.examples.fileupload;

import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.sandbox.SandboxAlertMessagesConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import java.io.IOException;

@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class,
        FilesRestController.class})
@SpringBootApplication(exclude = N2oFrameworkAutoConfiguration.class)
@ComponentScan(basePackages = {"net.n2oapp.framework.autotest.run"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = net.n2oapp.framework.autotest.run.FileStoreController.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = net.n2oapp.framework.autotest.run.AutoTestApplication.class)})
public class FileUploadATConfig {
    public static void main(String[] args) {
        SpringApplication.run(FileUploadATConfig.class, args);
    }

    @Bean
    AlertMessageBuilder alertMessageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                            PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public AlertMessagesConstructor alertMessagesConstructor(AlertMessageBuilder messageBuilder) {
        return new SandboxAlertMessagesConstructor(messageBuilder);
    }

    @Bean
    public FileStorageController fileStorageController() throws IOException {
        return new FileStorageController();
    }

}
