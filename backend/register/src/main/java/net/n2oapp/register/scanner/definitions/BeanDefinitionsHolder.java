package net.n2oapp.register.scanner.definitions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: boris.fanyuk
 * Date: 10.08.12
 * Time: 19:11
 */
public abstract class BeanDefinitionsHolder implements ClassDefinitionHolder {

    private Set<Class> classes = new LinkedHashSet<Class>();

    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public Set<Class> getClasses() {
        return classes;
    }

    public void init() {
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        for (String s : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(s);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName == null)
                continue;
            Class aClass = getClass(beanClassName);
            if (aClass == null)
                continue;
            classes.add(aClass);
        }

    }

    protected abstract Class getClass(String beanClassName);

}
