package net.n2oapp.framework.config.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Утилита преобразования стилей
 */
public class StylesResolver {

    private StylesResolver() {

    }

    /**
     * Преобразовать html стили в react стили
     */
    public static Map<String, String> resolveStyles(String input) {
        if (input == null || input.trim().length() == 0) return null;

        Map<String, String> result = new HashMap<>();

        String[] lines = input.trim().split(";");
        for (String line : lines) {
            String[] lineArr = line.split(":");
            if (lineArr.length != 2)
                throw new IllegalArgumentException("Invalid string of styles");
            String key = kebabToCamelCase(lineArr[0].replaceAll("[\\t ]", ""));
            String value = lineArr[1];
            while (value.startsWith(" "))
                value = value.replaceFirst("[\\t ]", "");
            result.put(key, value);
        }

        return result;
    }

    /**
     * Преобразовать html стили в react стили
     */
    public static String resolveStylesToString(String input) {
        if (input == null) return null;
        Map<String, String> styleMap = resolveStyles(input);
        if (styleMap == null) return null;
        return styleMap.keySet().stream().map(key -> key + ":" + styleMap.get(key))
                .collect(Collectors.joining(";"));
    }

    /**
     * Преобразовать строку из kebab-case в camelCase
     */
    private static String kebabToCamelCase(String s) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-' && i + 1 < s.length()) {
                builder.append(Character.toUpperCase(s.charAt(++i)));
            } else
                builder.append(s.charAt(i));
        }

        return builder.toString();
    }

    /**
     * Преобразовать строку из camelCase в UPPER_SNAKE_CASE
     */
    public static String camelToSnake(Object camelCase) {
        if (camelCase == null) return null;
        return join(splitByCharacterTypeCamelCase(camelCase.toString()), "_").toUpperCase();
    }
}
