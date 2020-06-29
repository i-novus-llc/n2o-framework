package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Слияние различных конфигурационных json файлов в один json
 */
public class AppConfigJsonWriter {

    /**
     * Чтение/запись json
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Замена значений настроек
     */
    private PropertyResolver propertyResolver;
    /**
     * Замена контекстных значений
     */
    private ContextProcessor contextProcessor;

    /**
     * Путь до основных файлов конфигураций
     */
    private String path = "classpath*:META-INF/config.json";
    /**
     * Путь до переопределяющих файлов конфигураций
     */
    private String overridePath = "classpath*:META-INF/config-build.json";

    private List<String> configs = Collections.emptyList();

    public AppConfigJsonWriter() {
    }

    public AppConfigJsonWriter(String path) {
        this();
        this.path = path;
    }

    /**
     * Загрузить конфигурации из разных файлов и слить в один
     */
    @Deprecated
    public void loadValues() {
        build();
    }

    private N2oConfigBuilder<AppConfig> build() {
        N2oConfigBuilder<AppConfig> configBuilder = new N2oConfigBuilder<>(AppConfig.class,
                objectMapper, propertyResolver, contextProcessor);
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        try {
            for (Resource resource : r.getResources(path)) {
                configBuilder.read(resource);
            }
            for (Resource resource : r.getResources(overridePath)) {
                configBuilder.read(resource);
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        configs.forEach(configBuilder::read);
        return configBuilder;
    }

    /**
     * Распечатать json с дополнительными значениями
     *
     * @param out         Принтер
     * @param addedValues Дополнительные значения
     */
    public void writeValues(PrintWriter out, Map<String, Object> addedValues) {
        build().addAll(addedValues).write(out);
    }

    /**
     * Получить json с дополнительными значениями
     *
     * @param addedValues Дополнительные значения
     * @return Json объект
     */
    public Map<String, Object> getValues(Map<String, Object> addedValues) {
        return objectMapper.convertValue(build().addAll(addedValues).get(), Map.class);
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public void setContextProcessor(ContextProcessor contextProcessor) {
        this.contextProcessor = contextProcessor;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setConfigs(List<String> configs) {
        this.configs = configs;
    }

    public void setOverridePath(String overridePath) {
        this.overridePath = overridePath;
    }
}