package net.n2oapp.framework.engine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        for (Map.Entry<String, FieldMapping> entry : mapping.entrySet())
            mapEntry(entry, dataSet, result);
        return result;
    }

    private static void mapEntry(Map.Entry<String, FieldMapping> entry, DataSet dataSet, Map<String, Object> result) {
        FieldMapping fieldMapping = entry.getValue();
        Object value = dataSet.get(entry.getKey());

        if (fieldMapping == null) {
            MappingProcessor.inMap(result, entry.getKey(), Placeholders.spel(entry.getKey()), value);
            return;
        }
        if (!isMappingEnabled(fieldMapping.getEnabled(), dataSet))
            return;

        String mapping = fieldMapping.getMapping() != null
                ? fieldMapping.getMapping()
                : Placeholders.spel(entry.getKey());

        if (fieldMapping.getChildMapping() == null || value == null) {
            MappingProcessor.inMap(result, entry.getKey(), mapping, value);
            return;
        }

        switch (value) {
            case Collection<?> collection -> {
                DataList list = new DataList();
                for (Object obj : collection)
                    list.add(mapToMap((DataSet) obj, fieldMapping.getChildMapping()));
                MappingProcessor.inMap(result, entry.getKey(), mapping, list);
            }
            case DataSet childDataSet -> MappingProcessor.inMap(result, entry.getKey(), mapping,
                    mapToMap(childDataSet, fieldMapping.getChildMapping()));
            default -> {
                // value не Collection и не DataSet — как и в исходном коде, ничего не кладём
            }
        }
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
            throw new IllegalArgumentException(String.format("Некорректный маппинг: %s", errorMapping));
    }
}
