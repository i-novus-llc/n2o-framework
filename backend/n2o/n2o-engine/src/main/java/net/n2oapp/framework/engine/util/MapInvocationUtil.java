package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитный класс, служащий для преобразования данных вызова в Map
 */
public class MapInvocationUtil {
    private static final ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
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
            Expression expression;
            Object data = dataSet.get(map.getKey());
            if (map.getValue() != null) {
                expression = writeParser.parseExpression(
                        map.getValue().getMapping() != null ? map.getValue().getMapping() : "['" + map.getKey() + "']");
                if (map.getValue().getChildMapping() != null) {
                    if (data instanceof Collection) {
                        List list = new ArrayList();
                        for (Object obj : (DataList) data)
                            list.add(mapToMap((DataSet) obj, map.getValue().getChildMapping()));
                        expression.setValue(result, list);
                    } else if (data instanceof DataSet)
                        expression.setValue(result, mapToMap((DataSet) data, map.getValue().getChildMapping()));
                } else
                    expression.setValue(result, data);
            } else {
                expression = writeParser.parseExpression("['" + map.getKey() + "']");
                expression.setValue(result, data);
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
