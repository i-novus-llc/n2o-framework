package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.N2oWebAppEnvironment;
import net.n2oapp.properties.web.WebApplicationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;

import java.util.Properties;

/**
 * Конфигурация настроек
 */
@Configuration
@ImportResource("classpath*:META-INF/n2o-properties-ext-context.xml")
public class N2oPropertiesConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "n2oProperties")
    public Properties n2oProperties() {
        WebApplicationProperties n2oProperties = new WebApplicationProperties("META-INF/n2o.properties", "META-INF/n2o-build.properties", "placeholders.properties");
        N2oWebAppEnvironment n2oWebAppEnvironment = new N2oWebAppEnvironment();
        n2oProperties.setEnvironment(n2oWebAppEnvironment);
        n2oWebAppEnvironment.setN2oProperties(n2oProperties);
        n2oProperties.setFilesystemFolder(System.getProperty("user.home") + "/.n2o");
        return n2oProperties;
    }

    @Bean("n2oPropertySource")
    public PropertySource n2oPropertySource(@Qualifier("n2oProperties") Properties n2oProperties, StandardEnvironment environment) {
        PropertiesPropertySource propertySource = new PropertiesPropertySource("n2oProperties", n2oProperties);
        environment.getPropertySources().addLast(propertySource);
        return propertySource;
    }

    @Bean
    @DependsOn("n2oPropertySource")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//        placeholderConfigurer.setEnvironment(environment);
//        MutablePropertySources propertySources = new MutablePropertySources();
//        propertySources.addFirst(n2oPropertySource);
//        placeholderConfigurer.setPropertySources(propertySources);
        placeholderConfigurer.setIgnoreResourceNotFound(true);
        return placeholderConfigurer;
    }

}
