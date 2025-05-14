package net.n2oapp.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import net.n2oapp.framework.api.N2oWebAppEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.test.TestContextEngine;
import org.springframework.core.env.PropertyResolver;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Стандартная реализация конструктора конфигурации клиента N2O приложения
 * @param <T> Тип конфигурации
 */
public class N2oConfigBuilder<T extends AppConfig> implements ConfigBuilder<T> {
    private T appConfig;
    private Class<T> appConfigType;
    /**
     * Чтение/запись json
     */
    private ObjectMapper objectMapper;
    /**
     * Замена значений настроек
     */
    private PropertyResolver propertyResolver;
    /**
     * Замена контекстных значений
     */
    private ContextProcessor contextProcessor;

    @SuppressWarnings("unchecked")
    public N2oConfigBuilder(T appConfig,
                            ObjectMapper objectMapper,
                            PropertyResolver propertyResolver,
                            ContextProcessor contextProcessor) {
        this.appConfig = appConfig;
        this.appConfigType = (Class<T>) appConfig.getClass();
        this.objectMapper = objectMapper;
        this.propertyResolver = propertyResolver;
        this.contextProcessor = contextProcessor;
    }

    public N2oConfigBuilder(T appConfig,
                            ObjectMapper objectMapper) {
        this(appConfig, objectMapper,
                new N2oWebAppEnvironment(),
                new ContextProcessor(new TestContextEngine()));
    }

    public N2oConfigBuilder(T appConfig) {
        this(appConfig, new ObjectMapper());
    }

    public N2oConfigBuilder(Class<T> appConfigType,
                            ObjectMapper objectMapper,
                            PropertyResolver propertyResolver,
                            ContextProcessor contextProcessor) {
        this.appConfigType = appConfigType;
        this.objectMapper = objectMapper;
        this.propertyResolver = propertyResolver;
        this.contextProcessor = contextProcessor;
    }

    public N2oConfigBuilder(Class<T> appConfigType,
                            ObjectMapper objectMapper) {
        this(appConfigType, objectMapper,
                new N2oWebAppEnvironment(),
                new ContextProcessor(new TestContextEngine()));
    }

    public N2oConfigBuilder(Class<T> appConfigType) {
        this(appConfigType, new ObjectMapper());
    }

    @Override
    public ConfigBuilder<T> add(String property, Object value) {
        appConfig.setProperty(property, value);
        return this;
    }

    @Override
    public ConfigBuilder<T> addAll(Map<String, Object> values) {
        values.forEach(this::add);
        return this;
    }

    @Override
    public ConfigBuilder<T> read(String content) {
        String json = resolvePlaceholders(content);
        try {
            if (appConfig != null) {
                ObjectReader merger = objectMapper.readerForUpdating(appConfig);
                appConfig = merger.readValue(json);
            } else {
                appConfig = objectMapper.readValue(json, appConfigType);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    @Override
    public void write(Writer out) {
        try {
            objectMapper.writeValue(out, appConfig);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void write(File file) {
        try {
            objectMapper.writeValue(file, appConfig);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public T get() {
        return appConfig;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public void setContextProcessor(ContextProcessor contextProcessor) {
        this.contextProcessor = contextProcessor;
    }

    private String resolvePlaceholders(String text) {
        if (propertyResolver != null)
            text = StringUtils.resolveProperties(text, propertyResolver);
        if (contextProcessor != null)
            text = contextProcessor.resolveJson(text, objectMapper);
        return text;
    }
}
