package net.n2oapp.properties.web;

import jakarta.servlet.ServletContext;
import net.n2oapp.properties.ReloadableProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.ServletContextAware;

import java.util.Properties;

/**
 * User: operhod
 * Date: 21.02.14
 * Time: 15:15
 * Properties, which lie at a root of classpath with name equal to contextPath of web application
 */
public class ServletPathProperties extends ReloadableProperties implements ServletContextAware {

    private String propertyName;

    public ServletPathProperties() {
    }

    public ServletPathProperties(ServletContext servletContext) {
        setServletContext(servletContext);
    }

    public ServletPathProperties(ServletContext servletContext, Properties baseProperties) {
        setServletContext(servletContext);
        setBaseProperties(baseProperties);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.propertyName = calcServletPropertiesPath(servletContext);
        setResource(new ClassPathResource(propertyName));
    }

    public void setClasspathPackage(String packageName) {
        if (propertyName == null)
            throw new IllegalStateException("Servlet context does not set");
        setResource(new ClassPathResource(packageName + "/" + propertyName));
    }

    public void setFilesystemFolder(String folderName) {
        if (propertyName == null)
            throw new IllegalStateException("Servlet context does not set");
        setResource(new FileSystemResource(folderName + "/" + propertyName));
    }

    private static String calcServletPropertiesPath(ServletContext servletContext) {
        String context = servletContext.getContextPath().replace("/", "").replace("\\", "");
        if (context.isEmpty()) {
            context = "root";
        }
        return context + ".properties";
    }
}
