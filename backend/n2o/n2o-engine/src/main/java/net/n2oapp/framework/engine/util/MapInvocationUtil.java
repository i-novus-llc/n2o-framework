package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитный класс, служащий для преобразования данных вызова в Map
 */
public class MapInvocationUtil {
    private static final Predicate<String> MAPPING_PATTERN = Pattern.compile("\\[.+]").asPredicate();
    private static final String KEY_ERROR = "%s -> %s";

    /**
     * Преобразует входные значения согласно маппингу и собирает их в map
     *
     * @param dataSet Входные данные
     * @param mapping Маппинг полей
     * @return Преобразованные входные значения согласно маппингу
     */
    public static Map<String, Object> mapToMap(DataSet dataSet, Map<String, FieldMapping> mapping) {
        validateMapping(mapping);
        Map<String, Object> result = new DataSet();

        for (Map.Entry<String, FieldMapping> map : mapping.entrySet()) {
            if (map.getValue().getEnabled() != null && !ScriptProcessor.evalForBoolean(map.getValue().getEnabled(), dataSet))
                continue;
            Object data = dataSet.get(map.getKey());
            if (map.getValue() != null) {
                String fieldMapping = map.getValue().getMapping() != null ? map.getValue().getMapping() : Placeholders.spel(map.getKey());
                if (map.getValue().getChildMapping() != null) {
                    if (data instanceof Collection) {
                        DataList list = new DataList();
                        for (Object obj : (Collection<?>) data)
                            list.add(mapToMap((DataSet) obj, map.getValue().getChildMapping()));
                        MappingProcessor.inMap(result, fieldMapping, list);
                    } else if (data instanceof DataSet)
                        MappingProcessor.inMap(result, fieldMapping, mapToMap((DataSet) data, map.getValue().getChildMapping()));
                } else
                    MappingProcessor.inMap(result, fieldMapping, data);
            } else {
                MappingProcessor.inMap(result, Placeholders.spel(map.getKey()), data);
            }
        }
        return result;
    }

    /**
     * Проверка корректности формата маппингов
     *
     * @param mapping Map маппингов
     */
    private static void validateMapping(Map<String, FieldMapping> mapping) {
        String errorMapping = mapping.entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue().getMapping() != null)
                .filter(e -> !MAPPING_PATTERN.test(e.getValue().getMapping()))
                .map(e -> String.format(KEY_ERROR, e.getKey(), e.getValue().getMapping()))
                .collect(Collectors.joining(", "));

        if (!errorMapping.isEmpty())
            throw new IllegalArgumentException("Not valid mapping: " + errorMapping);
    }
}
