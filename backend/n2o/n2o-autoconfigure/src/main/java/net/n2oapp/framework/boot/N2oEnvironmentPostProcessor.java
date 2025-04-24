package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Пост процессор для передачи окружения N2O всем бинам реализующим интерфейс
 * {@link net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware}
 */
public class N2oEnvironmentPostProcessor implements BeanPostProcessor {

    private MetadataEnvironment environment;

    public N2oEnvironmentPostProcessor(MetadataEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MetadataEnvironmentAware environmentAware)
            environmentAware.setEnvironment(environment);
        return bean;
    }
}
