package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.ConfigStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.EventListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Properties;

/**
 * Конфигурация N2O
 */
@Configuration
@Import({N2oPropertiesConfiguration.class,
        N2oMessagesConfiguration.class,
        N2oContextConfiguration.class,
        N2oCommonConfiguration.class})
@ImportResource({"classpath*:META-INF/n2o-auth-ext-context.xml",
        "classpath*:META-INF/n2o-ext-context.xml"})
@ServletComponentScan("net.n2oapp.framework")
@ComponentScan(basePackages = "net.n2oapp.framework.api", lazyInit = true)
public class N2oAutoConfiguration {

    @Autowired
    private ConfigStarter starter;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        starter.start();
    }

}
