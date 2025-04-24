package net.n2oapp.framework.boot.graphql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Утилитный класс для работы с GraphQl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

        if (obj instanceof Map map) {
            StringJoiner joiner = new StringJoiner(", ", "{", "}");
            map.forEach((key, value) -> joiner.add(key + ": " + toGraphQlString(value)));
            return joiner.toString();
        } else if (obj instanceof List list) {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (Object item : list)
                joiner.add(toGraphQlString(item));
            return joiner.toString();
        } else if (obj instanceof Temporal || obj instanceof Date || obj instanceof BigDecimal) {
            return "\"" + obj + "\"";
        } else if (obj instanceof String str) {
            return "\"" + escapeJson(str) + "\"";
        } else
            return obj.toString();
    }


    public static String escapeJson(String raw) {
        String escaped = raw;
        escaped = escaped.replace("\\", "\\\\");
        escaped = escaped.replace("\"", "\\\"");
        escaped = escaped.replace("\b", "\\b");
        escaped = escaped.replace("\f", "\\f");
        escaped = escaped.replace("\n", "\\n");
        escaped = escaped.replace("\r", "\\r");
        escaped = escaped.replace("\t", "\\t");
        return escaped;
    }
}
