package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.StringUtils;
import org.springframework.core.env.PropertyResolver;

import java.util.*;

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
        return properties.get(key) != null;
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
        if (value instanceof String && targetType != null && !String.class.equals(targetType)) {
            String strValue = (String) value;
            if (List.class.equals(targetType)) {
                List<String> list = new ArrayList<>();
                for (String item : strValue.split(",")) {
                    list.add(item.trim());
                }
                return (T) list;
            } else if (Set.class.equals(targetType)) {
                Set<String> set = new LinkedHashSet<>();
                for (String item : strValue.split(",")) {
                    set.add(item.trim());
                }
                return (T) set;
            } else if (Boolean.class.equals(targetType)) {
                return (T) Boolean.valueOf(strValue);
            } else if (Integer.class.equals(targetType)) {
                return (T) Integer.valueOf(strValue);
            } else if (Long.class.equals(targetType)) {
                return (T) Long.valueOf(strValue);
            } else
                throw new UnsupportedOperationException("targetType " + targetType + " is not supported");
        } else
            return (T) value;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        Object value = properties.get(key);
        return value != null ? (T) value : defaultValue;
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
