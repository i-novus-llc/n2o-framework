package net.n2oapp.framework.sandbox.autotest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.sandbox.autotest.examples.fileupload.FileStorageController;
import net.n2oapp.framework.sandbox.autotest.examples.fileupload.FilesRestController;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.properties.reader.PropertiesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Веб сервер для прогона автотестов по примерам сендбокса
 */
@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class,
        FileStorageController.class,
        FilesRestController.class,
        net.n2oapp.framework.sandbox.autotest.examples.fileupload.FileModel.class})
@SpringBootApplication(exclude = {N2oFrameworkAutoConfiguration.class})
@ComponentScan(value = {"net.n2oapp.framework.autotest", "net.n2oapp.framework.sandbox.autotest.examples.fileupload"},  excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = net.n2oapp.framework.autotest.run.AutoTestApplication.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = net.n2oapp.framework.autotest.run.FileStoreController.class)})
@PropertySource("classpath:sandbox.properties")
public class SandboxAutotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxAutotestApplication.class, args);
    }

    @Bean
    public SandboxTestDataProviderEngine dataProviderEngine() {
        SandboxTestDataProviderEngine dataProviderEngine = new SandboxTestDataProviderEngine() {

            @Value("${n2o.sandbox.project-id}")
            private String projectId;

            @Autowired
            private SandboxRestClient restClient;

            @Override
            protected InputStream getResourceInputStream(N2oTestDataProvider invocation) throws IOException {
                ClassPathResource classPathResource = new ClassPathResource(invocation.getFile());
                if (classPathResource.exists()) {
                    return classPathResource.getInputStream();
                }
                return new ByteArrayInputStream(restClient.getFile(projectId, invocation.getFile(), new MockHttpSession()).getBytes());
            }

            @Override
            protected void updateFile(String filename) {
            }
        };
        dataProviderEngine.setReadonly(true);
        return dataProviderEngine;
    }

    @Bean
    public SandboxRestClient restClient() {
        return new SandboxRestClientImpl();
    }

    @Bean
    AlertMessageBuilder messageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }

    @Bean
    @Primary
    PropertyResolver sandboxPropertyResolver() {
        SandboxPropertyResolver propertyResolver = new SandboxPropertyResolver();
        propertyResolver.configure(new SimplePropertyResolver(PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties")), null, null);
        return propertyResolver;
    }

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return ObjectMapperConstructor.metaObjectMapper();
    }
}
