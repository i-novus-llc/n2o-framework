package net.n2oapp.framework.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxContext;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.query.GetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
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
@ComponentScan("net.n2oapp.framework.sandbox")
public class N2oSandboxConfiguration {

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
    public SandboxRestClient restClient() {
        return new SandboxRestClientImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SandboxApplicationBuilderConfigurer sandboxApplicationBuilderConfigurer() {
        return new SandboxApplicationBuilderConfigurer();
    }
}
