package net.n2oapp.framework.sandbox.autotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
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
import org.springframework.mock.web.MockHttpSession;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oEngineConfiguration.class,
        N2oMetadataConfiguration.class})
@SpringBootApplication(exclude = {N2oFrameworkAutoConfiguration.class})
@ComponentScan(value = {"net.n2oapp.framework.sandbox", "net.n2oapp.framework.autotest"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = net.n2oapp.framework.autotest.run.AutoTestApplication.class)
})
@PropertySource("classpath:sandbox.properties")
public class SandboxAutotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxAutotestApplication.class, args);
    }

    @Bean
    public SandboxTestDataProviderEngine dataProviderEngine() {
        return new SandboxTestDataProviderEngine() {

            @Value("${n2o.sandbox.project-id}")
            private String projectId;

            @Autowired
            private SandboxRestClient restClient;

            @Override
            protected InputStream getResourceInputStream(N2oTestDataProvider invocation) {
                return new ByteArrayInputStream(restClient.getFile(projectId, invocation.getFile(), new MockHttpSession()).getBytes());
            }

            @Override
            protected void updateFile(String filename) {
                try {
                    String mapAsJson = super.getObjectMapper().writeValueAsString(getRepositoryData(filename));
                    FileModel fileModel = new FileModel();
                    fileModel.setFile(filename);
                    fileModel.setSource(mapAsJson);
                    restClient.putFiles(projectId, Collections.singletonList(fileModel), new MockHttpSession());
                } catch (JsonProcessingException e) {
                    throw new N2oException(e);
                }
            }
        };
    }

    @Bean
    AlertMessageBuilder messageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       @Qualifier("sandboxPropertyResolver") PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }

    @Bean
    PropertyResolver sandboxPropertyResolver() {
        SandboxPropertyResolver propertyResolver = new SandboxPropertyResolver();
        propertyResolver.configure(new SimplePropertyResolver(PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties")), null, null);
        return propertyResolver;
    }
}
