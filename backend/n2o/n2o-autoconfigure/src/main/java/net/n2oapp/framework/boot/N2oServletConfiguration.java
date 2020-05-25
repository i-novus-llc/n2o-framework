package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import net.n2oapp.framework.ui.servlet.AppConfigServlet;
import net.n2oapp.framework.ui.servlet.ExposedResourceBundleMessageSource;
import net.n2oapp.framework.ui.servlet.data.DataServlet;
import net.n2oapp.framework.ui.servlet.page.PageServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Optional;

@Configuration
public class N2oServletConfiguration {
    @Value("${n2o.header.id:}")
    private String headerId;

    @Value("${n2o.api.url:/n2o}")
    private String n2oApiUrl;

    @Bean
    public ServletRegistrationBean pageServlet(MetadataEnvironment env, MetadataRouter router,
                                               ErrorMessageBuilder errorMessageBuilder,
                                               SubModelsProcessor subModelsProcessor,
                                               Optional<ClientCacheTemplate> pageClientCacheTemplate) {
        PageServlet pageServlet = new PageServlet();
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        pageServlet.setPipeline(pipeline);
        pageServlet.setRouter(router);
        pageServlet.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        pageServlet.setErrorMessageBuilder(errorMessageBuilder);
        pageServlet.setSubModelsProcessor(subModelsProcessor);
        pageClientCacheTemplate.ifPresent(pageServlet::setClientCacheTemplate);
        return new ServletRegistrationBean(pageServlet, n2oApiUrl + "/page/*");
    }

    @Bean
    public ServletRegistrationBean dataServlet(DataController controller,
                                               ErrorMessageBuilder errorMessageBuilder) {
        DataServlet dataServlet = new DataServlet(controller);
        dataServlet.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        dataServlet.setErrorMessageBuilder(errorMessageBuilder);
        return new ServletRegistrationBean(dataServlet, n2oApiUrl + "/data/*");
    }

    @Bean
    public ServletRegistrationBean appConfigServlet(ConfigurableEnvironment configurableEnvironment,
                                                    ContextProcessor contextProcessor,
                                                    ExposedResourceBundleMessageSource clientMessageSource,
                                                    MetadataEnvironment env) {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        writer.setContextProcessor(contextProcessor);
        writer.setPropertyResolver(configurableEnvironment);
        writer.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        writer.setPath("classpath*:META-INF/config.json");
        writer.setOverridePath("classpath*:META-INF/config-build.json");

        AppConfigServlet appConfigServlet = new AppConfigServlet();
        appConfigServlet.setAppConfigJsonWriter(writer);
        appConfigServlet.setMessageSource(clientMessageSource);
        appConfigServlet.setEnvironment(env);
        ReadCompileBindTerminalPipeline pipeline = N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy()
                .compile().transform().cache().copy()
                .bind();
        appConfigServlet.setPipeline(pipeline);
        appConfigServlet.setHeaderSourceId(headerId);
        return new ServletRegistrationBean(appConfigServlet, n2oApiUrl + "/config", "/n2o/config.json");
    }
}
