package net.n2oapp.properties.test;

import net.n2oapp.properties.StaticProperties;
import org.springframework.core.env.PropertyResolver;

import java.util.Properties;

/**
 * Static properties for tests
 */
public class TestStaticProperties extends StaticProperties {

    @Override
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        TestStaticProperties.propertyResolver = propertyResolver;
    }

    public Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        super.setProperties(properties);
    }

    public Properties getProperties() {
        return this.properties;
    }
}
