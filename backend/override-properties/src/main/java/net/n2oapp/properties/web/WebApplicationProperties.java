package net.n2oapp.properties.web;

import net.n2oapp.properties.ExpressionBasedProperties;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.ReloadableProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

import static net.n2oapp.properties.reader.PropertiesReader.getPropertiesFromClasspath;
import static net.n2oapp.properties.reader.PropertiesReader.getReloadableFromClasspath;

/**
 * Composite properties, with the scheme override: default -> build -> environment -> servlet
 */
public class WebApplicationProperties extends ExpressionBasedProperties implements ServletContextAware {

    private String environmentPropertiesName;

    public WebApplicationProperties(String defaultPropertiesName, String buildPropertiesName, String environmentPropertiesName) {
        this.environmentPropertiesName = environmentPropertiesName;
        OverrideProperties buildProperties = getPropertiesFromClasspath(buildPropertiesName, defaultPropertiesName);
        ReloadableProperties environmentProperties = getReloadableFromClasspath(environmentPropertiesName, 60);
        environmentProperties.setBaseProperties(buildProperties);
        setBaseProperties(environmentProperties);
    }

    public void setClasspathPackage(String packageName) {
        getEnvProperties().setResource(new ClassPathResource(packageName + "/" + environmentPropertiesName));
        ServletPathProperties servletPathProperties = (ServletPathProperties) getServletProperties();
        if (servletPathProperties != null)
            servletPathProperties.setClasspathPackage(packageName);
    }

    public void setFilesystemFolder(String folderName) {
        getEnvProperties().setResource(new FileSystemResource(folderName + "/" + environmentPropertiesName));
        ServletPathProperties servletPathProperties = (ServletPathProperties) getServletProperties();
        if (servletPathProperties != null)
            servletPathProperties.setFilesystemFolder(folderName);
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
        if (getBaseProperties() instanceof ServletPathProperties) {
            return (ServletPathProperties) getBaseProperties();
        } else
            return null;
    }

    public ReloadableProperties getEnvProperties() {
        if (getServletProperties() != null) {
            return (ReloadableProperties) getServletProperties().getBaseProperties();
        } else {
            return (ReloadableProperties)getBaseProperties();
        }
    }

    public OverrideProperties getBuildProperties() {
        return (OverrideProperties) getEnvProperties().getBaseProperties();
    }

    public OverrideProperties getDefaultProperties() {
        return (OverrideProperties) getBuildProperties().getBaseProperties();
    }


    public boolean envPropertiesIsExists() {
        return getEnvProperties().isExists();
    }

    public boolean servletPropertiesIsExists() {
        return getServletProperties() != null && getServletProperties().isExists();
    }


    /**
     * Получение значения свойства для все уровней
     */
    public PropertyValue getFullPropertyValue(String property) {
        return new PropertyValue(
                getDefaultProperties().getCurrentLvlProperty(property),
                getBuildProperties().getCurrentLvlProperty(property),
                getEnvProperties().getCurrentLvlProperty(property),
                servletPropertiesIsExists() ? getServletProperties().getCurrentLvlProperty(property) : null,
                getProperty(property)
        );
    }

    public static class PropertyValue {
        private String defaultValue;
        private String buildValue;
        private String envValue;
        private String servletValue;
        private String value;

        private PropertyValue(String defaultValue, String buildValue, String envValue, String servletValue, String value) {
            this.defaultValue = defaultValue;
            this.buildValue = buildValue;
            this.envValue = envValue;
            this.servletValue = servletValue;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getBuildValue() {
            return buildValue;
        }

        public String getEnvValue() {
            return envValue;
        }

        public String getServletValue() {
            return servletValue;
        }
    }


}
