package net.n2oapp.properties;

import org.springframework.core.env.*;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 *
 * Convenient typed static access to the properties.
 */
public class StaticProperties {

    private static final Function<String, String> DUMMY_MAPPER = s -> s;

    protected static PropertyResolver propertyResolver = null;

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        if (StaticProperties.propertyResolver != null) {
            throw new IllegalStateException("static-properties can be initialized only once");
        }
        StaticProperties.propertyResolver = propertyResolver;
    }

    public void setPropertySources(PropertySources propertySources) {
        setPropertyResolver(new PropertySourcesPropertyResolver(propertySources));
    }

    public void setPropertySource(PropertySource<?> propertySource) {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource);
        setPropertyResolver(new PropertySourcesPropertyResolver(propertySources));
    }

    public void setProperties(Properties properties) {
        setPropertySource(new PropertiesPropertySource("static", properties));
    }

    public static boolean containsProperty(String key) {
        if (propertyResolver == null) {
            throw new IllegalStateException("cannot get value of property [" + key + "], because static properties is not yet initialized");
        }
        return propertyResolver.containsProperty(key);
    }

    public static String get(String key) {
        if (propertyResolver == null) {
            throw new IllegalStateException("cannot get value of property [" + key + "], because static properties is not yet initialized");
        }
        return propertyResolver.getProperty(key);
    }

    public static String getProperty(String key) {
        return get(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static Integer getInteger(String key) {
        String value = get(key);
        if (value == null) return null;
        return Integer.parseInt(value);
    }

    public static double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    public static List<String> getList(String key) {
        return getList(key, ",");
    }

    public static List<String> getList(String key, String separator) {
        return getList(key, separator, DUMMY_MAPPER);
    }

    public static <T> List<T> getList(String key, String separator, Function<String, T> mapper) {
        String value = get(key);
        if (value == null)
            return emptyList();
        return Arrays.asList(value.split(Pattern.quote(separator))).stream().map(String::trim).map(mapper).collect(toList());
    }


    public static boolean isEnabled(String key) {
        if (!key.endsWith(".enabled")) throw new IllegalArgumentException("enabled-property must end with '.enabled'");
        return getBoolean(key);
    }

    public static <T extends Enum<?>> T getEnum(String key, Class<T> enumClass) {
        String value = get(key);
        if (value == null || value.isEmpty())
            return null;
        value = value.replaceAll("[\\W_]", "");
        T res = null;
        for (Enum enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().replaceAll("[\\W_]", "").equalsIgnoreCase(value)) {
                res = (T) enumValue;
            }
        }
        return res;
    }

}