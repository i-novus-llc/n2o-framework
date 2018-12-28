package net.n2oapp.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * User: operhod
 * Date: 05.03.14
 * Time: 9:32
 */
@Deprecated
public class StaticServletContext implements ApplicationContextAware {
    private static WebApplicationContext CONTEXT;
    private static Logger logger = LoggerFactory.getLogger(StaticServletContext.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            CONTEXT = (WebApplicationContext) applicationContext;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("It isn't web-application!");
        }
    }

    public static ServletContext getServletContext() {
        return CONTEXT.getServletContext();
    }
}
