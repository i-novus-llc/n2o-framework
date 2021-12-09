package net.n2oapp.framework.config.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилита для генерации различных свойств во время компиляции
 */
public class CompileUtil {

    public static String generateSourceDatasourceId(String widgetId) {
        return widgetId + "_ds";
    }

    public static String generateWidgetId(String pageId, String localWidgetId) {
        if ("_".equals(pageId))
            return localWidgetId;
        return pageId + "_" + localWidgetId;
    }


    /**
     * Преобразовывает плоскую мапу в объемную по знаку "-" в ключе
     * a-b : 123 -> a : {b : 123}
     *
     * @param attributes  плоская мапа атрибутов
     * @param transformer фукнция преобразования значения
     * @return объемная мапа атрибутов
     */
    public static Map<String, Object> resolveNestedAttributes(Map<String, Object> attributes, Function<Object, Object> transformer) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String[] keyChain = entry.getKey().split("-");
            Map<String, Object> nested = result;
            for (int i = 0; i < keyChain.length - 1; i++) {
                if (!nested.containsKey(keyChain[i])) {
                    nested.put(keyChain[i], new HashMap<>());
                }
                if (!HashMap.class.equals(nested.get(keyChain[i]).getClass())) {
                    throw new IllegalArgumentException("The result already contains an element with key " + keyChain[i]);
                }
                nested = (Map<String, Object>) nested.get(keyChain[i]);
            }
            if (nested.containsKey(keyChain[keyChain.length - 1])) {
                throw new IllegalArgumentException("The result already contains an element with key " + keyChain[keyChain.length - 1]);
            }
            nested.put(keyChain[keyChain.length - 1], transformer.apply(entry.getValue()));
        }
        return result;
    }
}