package net.n2oapp.framework.boot.graphql;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Утилитный класс для работы с GraphQl
 */
public class GraphQlUtil {

    /**
     * Преобразование структуры объекта в строку, корректную в GraphQl запросе
     *
     * @param obj Входящий объект
     * @return GraphQl строка
     */
    public static String toGraphQlString(Object obj) {
        if (obj == null)
            return null;

        if (obj instanceof Map) {
            StringJoiner joiner = new StringJoiner(", ", "{", "}");
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
                joiner.add(entry.getKey() + ": " + toGraphQlString(entry.getValue()));
            }
            return joiner.toString();
        } else if (obj instanceof List) {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (Object item : ((List) obj))
                joiner.add(toGraphQlString(item));
            return joiner.toString();
        } else if (obj instanceof String)
            return "\"" + obj + "\"";
        else
            return obj.toString();
    }
}
