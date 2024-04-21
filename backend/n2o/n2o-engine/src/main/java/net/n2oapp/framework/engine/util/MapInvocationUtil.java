package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.util.MappingProcessor.isMappingEnabled;

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

        for (Map.Entry<String, FieldMapping> entry : mapping.entrySet()) {
            Object data = dataSet.get(entry.getKey());
            if (entry.getValue() != null) {
                if (!isMappingEnabled(entry.getValue().getEnabled(), dataSet))
                    continue;
                String fieldMapping = entry.getValue().getMapping() != null ?
                        entry.getValue().getMapping() :
                        Placeholders.spel(entry.getKey());

                if (entry.getValue().getChildMapping() != null && data != null) {
                    if (data instanceof Collection) {
                        DataList list = new DataList();
                        for (Object obj : (Collection<?>) data)
                            list.add(mapToMap((DataSet) obj, entry.getValue().getChildMapping()));
                        MappingProcessor.inMap(result, entry.getKey(), fieldMapping, list);
                    } else if (data instanceof DataSet)
                        MappingProcessor.inMap(result, entry.getKey(), fieldMapping, mapToMap((DataSet) data, entry.getValue().getChildMapping()));
                } else
                    MappingProcessor.inMap(result, entry.getKey(), fieldMapping, data);
            } else {
                MappingProcessor.inMap(result, entry.getKey(), Placeholders.spel(entry.getKey()), data);
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
