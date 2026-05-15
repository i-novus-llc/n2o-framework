package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.SpringProvider;
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
        String beanName = provider.getSpringBean();
        if (beanName != null && targetClass != null)
            return context.getBean(beanName, targetClass);
        if (beanName != null)
            return context.getBean(beanName);
        if (targetClass != null)
            return context.getBean(targetClass);
        return null;
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
