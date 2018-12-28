package net.n2oapp.framework.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.exception.N2oException;

import java.util.Iterator;

/**
 * User: operhod
 * Date: 03.04.14
 * Time: 10:46
 */
public class JsonUtil {


    private static ObjectMapper mapper;

    public static void merge(ObjectNode mainNode, ObjectNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode jsonMainNode = mainNode.get(fieldName);
            JsonNode jsonUpdateNode = updateNode.get(fieldName);
            if (jsonMainNode != null && (jsonMainNode.isObject() && jsonUpdateNode.isObject())) {
                merge((ObjectNode) jsonMainNode, (ObjectNode) jsonUpdateNode);
            } else if (jsonMainNode != null && jsonMainNode.isArray() && jsonUpdateNode.isArray()) {
                mergeArrays((ArrayNode) jsonMainNode, (ArrayNode) jsonUpdateNode);
            } else {
                mainNode.put(fieldName, jsonUpdateNode);
            }
        }
    }

    public static String getObjectAsJsonString(Object o) {
        try {
            return getMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new N2oException(e);
        }
    }

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            initMapper();
        }
        return mapper;
    }

    private static synchronized void initMapper() {
        if (mapper == null)
            mapper = StaticSpringContext.getBean("n2oObjectMapper", ObjectMapper.class);
    }

    public static void mergeArrays(ArrayNode mainArray, ArrayNode updateArray) {
        mainArray.addAll(updateArray);
    }

}
