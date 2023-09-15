package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import net.n2oapp.framework.mvc.cache.LifetimeClientCacheTemplate;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import net.n2oapp.framework.ui.servlet.AppConfigServlet;
import net.n2oapp.framework.ui.servlet.ExposedResourceBundleMessageSource;
import net.n2oapp.framework.ui.servlet.ModifiedClientCacheTemplate;
import net.n2oapp.framework.ui.servlet.data.DataServlet;
import net.n2oapp.framework.ui.servlet.page.PageServlet;
import net.n2oapp.framework.ui.servlet.table.ExportServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration
public class N2oServletConfiguration {
    @Value("${n2o.application.id:}")
    private String applicationId;

    @Value("${n2o.api.url:/n2o}")
    private String n2oApiUrl;

    @Bean
    @ConditionalOnProperty(name = "n2o.ui.cache.page.enabled", havingValue = "true")
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
                                               AlertMessageBuilder messageBuilder,
                                               SubModelsProcessor subModelsProcessor,
                                               Optional<ClientCacheTemplate> pageClientCacheTemplate,
                                               AlertMessagesConstructor messagesConstructor) {
        PageServlet pageServlet = new PageServlet();
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        pageServlet.setPipeline(pipeline);
        pageServlet.setRouter(router);
        pageServlet.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        pageServlet.setMessageBuilder(messageBuilder);
        pageServlet.setMessagesConstructor(messagesConstructor);
        pageServlet.setSubModelsProcessor(subModelsProcessor);
        pageServlet.setPropertyResolver(env.getSystemProperties());
        pageClientCacheTemplate.ifPresent(pageServlet::setClientCacheTemplate);
        return new ServletRegistrationBean(pageServlet, n2oApiUrl + "/page/*");
    }

    @Bean
    public ServletRegistrationBean dataServlet(DataController controller,
                                               AlertMessageBuilder messageBuilder) {
        DataServlet dataServlet = new DataServlet(controller);
        dataServlet.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        dataServlet.setMessageBuilder(messageBuilder);
        return new ServletRegistrationBean(dataServlet, n2oApiUrl + "/data/*");
    }

    @Bean
    public ServletRegistrationBean exportServlet(ExportController controller,
                                                 AlertMessageBuilder messageBuilder) {
        ExportServlet exportServlet = new ExportServlet(controller);
        exportServlet.setMessageBuilder(messageBuilder);
        return new ServletRegistrationBean(exportServlet, n2oApiUrl + "/export/*");
    }

    @Bean
    public ServletRegistrationBean appConfigServlet(ConfigurableEnvironment configurableEnvironment,
                                                    ContextProcessor contextProcessor,
                                                    ExposedResourceBundleMessageSource clientMessageSource,
                                                    MetadataEnvironment env) {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        writer.setContextProcessor(contextProcessor);
        writer.setPropertyResolver(configurableEnvironment);
        ObjectMapper objectMapper = ObjectMapperConstructor.metaObjectMapper();
        writer.setObjectMapper(objectMapper);
        writer.setPath("classpath*:META-INF/config.json");
        writer.setOverridePath("classpath*:META-INF/config-build.json");

        AppConfigServlet appConfigServlet = new AppConfigServlet();
        appConfigServlet.setAppConfigJsonWriter(writer);
        appConfigServlet.setObjectMapper(objectMapper);
        appConfigServlet.setMessageSource(clientMessageSource);
        appConfigServlet.setEnvironment(env);
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        appConfigServlet.setPipeline(pipeline);
        appConfigServlet.setApplicationSourceId(applicationId);
        return new ServletRegistrationBean(appConfigServlet, n2oApiUrl + "/config", "/n2o/config.json");
    }
}
