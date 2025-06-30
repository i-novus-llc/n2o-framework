package net.n2oapp.framework.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.FileStorageOnDisk;
import net.n2oapp.framework.sandbox.file_storage.S3YandexFileStorage;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxContext;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.query.GetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Sandbox
 */
@Configuration
@Import({N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oEnvironmentConfiguration.class,
        N2oMetadataConfiguration.class,
        N2oEngineConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableCaching
@ComponentScan(basePackages = {"net.n2oapp.framework.sandbox", "net.n2oapp.framework.autotest.cases"})
public class N2oSandboxConfiguration {

    @Value("${yandex.s3.access-key:#{null}}")
    private String accessKey;
    @Value("${yandex.s3.secret-key:#{null}}")
    private String secretKey;
    @Value("${yandex.s3.url:#{null}}")
    private String endpoint;
    @Value("${yandex.s3.bucket:#{null}}")
    private String bucket;

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperConstructor.metaObjectMapper();
    }

    @Bean
    public WebMvcConfigurer mvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/view/*/static/**")
                        .addResourceLocations("/static/")
                        .resourceChain(true)
                        .addResolver(new WebStaticResolver("META-INF/resources"));
            }
        };
    }

    @Bean
    public ControllerFactory controllerFactory(Map<String, SetController> setControllers,
                                               Map<String, GetController> getControllers) {
        Map<String, Object> controllers = new HashMap<>();
        controllers.putAll(setControllers);
        controllers.putAll(getControllers);
        return new N2oControllerFactory(controllers);
    }

    @Bean
    public PropertyResolver propertyResolver() {
        return new SandboxPropertyResolver();
    }

    @Bean
    public AlertMessageBuilder alertMessageBuilder(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                                   PropertyResolver propertyResolver) {
        return new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public AlertMessagesConstructor alertMessagesConstructor(AlertMessageBuilder messageBuilder) {
        return new SandboxAlertMessagesConstructor(messageBuilder);
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {

            @Autowired
            private SandboxTestDataProviderEngine dataProviderEngine;

            @Autowired
            private SandboxContext sandboxContext;

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                HttpSession session = se.getSession();
                dataProviderEngine.deleteSessionDataSets(session);
                sandboxContext.deleteSessionProjectsProperties(session);
            }
        };
    }

    @Bean
    @ConditionalOnProperty(name = "yandex.s3.url")
    public FileStorage s3YandexFileStorage(S3Client s3YandexClient) {
        return new S3YandexFileStorage(s3YandexClient, bucket);
    }

    @Bean
    @ConditionalOnProperty(name = "yandex.s3.url")
    public S3Client s3YandexClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("Stub"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileStorage fileStorageOnDisk() {
        return new FileStorageOnDisk();
    }

    @Bean
    @ConditionalOnMissingBean
    public SandboxApplicationBuilderConfigurer sandboxApplicationBuilderConfigurer() {
        return new SandboxApplicationBuilderConfigurer();
    }
}
