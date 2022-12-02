package net.n2oapp.framework.config.util;

import java.util.HashMap;
import java.util.Map;

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

}
