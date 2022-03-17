package net.n2oapp.framework.sandbox.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.boot.*;
import net.n2oapp.framework.sandbox.server.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.server.view.SandboxContext;
import net.n2oapp.framework.sandbox.server.view.SandboxPropertyResolver;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.query.GetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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
@ComponentScan(basePackages = {"net.n2oapp.framework.api", "net.n2oapp.framework.ui", "net.n2oapp.framework.sandbox.server", "net.n2oapp.framework.sandbox.server.cases"}, lazyInit = true)
@PropertySource("classpath:sandbox.properties")
public class N2oSandboxConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperConstructor.metaObjectMapper();
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
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("redirect:/editor/");
                registry.addViewController("/view/*/").setViewName("forward:/index.html");
                registry.addViewController("/editor/*/").setViewName("forward:/editor/index.html");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/view/*/static/**")
                        .addResourceLocations("/static/").resourceChain(true).addResolver(new WebStaticResolver("META-INF/resources"));

                registry.addResourceHandler(
                        "/view/*/serviceWorker.js",
                        "/view/*/manifest.json",
                        "/view/*/favicon.ico",
                        "/editor/*/*",
                        "/editor/*/static/**"
                )
                        .addResourceLocations("/editor/static/").resourceChain(true).addResolver(new WebStaticResolver("META-INF/resources/editor"));
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }
        };
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


    /*
    решили пока зкомментировать и отключить кеширование, потому что есть проблемы с обновлением application.properties
    @Bean
    public ProjectFileUpdateListener projectFileUpdateListener(CacheManager cacheManager, SourceTypeRegister sourceTypeRegister) {
        return new ProjectFileUpdateListener(sourceTypeRegister, cacheManager);
    }
*/

}
