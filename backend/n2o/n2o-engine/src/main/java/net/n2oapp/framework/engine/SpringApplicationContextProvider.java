package net.n2oapp.framework.engine;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static String getEnvironmentProperty(String key) {
        if (applicationContext != null)
            return applicationContext.getEnvironment().getProperty(key);
        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}