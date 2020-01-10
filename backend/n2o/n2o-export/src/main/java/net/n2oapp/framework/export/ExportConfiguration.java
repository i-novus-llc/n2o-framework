package net.n2oapp.framework.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.n2oapp.framework.export", lazyInit = true)
public class ExportConfiguration {

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
                                                     @Qualifier("n2oObjectMapper") ObjectMapper n2oObjectMapper,
                                                     ErrorMessageBuilder errorMessageBuilder) {
        ExportDataServlet exportDataServlet = new ExportDataServlet(controller);
        exportDataServlet.setObjectMapper(n2oObjectMapper);
        exportDataServlet.setErrorMessageBuilder(errorMessageBuilder);
        return new ServletRegistrationBean(exportDataServlet, n2oApiUrl + "/export");
    }
}
