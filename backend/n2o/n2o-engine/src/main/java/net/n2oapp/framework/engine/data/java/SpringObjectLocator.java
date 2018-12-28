package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.SpringProvider;
import net.n2oapp.framework.engine.data.java.ObjectLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Поиск Spring бинов
 */
@Component
class SpringObjectLocator implements ObjectLocator<SpringProvider>, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public Object locate(Class<?> targetClass, SpringProvider provider) {
        Object bean;
        if (provider.getSpringBean() != null && targetClass != null) {
            bean = context.getBean(provider.getSpringBean(), targetClass);
        } else if (provider.getSpringBean() != null) {
            bean = context.getBean(provider.getSpringBean());
        } else {
            bean = context.getBean(targetClass);
        }
        return bean;
    }

    @Override
    public boolean match(DIProvider provider) {
        return provider instanceof SpringProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
