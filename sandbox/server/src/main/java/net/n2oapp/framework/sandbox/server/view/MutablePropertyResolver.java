package net.n2oapp.framework.sandbox.server.view;

import net.n2oapp.framework.api.StringUtils;
import org.springframework.core.env.PropertyResolver;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Настроки на основе мапы с возможностью изменения
 */
public class MutablePropertyResolver implements PropertyResolver {
    private Map<String, String> properties;


    public MutablePropertyResolver(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.get(key) != null;
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = properties.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = properties.get(key);
        return (T) value;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        String value = properties.get(key);
        return value != null ? (T) value : defaultValue;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        String value = getProperty(key);
        if (value == null)
            throw new NoSuchElementException(key);
        return value;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T value = getProperty(key, targetType);
        if (value == null)
            throw new NoSuchElementException(key);
        return value;
    }

    @Override
    public String resolvePlaceholders(String text) {
        return StringUtils.resolveProperties(text, properties);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        String value = resolvePlaceholders(text);
        if (value == null)
            throw new NoSuchElementException(text);
        return value;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
