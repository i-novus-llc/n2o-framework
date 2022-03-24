package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.springframework.core.env.PropertyResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Класс предназначен для работы с property в sandbox.
 */
public class SandboxPropertyResolver implements PropertyResolver {
    /**
     * Оригинальный defaultPropertyResolver
     */
    private PropertyResolver defaultPropertyResolver;
    /**
     * Дополнительные настройки runtime
     */
    private PropertyResolver runtimePropertyResolver;
    /**
     * Файл application.properties в sandbox проекте
     */
    private PropertyResolver projectPropertyResolver;


    public void configure(PropertyResolver defaultPropertyResolver,
                          Map<String, String> runtimeProperties,
                          String projectFile) {
        initResolvers(defaultPropertyResolver, runtimeProperties);
        Properties properties = new Properties();

        if (projectFile != null) {
            try (InputStream inputStream = new ByteArrayInputStream(projectFile.getBytes())) {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
        this.projectPropertyResolver = new SimplePropertyResolver(properties);
    }

    private void initResolvers(PropertyResolver defaultPropertyResolver,
                               Map<String, String> runtimeProperties) {
        this.defaultPropertyResolver = defaultPropertyResolver;
        if (runtimeProperties != null)
            this.runtimePropertyResolver = new MutablePropertyResolver(runtimeProperties);
    }

    @Override
    public boolean containsProperty(String key) {
        return projectPropertyResolver != null && projectPropertyResolver.containsProperty(key) ||
                runtimePropertyResolver != null && runtimePropertyResolver.containsProperty(key) ||
                defaultPropertyResolver != null && defaultPropertyResolver.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        String value = resolveProperty(key, null, projectPropertyResolver, String.class);
        value = resolveProperty(key, value, runtimePropertyResolver, String.class);
        return resolveProperty(key, value, defaultPropertyResolver, String.class);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = resolveProperty(key, null, projectPropertyResolver, String.class);
        value = resolveProperty(key, value, runtimePropertyResolver, String.class);
        value = resolveProperty(key, value, defaultPropertyResolver, String.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        T value = resolveProperty(key, null, projectPropertyResolver, targetType);
        value = resolveProperty(key, value, runtimePropertyResolver, targetType);
        return resolveProperty(key, value, defaultPropertyResolver, targetType);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        T value = resolveProperty(key, null, projectPropertyResolver, targetType);
        value = resolveProperty(key, value, runtimePropertyResolver, targetType);
        value = resolveProperty(key, value, defaultPropertyResolver, targetType);
        return value != null ? value : defaultValue;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        String value = getProperty(key);
        return value == null ? defaultPropertyResolver.getRequiredProperty(key) : value;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T value = getProperty(key, targetType);
        return value == null ? defaultPropertyResolver.getRequiredProperty(key, targetType) : value;
    }

    @Override
    public String resolvePlaceholders(String text) {
        String value = text;
        if (projectPropertyResolver != null) {
            value = projectPropertyResolver.resolvePlaceholders(text);
        }
        if (value.equals(text) && runtimePropertyResolver != null) {
            value = runtimePropertyResolver.resolvePlaceholders(text);
        }
        if (value.equals(text) && defaultPropertyResolver != null) {
            value = defaultPropertyResolver.resolvePlaceholders(text);
        }
        return value;
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        String value = resolvePlaceholders(text);
        return (value.equals(text) ? defaultPropertyResolver.resolveRequiredPlaceholders(text) : value);
    }

    /**
     * Разрешение настройки, если предлыдущее значение null
     *
     * @param key       Ключ property
     * @param prevValue Предыдущее значение
     * @return Значение property или null,
     * если property с таким ключом не был найден
     */
    private <T> T resolveProperty(String key, T prevValue, PropertyResolver resolver, Class<T> targetType) {
        if (prevValue != null)
            return prevValue;
        if (resolver != null && resolver.containsProperty(key)) {
            String value = resolver.getProperty(key);
            if (value != null && targetType != null && !String.class.equals(targetType)) {
                if (List.class.equals(targetType)) {
                    List<String> list = new ArrayList<>();
                    for (String item : value.split(",")) {
                        list.add(item.trim());
                    }
                    return (T) list;
                } else if (Set.class.equals(targetType)) {
                    Set<String> set = new LinkedHashSet<>();
                    for (String item : value.split(",")) {
                        set.add(item.trim());
                    }
                    return (T) set;
                } else if (Boolean.class.equals(targetType)) {
                    return (T) Boolean.valueOf(value);
                } else if (Integer.class.equals(targetType)) {
                    return (T) Integer.valueOf(value);
                } else if (Long.class.equals(targetType)) {
                    return (T) Long.valueOf(value);
                } else if (targetType.isEnum()) {
                    return (T) Enum.valueOf((Class) targetType, value);
                } else
                    throw new UnsupportedOperationException("targetType " + targetType + " is not supported");
            } else
                return (T) value;
        } else
            return null;
    }
}
