package net.n2oapp.framework.config.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * Builder для проверки json метаданных
 */
public class JsonChecker {
    private Resource jsonResource;
    private CompileContext<?,?> context;
    private JsonMetadataTester tester;
    private String jsonCutPath;
    private String xmlCutPath;
    private List<String> jsonExcludePaths = new ArrayList<>();
    private Map<String, Object> jsonChangeValuePath = new HashMap<>();
    private Map<String, String> jsonChangeNodePath = new HashMap<>();

    public JsonChecker(JsonMetadataTester tester) {
        this.tester = tester;
    }

    public JsonChecker(Resource jsonResource, JsonMetadataTester tester) {
        this(tester);
        this.jsonResource = jsonResource;
    }

    public JsonChecker(CompileContext<?, ?> context, Resource jsonResource, JsonMetadataTester tester) {
        this(jsonResource, tester);
        this.context = context;
    }

    @Deprecated
    public JsonChecker jsonExcludePaths(List<String> jsonExcludePaths) {
        this.jsonExcludePaths = jsonExcludePaths;
        return this;
    }

    /**
     * Вырезать поддерево узлов исходного json
     * @param jsonCutPath Путь поддерева узлов
     */
    public JsonChecker cutJson(String jsonCutPath) {
        this.jsonCutPath = jsonCutPath;
        return this;
    }

    /**
     * Вырезать поддерево узлов json полученного из xml
     * @param xmlCutPath Путь поддерева узлов
     */
    public JsonChecker cutXml(String xmlCutPath) {
        this.xmlCutPath = xmlCutPath;
        return this;
    }

    /**
     * Исключить путь из сравнения
     * @param jsonExcludePaths Пути
     */
    public JsonChecker exclude(String... jsonExcludePaths) {
        this.jsonExcludePaths.addAll(Arrays.asList(jsonExcludePaths));
        return this;
    }

    /**
     * Изменить значение узла json
     * @param jsonChangePath Путь к значению
     * @param value Новое значение
     */
    public JsonChecker changeValue(String jsonChangePath, String value) {
        this.jsonChangeValuePath.put(jsonChangePath, value);
        return this;
    }

    /**
     * Переместить значение узела json в другой путь
     * @param jsonNodePath Путь к узлу
     * @param jsonChangeNodePath Путь, куда переместить значение
     */
    public JsonChecker changeNode(String jsonNodePath, String jsonChangeNodePath) {
        this.jsonChangeNodePath.put(jsonNodePath, jsonChangeNodePath);
        return this;
    }

    /**
     * Утверждать, что исходный json ресурс, переданный в конструкторе, эквивалентен полученному из xml по адресу и классу
     * @param route Адрес получения метаданной
     * @param compiledClass Класс собранной метаданной
     */
    public void assertEquals(String route, Class<? extends Compiled> compiledClass) {
        try {
            tester.check(route, compiledClass,
                    jsonResource,
                    xmlCutPath, jsonCutPath,
                    jsonExcludePaths,
                    jsonChangeValuePath, jsonChangeNodePath, new HashMap<>());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Утверждать, что исходный json ресурс, переданный в конструкторе, эквивалентен полученному из xml по адресу и классу
     * @param route Адрес получения метаданной
     * @param compiledClass Класс собранной метаданной
     */
    public void assertEquals(String route, Class<? extends Compiled> compiledClass, Map<String, String[]> queryParams) {
        try {
            tester.check(route, compiledClass,
                    jsonResource,
                    xmlCutPath, jsonCutPath,
                    jsonExcludePaths,
                    jsonChangeValuePath, jsonChangeNodePath, queryParams);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Утверждать, что исходный json ресурс, переданный в конструкторе, эквивалентен полученному из xml по контексту сборки
     * @param context Контекст сборки xml
     */
    public void assertEquals(CompileContext<?,?> context) {
        try {
            tester.check(jsonResource, context, new DataSet(),
                    xmlCutPath, jsonCutPath, jsonExcludePaths,
                    jsonChangeValuePath, jsonChangeNodePath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Утверждать, что исходный json ресурс, переданный в конструкторе, эквивалентен полученному из xml по контексту сборки
     * @param context Контекст сборки xml
     */
    public void assertEquals(CompileContext<?,?> context, DataSet data) {
        try {
            tester.check(jsonResource, context, data,
                    xmlCutPath, jsonCutPath, jsonExcludePaths,
                    jsonChangeValuePath, jsonChangeNodePath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Утверждать, что исходный json ресурс, переданный в конструкторе, эквивалентен полученному из xml
     */
    public void assertEquals() {
        assertEquals(context);
    }

    /**
     * Утверждать, что исходный json эквивалентен полученному из xml по контексту сборки
     * @param jsonSource Исходный json
     * @param context Контекст сборки xml
     */
    public void assertEquals(DataSet jsonSource, CompileContext<?,?> context) {
        try {
            tester.check(jsonSource, context, new DataSet(),
                    xmlCutPath, jsonExcludePaths);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Утверждать, что исходный json эквивалентен полученному из xml по контексту сборки
     * @param jsonSource Исходный json
     * @param context Контекст сборки xml
     */
    public void assertEquals(DataSet jsonSource, CompileContext<?,?> context, DataSet data) {
        try {
            tester.check(jsonSource, context, data,
                    xmlCutPath, jsonExcludePaths);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
