package net.n2oapp.framework.config.warmup;

import net.n2oapp.framework.api.event.N2oStartedEvent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/**
 * Прогрев сборки хедера и регистрация маршрутов
 */
public class HeaderWarmUpper implements EnvironmentAware {
    private static Logger log = LoggerFactory.getLogger(HeaderWarmUpper.class);

    private Environment environment;
    private N2oApplicationBuilder applicationBuilder;

    @EventListener(N2oStartedEvent.class)
    public void warmUp() {
        String headerId = environment.getProperty("n2o.header.id", String.class);
        String welcomePageId = environment.getProperty("n2o.homepage.id", String.class);
        // необходимо чтобы зарегистрировать рутовые страницы в RouteRegister
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = applicationBuilder
                .read().transform().validate().cache()
                .compile().transform().cache();
        if (headerId != null && !headerId.isEmpty()) {
            pipeline.get(new ApplicationContext(headerId));
        } else if (welcomePageId != null && !welcomePageId.isEmpty()) {
            PageContext context = new PageContext(welcomePageId, "/");
            try {
           	    pipeline.get(context);
            } catch (ReferentialIntegrityViolationException ignore) {
                log.error("Main page by id " + welcomePageId + " not found ");
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setApplicationBuilder(N2oApplicationBuilder applicationBuilder) {
        this.applicationBuilder = applicationBuilder;
    }
}
