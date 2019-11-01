package net.n2oapp.framework.config.warmup;

import net.n2oapp.framework.api.event.N2oStartedEvent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/**
 * Прогрев сборки хедера и регистрация маршрутов
 */
public class HeaderWarmUpper implements EnvironmentAware {
    private Environment environment;
    private N2oApplicationBuilder applicationBuilder;

    @EventListener(N2oStartedEvent.class)
    public void warmUp() {
        String headerId = environment.getProperty("n2o.header.id", String.class, null);
        String welcomePageId = environment.getProperty("n2o.ui.homepage.id", String.class, null);
        // необходимо чтобы зарегистрировать рутовые страницы в RouteRegister
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = applicationBuilder
                .read().transform().validate().cache()
                .compile().transform().cache();
        if (headerId != null && !headerId.isEmpty()) {
            pipeline.get(new HeaderContext(headerId));
        } else if (welcomePageId != null && !welcomePageId.isEmpty()) {
            PageContext context = new PageContext(welcomePageId, "/");
            pipeline.get(context);
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
