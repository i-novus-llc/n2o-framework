package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
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
import net.n2oapp.framework.ui.servlet.data.PagingCountServlet;
import net.n2oapp.framework.ui.servlet.data.ValidationDataServlet;
import net.n2oapp.framework.ui.servlet.page.PageServlet;
import net.n2oapp.framework.ui.servlet.table.ExportServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.Optional;

@AutoConfiguration
public class N2oServletConfiguration {
    @Value("${n2o.application.id:}")
    private String applicationId;

    @Value("${n2o.api.url:/n2o}")
    private String n2oApiUrl;

    private final ObjectMapper metaObjectMapper = ObjectMapperConstructor.metaObjectMapper();

    @Bean
    @ConditionalOnProperty(name = "n2o.ui.cache.page.enabled", havingValue = "true")
    public ClientCacheTemplate pageClientCacheTemplate(CacheManager cacheManager, Environment env) {
        boolean enabled = env.getProperty("n2o.ui.cache.page.enabled", Boolean.class, false);
        if (!enabled)
            return null;

        long lifetime = env.getProperty("n2o.ui.cache.page.lifetime", Long.class, 1000 * 60 * 10L);

        String mode = env.getProperty("n2o.ui.cache.page.mode", String.class, "lifetime");
        return switch (mode) {
            case "lifetime" -> new LifetimeClientCacheTemplate(lifetime);
            case "modified" -> new ModifiedClientCacheTemplate(cacheManager);
            default -> throw new UnsupportedOperationException("Unknown page client cache mode " + mode);
        };
    }

    @Bean
    public ServletRegistrationBean<PageServlet> pageServlet(MetadataEnvironment env,
                                                            MetadataRouter router,
                                                            AlertMessagesConstructor messagesConstructor,
                                                            SubModelsProcessor subModelsProcessor,
                                                            Optional<ClientCacheTemplate> pageClientCacheTemplate) {
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();

        PageServlet pageServlet = new PageServlet(
                metaObjectMapper,
                pageClientCacheTemplate.orElse(null),
                messagesConstructor,
                router,
                pipeline,
                subModelsProcessor
        );
        return new ServletRegistrationBean<>(pageServlet, n2oApiUrl + "/page/*");
    }

    @Bean
    public ServletRegistrationBean<DataServlet> dataServlet(DataController controller,
                                                            AlertMessagesConstructor messagesConstructor) {
        DataServlet dataServlet = new DataServlet(controller, metaObjectMapper, messagesConstructor);
        return new ServletRegistrationBean<>(dataServlet, n2oApiUrl + "/data/*");
    }

    @Bean
    public ServletRegistrationBean<PagingCountServlet> pagingCountServlet(DataController controller,
                                                                          AlertMessagesConstructor messagesConstructor) {
        PagingCountServlet servlet = new PagingCountServlet(controller, metaObjectMapper, messagesConstructor);
        return new ServletRegistrationBean<>(servlet, n2oApiUrl + "/count/*");
    }

    @Bean
    public ServletRegistrationBean<ValidationDataServlet> validationServlet(DataController controller,
                                                                            AlertMessagesConstructor messagesConstructor) {
        ValidationDataServlet servlet = new ValidationDataServlet(controller, metaObjectMapper, messagesConstructor);
        return new ServletRegistrationBean<>(servlet, n2oApiUrl + "/validation/*");
    }

    @Bean
    public ServletRegistrationBean<ExportServlet> exportServlet(ExportController controller,
                                                                AlertMessagesConstructor messagesConstructor) {
        ExportServlet servlet = new ExportServlet(controller, metaObjectMapper, messagesConstructor);
        return new ServletRegistrationBean<>(servlet, n2oApiUrl + "/export/*");
    }

    @Bean
    public ServletRegistrationBean<AppConfigServlet> appConfigServlet(ConfigurableEnvironment configurableEnvironment,
                                                                      ContextProcessor contextProcessor,
                                                                      ExposedResourceBundleMessageSource clientMessageSource,
                                                                      MetadataEnvironment env) {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        writer.setContextProcessor(contextProcessor);
        writer.setPropertyResolver(configurableEnvironment);
        writer.setObjectMapper(metaObjectMapper);
        writer.setPath("classpath*:META-INF/config.json");
        writer.setOverridePath("classpath*:META-INF/config-build.json");

        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();

        AppConfigServlet appConfigServlet = new AppConfigServlet(
                writer,
                clientMessageSource,
                pipeline,
                env,
                applicationId,
                metaObjectMapper
        );
        return new ServletRegistrationBean<>(appConfigServlet, n2oApiUrl + "/config", "/n2o/config.json");
    }
}