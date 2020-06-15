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
import java.util.List;
import java.util.Map;

/**
 * Слияние различных конфигурационных json файлов в один json
 */
public class AppConfigJsonWriter {
    private N2oConfigBuilder<AppConfig> configBuilder;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    /**
     * Путь до основных файлов конфигураций
     */
    private String path = "classpath*:META-INF/config.json";
    /**
     * Путь до переопределяющих файлов конфигураций
     */
    private String overridePath = "classpath*:META-INF/config-build.json";
    /**
     * Маппер json
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    public AppConfigJsonWriter() {
        configBuilder = new N2oConfigBuilder<>(new AppConfig());
    }

    public AppConfigJsonWriter(String path) {
        this();
        this.path = path;
    }

    /**
     * Загрузить конфигурации из разных файлов и слить в один
     */
    public void loadValues() {
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
    }

    /**
     * Распечатать json с дополнительными значениями
     *
     * @param out         Принтер
     * @param addedValues Дополнительные значения
     * @throws IOException Ошибка печати
     */
    public void writeValues(PrintWriter out, Map<String, Object> addedValues) throws IOException {
        configBuilder.addAll(addedValues);
        configBuilder.write(out);
    }

    /**
     * Записать в виде файла на диск
     *
     * @param directory   Директория файла
     * @param addedValues Дополнительные значения
     * @throws IOException Ошибка записи
     */
    public void writeValues(String directory, Map<String, Object> addedValues) throws IOException {
        configBuilder.addAll(addedValues);
        String filePath = PathUtil.concatFileNameAndBasePath("config.json", directory);
        configBuilder.write(new File(filePath));
    }

    /**
     * Получить json с дополнительными значениями
     *
     * @param addedValues Дополнительные значения
     * @return Json объект
     */
    public Map<String, Object> getValues(Map<String, Object> addedValues) {
        configBuilder.addAll(addedValues);
        return objectMapper.convertValue(configBuilder.get(), Map.class);
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        configBuilder.setPropertyResolver(propertyResolver);
    }

    public void setContextProcessor(ContextProcessor contextProcessor) {
        configBuilder.setContextProcessor(contextProcessor);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        configBuilder.setObjectMapper(objectMapper);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setConfigs(List<String> configs) {
        configs.forEach(configBuilder::read);
    }

    public void setOverridePath(String overridePath) {
        this.overridePath = overridePath;
    }
}