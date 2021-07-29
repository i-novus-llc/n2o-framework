package net.n2oapp.criteria.dataset;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

/**
 * Утилитный класс для работы с данными объекта
 */
public class DataSetUtil {
    private static final ExpressionParser readParser = new SpelExpressionParser(new SpelParserConfiguration(false, false));


    /**
     * Построение набора данных по объекту с учетом маппинга полей
     *
     * @param source        Исходный объект
     * @param fieldsMapping Маппинг полей
     * @return Набор данных объекта
     */
    public static DataSet extract(Object source, Map<String, String> fieldsMapping) {
        DataSet dataSet = new DataSet();
        for (Map.Entry<String, String> map : fieldsMapping.entrySet()) {
            String mapping = map.getValue() != null ? map.getValue()
                    : "['" + map.getKey() + "']";
            Expression expression = readParser.parseExpression(mapping);
            Object value = expression.getValue(source);
            dataSet.put(map.getKey(), value);
        }
        return dataSet;
    }
}
