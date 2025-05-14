package net.n2oapp.framework.config.properties;

import org.springframework.core.env.PropertyResolver;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Настройки с несколькими уровнями переопределения
 */
public class MultivaluedPropertyResolver implements PropertyResolver {

    private List<PropertyResolver> stack;

    public MultivaluedPropertyResolver(PropertyResolver... properties) {
        if (properties == null || properties.length == 0)
            throw new IllegalArgumentException("Must be property resolver");
        this.stack = Arrays.asList(properties);
    }

    @Override
    public boolean containsProperty(String key) {

        return false;
    }

    @Override
    public String getProperty(String key) {
        return resolveStack(r -> r.getProperty(key));
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return resolveStack(r -> r.getProperty(key), r -> r.getProperty(key, defaultValue));
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return resolveStack(r -> r.getProperty(key, targetType));
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return resolveStack(r -> r.getProperty(key, targetType), r -> r.getProperty(key, targetType, defaultValue));
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return resolveStack(r -> r.getProperty(key), r -> r.getRequiredProperty(key));
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return resolveStack(r -> r.getProperty(key, targetType), r -> r.getRequiredProperty(key, targetType));
    }

    @Override
    public String resolvePlaceholders(String text) {
        return resolvePlaceholdersStack(text,
                PropertyResolver::resolvePlaceholders,
                PropertyResolver::resolvePlaceholders,
                t -> !t.contains("${"));
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return resolvePlaceholdersStack(text,
                PropertyResolver::resolvePlaceholders,
                PropertyResolver::resolveRequiredPlaceholders,
                t -> !t.contains("${"));
    }

    private <T> T resolveStack(Function<PropertyResolver, T> resolver) {
        return resolveStack(resolver, null);
    }

    private <T> T resolveStack(Function<PropertyResolver, T> anyResolver,
                               Function<PropertyResolver, T> lastResolver) {
        T value = null;
        for (int i = 0; i < stack.size(); i++) {
            if (value != null)
                break;
            if (lastResolver != null && i == stack.size() - 1)
                value = lastResolver.apply(stack.get(i));
            else
                value = anyResolver.apply(stack.get(i));
        }
        return value;
    }

    private String resolvePlaceholdersStack(String text, BiFunction<PropertyResolver, String, String> anyProcessor,
                               BiFunction<PropertyResolver, String, String> lastProcessor,
                               Predicate<String> condition) {
        if (text == null)
            return null;
        String result = text;
        for (int i = 0; i < stack.size(); i++) {
            if (condition.test(result))
                break;
            if (lastProcessor != null && i == stack.size() - 1)
                result = lastProcessor.apply(stack.get(i), result);
            else
                result = anyProcessor.apply(stack.get(i), result);
        }
        return result;
    }


}
