package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.N2oWebAppEnvironment;
import net.n2oapp.properties.web.WebApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

public class N2oPropertiesPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        WebApplicationProperties n2oProperties = new WebApplicationProperties("META-INF/n2o.properties", "META-INF/n2o-build.properties", "placeholders.properties");
        N2oWebAppEnvironment n2oWebAppEnvironment = new N2oWebAppEnvironment();
        n2oProperties.setEnvironment(n2oWebAppEnvironment);
        n2oWebAppEnvironment.setN2oProperties(n2oProperties);
        n2oProperties.setFilesystemFolder(System.getProperty("user.home") + "/.n2o");
        PropertiesPropertySource propertySource = new PropertiesPropertySource("n2oProperties", n2oProperties);
        environment.getPropertySources().addLast(propertySource);
    }
}
