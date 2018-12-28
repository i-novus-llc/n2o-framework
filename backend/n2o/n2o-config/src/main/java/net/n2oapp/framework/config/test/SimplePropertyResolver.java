package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.StringUtils;
import org.springframework.core.env.PropertyResolver;

import java.util.Properties;

/**
 * Простой шаблонизатор настроек
 */
public class SimplePropertyResolver implements PropertyResolver {
    private Properties properties;

    public SimplePropertyResolver(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.contains(key);
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        Object value = properties.get(key);
        return (T) value;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        Object value = properties.get(key);
        return value != null ? (T) value : defaultValue;
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return targetType;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        String value = properties.getProperty(key);
        if (value == null)
            throw new IllegalStateException("Property " + key + " not found");
        return value;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        Object value = properties.get(key);
        if (value == null)
            throw new IllegalStateException("Property " + key + " not found");
        return (T) value;
    }

    @Override
    public String resolvePlaceholders(String text) {
        return StringUtils.resolveProperties(text, this::getProperty);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return StringUtils.resolveProperties(text, this::getRequiredProperty);
    }

    public Object setProperty(String key, Object value) {
        return properties.put(key, value);
    }
}
