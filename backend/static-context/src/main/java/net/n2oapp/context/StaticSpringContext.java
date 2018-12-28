package net.n2oapp.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * User: iryabov
 * Date: 22.08.13
 * Time: 11:58
 */
@Deprecated
public class StaticSpringContext implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext CONTEXT;

    private static CacheTemplateByMap CACHE = new CacheTemplateByMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    //for unit tests only!
    public void setCacheTemplate(CacheTemplateByMap cacheTemplate) {
        CACHE = cacheTemplate;
    }

    @Deprecated
    public static Object getBean(String beanName) {
        return CACHE.get(() -> CONTEXT.getBean(beanName), "get", beanName);
    }

    @Deprecated
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return CACHE.get(() -> CONTEXT.getBean(beanName, clazz), "get", beanName);
    }

    @Deprecated
    public static <T> T getBean(Class<T> clazz) {
        return CACHE.get(() -> CONTEXT.getBean(clazz), "get", clazz);
    }

    @Deprecated
    public static boolean containsBean(Class clazz) {
        return CACHE.get(() -> CONTEXT.getBeansOfType(clazz).size() > 0, "contains", clazz);
    }

    @Deprecated
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return CONTEXT.getBeansOfType(clazz);
    }

    @Override
    public void destroy() {
        CONTEXT = null;
    }

}
