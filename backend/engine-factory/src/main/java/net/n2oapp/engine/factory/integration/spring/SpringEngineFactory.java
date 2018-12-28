package net.n2oapp.engine.factory.integration.spring;

import net.n2oapp.engine.factory.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика движков на основе Spring бинов
 */
public abstract class SpringEngineFactory<T, G> extends CachedEngineFactory<T, G>
        implements ApplicationContextAware {
    private ApplicationContext context;

    protected SpringEngineFactory(ApplicationContext context) {
        this.context = context;
    }

    protected SpringEngineFactory() {
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<G> findEngines() {
        Map<String, G> beans = context.getBeansOfType(getEngineClass());
        return OverrideBean.removeOverriddenBeans(beans).values();
    }

    public abstract Class<G> getEngineClass();
}
