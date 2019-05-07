package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

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
    private static final Logger log = LoggerFactory.getLogger(AppConfigJsonWriter.class);
    /**
     * Путь до основных файлов конфигураций
     */
    private String path = "classpath*:META-INF/config.json";
    /**
     * Путь до переопределяющих файлов конфигураций
     */
    private String overridePath = "classpath*:META-INF/config-build.json";
    /**
     * Замена значений настроек
     */
    private PropertyResolver propertyResolver;
    /**
     * Замена контекстных значений
     */
    private ContextProcessor contextProcessor;
    /**
     * Маппер json
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Кэш конфигураций
     */
    private List<String> configs = new ArrayList<>();

    public AppConfigJsonWriter() {
    }

    public AppConfigJsonWriter(String path) {
        this.path = path;
    }

    /**
     * Загрузить конфигурации из разных файлов и слить в один
     */
    public void loadValues() {
        configs.addAll(readConfigs());
    }

    /**
     * Распечатать json с дополнительными значениями
     * @param out Принтер
     * @param addedValues Дополнительные значения
     * @throws IOException Ошибка печати
     */
    public void writeValues(PrintWriter out, Map<String, ?> addedValues) throws IOException {
        objectMapper.writeValue(out, getNode(addedValues));
    }

    /**
     * Получить json с дополнительными значениями
     * @param addedValues Дополнительные значения
     * @return Json объект
     */
    public Map<String, Object> getValues(Map<String, ?> addedValues) {
        return objectMapper.convertValue(getNode(addedValues), Map.class);
    }

    private ObjectNode getNode(Map<String, ?> addedValues) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode configsNode = retrieveConfig(configs);
        for (String key : addedValues.keySet()) {
            objectNode.set(key, objectMapper.valueToTree(addedValues.get(key)));
        }
        if (configsNode != null)
            JsonUtil.merge(objectNode, configsNode);
        return objectNode;
    }

    private ObjectNode retrieveConfig(List<String> configs) {
        ObjectNode res = null;
        if (configs == null)
            return null;
        for (String config : configs) {
            try {
                if (res == null) res = read(config);
                else JsonUtil.merge(res, read(config));
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
        return res;
    }

    private ObjectNode read(String json) throws IOException {
        String text = json;
        if (propertyResolver != null)
            text = StringUtils.resolveProperties(text, propertyResolver);
        if (contextProcessor != null)
            text = contextProcessor.resolveJson(text, objectMapper);
        return (ObjectNode) objectMapper.readTree(text);
    }

    private List<String> readConfigs() {
        List<String> result = new ArrayList<>();
        try {
            if (path != null)
                load(result, path);
            if (overridePath != null)
                load(result, overridePath);
        } catch (IOException e) {
            throw new N2oException(e);
        }
        return result;
    }

    private void load(List<String> configs, String path) throws IOException {
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        for (Resource resource : r.getResources(path)) {
            try (InputStream is = resource.getInputStream()) {
                if (is != null) {
                    String text = IOUtils.toString(is, "UTF-8");
                    configs.add(text);
                } else {
                    log.debug("{} not found.", path);
                }
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
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