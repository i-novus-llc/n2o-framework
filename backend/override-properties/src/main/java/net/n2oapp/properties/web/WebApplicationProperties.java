package net.n2oapp.properties.web;

import jakarta.servlet.ServletContext;
import net.n2oapp.properties.ExpressionBasedProperties;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.ReloadableProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.ServletContextAware;

import static net.n2oapp.properties.reader.PropertiesReader.getPropertiesFromClasspath;

/**
 * Composite properties, with the scheme override: default -> build -> environment -> servlet
 */
public class WebApplicationProperties extends ExpressionBasedProperties implements ServletContextAware {

    private String buildPropertiesName;

    public WebApplicationProperties(String defaultPropertiesName, String buildPropertiesName) {
        this.buildPropertiesName = buildPropertiesName;
        OverrideProperties buildProperties = getPropertiesFromClasspath(buildPropertiesName, defaultPropertiesName);
        setBaseProperties(buildProperties);
    }

    public void setFilesystemFolder(String folderName) {
        ServletPathProperties servletPathProperties = (ServletPathProperties) getServletProperties();
        if (servletPathProperties != null) {
            servletPathProperties.setFilesystemFolder(folderName);
            servletPathProperties.setResource(new FileSystemResource(folderName + "/" + buildPropertiesName));
        }
    }

    public void setEnvironment(ConfigurableEnvironment webAppEnvironment) {
        setEvaluationContext(new StandardEvaluationContext(webAppEnvironment));
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        ServletPathProperties servletPathProperties = new ServletPathProperties(servletContext, getBaseProperties());
        setBaseProperties(servletPathProperties);
    }

    public ReloadableProperties getServletProperties() {
        if (getBaseProperties() instanceof ServletPathProperties servletPathProperties) {
            return servletPathProperties;
        } else
            return null;
    }

}
