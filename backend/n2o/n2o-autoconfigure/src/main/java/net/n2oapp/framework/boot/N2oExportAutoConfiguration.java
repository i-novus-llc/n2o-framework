package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.export.ExportController;
import net.n2oapp.framework.export.ExportDataServlet;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ExportController.class)
@ComponentScan(basePackages = "net.n2oapp.framework.export", lazyInit = true)
public class N2oExportAutoConfiguration {
    @Value("${n2o.api.url:/n2o}")
    private String n2oApiUrl;

    @Bean
    public ExportController exportController(MetadataEnvironment environment,
                                             MetadataRouter router,
                                             QueryController queryController) {
        return new ExportController(environment, router, queryController);
    }

    @Bean
    public ServletRegistrationBean exportDataServlet(ExportController controller,
                                                     AlertMessageBuilder messageBuilder) {
        ExportDataServlet exportDataServlet = new ExportDataServlet(controller);
        exportDataServlet.setObjectMapper(ObjectMapperConstructor.metaObjectMapper());
        exportDataServlet.setMessageBuilder(messageBuilder);
        return new ServletRegistrationBean(exportDataServlet, n2oApiUrl + "/export");
    }
}
