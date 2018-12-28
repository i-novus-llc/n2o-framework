package net.n2oapp.engine.factory.simple;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.engine.factory.integration.spring.SpringEngineFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * User: iryabov
 * Date: 09.09.13
 * Time: 10:51
 */
public abstract class BeanListAware<G> implements ApplicationContextAware {
    private volatile List<G> beans;
    private ApplicationContext context;


    private synchronized void initBeans() {
        if (beans == null) {
            beans = new ArrayList<>(OverrideBean.removeOverriddenBeans(context.getBeansOfType(getBeanClass())).values());
        }
    }

    public List<G> getBeans() {
        if (beans == null)
            initBeans();
        return beans;
    }


    public abstract Class<G> getBeanClass();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
