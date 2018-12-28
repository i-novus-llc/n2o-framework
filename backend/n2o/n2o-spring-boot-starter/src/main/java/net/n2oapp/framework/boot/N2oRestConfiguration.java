package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import net.n2oapp.framework.mvc.cache.LifetimeClientCacheTemplate;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.query.GetController;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import net.n2oapp.framework.ui.servlet.AppConfigServlet;
import net.n2oapp.framework.ui.servlet.ExposedResourceBundleMessageSource;
import net.n2oapp.framework.ui.servlet.ModifiedClientCacheTemplate;
import net.n2oapp.framework.ui.servlet.data.DataServlet;
import net.n2oapp.framework.ui.servlet.page.PageServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Конфигурация контроллеров
 */
@Configuration
@ServletComponentScan("net.n2oapp.framework")
@ComponentScan(basePackages = "net.n2oapp.framework.ui", lazyInit = true)
public class N2oRestConfiguration {


    @Value("${n2o.ui.header.id:}")
    private String headerId;

    @Value("${n2o.api.url:/n2o}")
    private String n2oApiUrl;

    @Value("${n2o.format.date}")
    private String dataFormat;


    @Bean
    ControllerFactory controllerFactory(Map<String, SetController> setControllers, Map<String, GetController> getControllers) {
        Map<String, Object> controllers = new HashMap<>();
        controllers.putAll(setControllers);
        controllers.putAll(getControllers);
        return new N2oControllerFactory(controllers);
    }

    @Bean
    public DataController dataController(ControllerFactory controllerFactory,
                                         ObjectMapper n2oObjectMapper,
                                         MetadataEnvironment environment,
                                         MetadataRouter router) {
        return new DataController(controllerFactory, n2oObjectMapper, router, environment);
    }

    @Bean
    public ErrorMessageBuilder errorMessageBuilder(MessageSourceAccessor messageSourceAccessor) {
        return new ErrorMessageBuilder(messageSourceAccessor);
    }

    @Bean
    public ClientCacheTemplate pageClientCacheTemplate(CacheManager cacheManager, Environment env) {
        boolean enabled = env.getProperty("n2o.ui.cache.page.enabled", Boolean.class, false);
        if (!enabled)
            return null;
        long lifetime = env.getProperty("n2o.ui.cache.page.lifetime", Long.class, 1000 * 60 * 10L);
        String mode = env.getProperty("n2o.ui.cache.page.mode", String.class, "lifetime");
        switch (mode) {
            case "lifetime":
                return new LifetimeClientCacheTemplate(lifetime);
            case "modified":
                return new ModifiedClientCacheTemplate(cacheManager);
            default:
                throw new UnsupportedOperationException("Unknown page client cache mode " + mode);
        }
    }

    @Bean
    public ServletRegistrationBean pageServlet(MetadataEnvironment env, MetadataRouter router,
                                               @Qualifier("n2oObjectMapper") ObjectMapper n2oObjectMapper,
                                               ErrorMessageBuilder errorMessageBuilder,
                                               Optional<ClientCacheTemplate> pageClientCacheTemplate) {
        PageServlet pageServlet = new PageServlet();
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        pageServlet.setPipeline(pipeline);
        pageServlet.setRouter(router);
        pageServlet.setObjectMapper(n2oObjectMapper);
        pageServlet.setErrorMessageBuilder(errorMessageBuilder);
        pageClientCacheTemplate.ifPresent(pageServlet::setClientCacheTemplate);
        return new ServletRegistrationBean(pageServlet, n2oApiUrl + "/page/*");
    }

    @Bean
    public ServletRegistrationBean dataServlet(DataController controller,
                                               @Qualifier("n2oObjectMapper") ObjectMapper n2oObjectMapper,
                                               ErrorMessageBuilder errorMessageBuilder) {
        DataServlet dataServlet = new DataServlet(controller);
        dataServlet.setObjectMapper(n2oObjectMapper);
        dataServlet.setErrorMessageBuilder(errorMessageBuilder);
        return new ServletRegistrationBean(dataServlet, n2oApiUrl + "/data/*");
    }

    @Bean
    public ServletRegistrationBean appConfigServlet(Properties n2oProperties,
                                                    ContextProcessor contextProcessor,
                                                    @Qualifier("n2oObjectMapper") ObjectMapper n2oObjectMapper,
                                                    ExposedResourceBundleMessageSource clientMessageSource,
                                                    MetadataEnvironment env) {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        writer.setContextProcessor(contextProcessor);
        writer.setProperties(n2oProperties);
        writer.setObjectMapper(n2oObjectMapper);
        writer.setPath("classpath*:META-INF/config.json");
        writer.setOverridePath("classpath*:META-INF/config-build.json");

        AppConfigServlet appConfigServlet = new AppConfigServlet();
        appConfigServlet.setAppConfigJsonWriter(writer);
        appConfigServlet.setMessageSource(clientMessageSource);
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        appConfigServlet.setPipeline(pipeline);
        appConfigServlet.setHeaderSourceId(headerId);
        return new ServletRegistrationBean(appConfigServlet, n2oApiUrl + "/config", "/n2o/config.json");
    }

}
