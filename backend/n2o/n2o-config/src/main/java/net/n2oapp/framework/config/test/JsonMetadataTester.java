package net.n2oapp.framework.config.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.NestedUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.PipelineFunction;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Инструмент для тестирования сборки json метаданных из xml
 */
public class JsonMetadataTester {
    private PipelineFunction<ReadCompileBindTerminalPipeline> pipelineFunc;
    private N2oApplicationBuilder builder;
    private ObjectMapper mapper;
    private boolean printJsonOnFail;

    public JsonMetadataTester(N2oApplicationBuilder builder) {
        this.builder = builder;
        mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        pipelineFunc = (b) -> b.read().compile().bind();
    }


    /**
     * Проверить, что xml преобразуется в json эквивалентный исходному
     *  @param route       Маршрут до метаданных
     * @param jsonUri     Путь к json файлу
     * @param xmlCutPath  Путь для проверки эквивалентности внутри json полученного из xml
     * @param jsonCutPath Путь внутри json для проверки эквивалентности
     * @param queryParams
     */
    public void check(String route,
                      Class<? extends Compiled> compiledClass,
                      Resource jsonUri,
                      String xmlCutPath,
                      String jsonCutPath,
                      List<String> jsonExcludePath,
                      Map<String, Object> jsonChangeValuePaths,
                      Map<String, String> jsonChangeNodePaths, Map<String, String[]> queryParams) throws IOException {
        CompileContext<?,?> context = builder.route(route, compiledClass, queryParams);
        DataSet params = context.getParams(route, queryParams);
        check(jsonUri, context, params,
                xmlCutPath, jsonCutPath, jsonExcludePath,
                jsonChangeValuePaths, jsonChangeNodePaths);
    }

    public void check(Resource jsonUri, CompileContext<?, ?> context, DataSet data,
                      String xmlCutPath, String jsonCutPath, List<String> jsonExcludePath,
                      Map<String, Object> jsonChangeValuePaths, Map<String, String> jsonChangeNodePaths) throws IOException {
        DataSet source = exclude(changeValue(changeNode(cut(deserialize(jsonUri), jsonCutPath), jsonChangeNodePaths), jsonChangeValuePaths), jsonExcludePath);
        check(source, context, data, xmlCutPath, jsonExcludePath);
    }

    public void check(DataSet source, CompileContext<?, ?> context, DataSet data,
                      String xmlCutPath, List<String> jsonExcludePath) throws IOException {
        builder.scan();
        Compiled compiled = get(context, data, pipelineFunc.apply(new N2oPipelineSupport(builder.getEnvironment())));
        DataSet result = exclude(cut(serialize(compiled), xmlCutPath), jsonExcludePath);
        try {
            assertDeepEquals(source, result, "");
        } catch (AssertionError | Exception e) {
            if (printJsonOnFail)
                System.out.println(toJson(compiled));
            throw e;
        }
    }

    public void setPipelineFunc(PipelineFunction<ReadCompileBindTerminalPipeline> pipelineFunc) {
        this.pipelineFunc = pipelineFunc;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setPrintJsonOnFail(boolean printJsonOnFail) {
        this.printJsonOnFail = printJsonOnFail;
    }

    private void assertDeepEquals(Object source, Object result, String position) {
        if (source instanceof Map) {
            assert result != null : "Result mustn't be null in " + position;
            assert result instanceof Map : "Result must be [Map] in " + position;
            for (Map.Entry<String, Object> resultEntry : ((Map<String, Object>) source).entrySet()) {
                assertDeepEquals(resultEntry.getValue(), ((Map) result).get(NestedUtils.wrapKey(resultEntry.getKey())), !position.isEmpty() ? position + "." + resultEntry.getKey() : resultEntry.getKey());
            }
            Set<String> keys = new HashSet<>(((Map) result).keySet());
            keys.removeAll(((Map) source).keySet());
            keys = keys.stream().filter(k -> !isEmpty(((Map) result).get(k))).collect(Collectors.toSet());
            assert keys.isEmpty() : "Result contains [" + keys.iterator().next() + "], but source isn't in " + position + ". ";
        } else if (source instanceof List) {
            assert result != null : "Result mustn't be null in " + position;
            assert result instanceof List : "Result must be [List] in " + position;
            assert ((List) source).size() == ((List) result).size() : "Source and Result must have equals size in " + position;
            for (int i = 0; i < ((List) source).size(); i++) {
                assertDeepEquals(((List) source).get(i), ((List) result).get(i), position + "[" + i + "]");
            }

        } else if (source != null) {
            assert source.equals(result) : "Value not equals in " + position + ". Expected " + source + ", but actual " + result;
        } else
            assert result == null;
    }


    private DataSet deserialize(Resource jsonUri) throws IOException {
        return mapper.readValue(jsonUri.getInputStream(), DataSet.class);
    }

    private DataSet serialize(Compiled client) throws IOException {
        return mapper.readValue(toJson(client), DataSet.class);
    }

    private String toJson(Object object) throws IOException {
        String result = mapper.writeValueAsString(object);

        return result;
    }

    private DataSet cut(DataSet data, String jsonCutPath) {
        if (jsonCutPath != null)
            return (DataSet) data.get(jsonCutPath);
        return data;
    }

    private DataSet exclude(DataSet data, List<String> jsonExcludePath) {
        if (jsonExcludePath != null && !jsonExcludePath.isEmpty()) {
            for (String path : jsonExcludePath) {
                data.remove(path);
            }
        }
        return data;
    }

    private DataSet changeValue(DataSet data, Map<String, Object> changePath) {
        if (changePath != null && !changePath.isEmpty()) {
            for (Map.Entry<String, Object> entry : changePath.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
        }
        return data;
    }

    private DataSet changeNode(DataSet data, Map<String, String> changePath) {
        if (changePath != null && !changePath.isEmpty()) {
            for (Map.Entry<String, String> entry : changePath.entrySet()) {
                Object value = data.remove(entry.getKey());
                data.put(entry.getValue(), value);
            }
        }
        return data;
    }

    private Compiled get(CompileContext<?,?> compileContext, DataSet data, ReadCompileBindTerminalPipeline pipeline) {
        return pipeline.get(compileContext, data);
    }

    private boolean isEmpty(Object value) {
        return value == null ||
                value instanceof Map && ((Map) value).isEmpty() ||
                value instanceof Collection && ((Collection) value).isEmpty() ||
                value instanceof String && ((String) value).isEmpty();
    }

}
