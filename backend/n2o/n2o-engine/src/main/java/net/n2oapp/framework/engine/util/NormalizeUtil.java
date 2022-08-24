package net.n2oapp.framework.engine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;

import java.util.Base64;

/**
 * Утилитный класс для функций нормализации данных
 */
public class NormalizeUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private NormalizeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static DataSet jsonToMap(String json) {
        try {
            return new DataSet(objectMapper.readValue(json, new TypeReference<>() { } ));
        } catch (JsonProcessingException e) {
            throw new N2oException("Unable to apply function \"#jsonToMap\"", e);
        }
    }

    public static String mapToJson(Object map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new N2oException("Unable to apply function \"#mapToJson\"", e);
        }
    }

    public static String encodeToBase64(String text) {
        return Base64.getUrlEncoder().encodeToString(text.getBytes());
    }

    public static String decodeFromBase64(String base64) {
        return new String(Base64.getUrlDecoder().decode(base64));
    }
}
