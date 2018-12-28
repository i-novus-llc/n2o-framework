package net.n2oapp.framework.boot;

import net.n2oapp.context.StaticServletContext;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.properties.StaticProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * Общая конфигурация бинов N2O
 */
@Configuration
@EnableScheduling
public class N2oCommonConfiguration {

    @Value("${n2o.ui.task.executor.pool.size}")
    private int executerPoolSize;
    @Value("${n2o.ui.task.scheduler.pool.size}")
    private int schedulerPoolSize;

    @Bean
    @ConditionalOnMissingBean
    @Deprecated //Нужно использовать настройки spring напрямую. Через StaticProperties - устаревший подход.
    public StaticProperties staticProperties(Environment environment) {
        StaticProperties staticProperties = new StaticProperties() {
            @Override
            public void setPropertyResolver(PropertyResolver propertyResolver) {
                StaticProperties.propertyResolver = propertyResolver;
            }
        };
        staticProperties.setPropertyResolver(environment);
        return staticProperties;
    }

    @Bean
    public StaticSpringContext staticSpringContext(ApplicationContext context) {
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(context);
        return staticSpringContext;
    }

    @Bean
    public StaticServletContext staticServletContext(ApplicationContext applicationContext) {
        StaticServletContext staticServletContext = new StaticServletContext();
        staticServletContext.setApplicationContext(applicationContext);
        return staticServletContext;
    }

    @Bean
    public TaskExecutor n2oExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(executerPoolSize);
        return executor;
    }

    @Bean
    public TaskScheduler n2oScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerPoolSize);
        return scheduler;
    }

    @Configuration
    @AutoConfigureAfter(N2oContextConfiguration.class)
    @Import({N2oEngineConfiguration.class, N2oMetadataConfiguration.class, N2oRestConfiguration.class})
    static class InnerConfiguration {

        private StaticSpringContext staticSpringContext;
        private StaticProperties staticProperties;

        public InnerConfiguration(StaticSpringContext staticSpringContext, StaticProperties staticProperties) {
            this.staticSpringContext = staticSpringContext;
            this.staticProperties = staticProperties;
        }
    }


}
