package net.n2oapp.framework.config.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

/**
 * Утилита преобразования CSS-стилей в стили, пригодные для использования в React.
 */
public class StylesResolver {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    private StylesResolver() {

    }

    /**
     * Преобразует строку CSS-стилей в {@code Map} с ключами в camelCase.
     * <p>
     * Разбор корректно обрабатывает допустимые в CSS конструкции, в которых символы
     * {@code ':'} или {@code ';'} встречаются не на верхнем уровне вложенности:
     * {@code url(http://...)} (без кавычек), {@code url('http://...')},
     * {@code url(data:image/svg+xml;base64,...)}, {@code content: 'a; b'} и т.п.
     * Разделителями объявлений и пар «ключ-значение» считаются только {@code ';'} и {@code ':'},
     * находящиеся вне кавычек и вне круглых скобок.
     *
     * @param input строка CSS-стилей (может быть {@code null})
     * @return карта стилей в camelCase или {@code null}, если вход пуст
     * @throws IllegalArgumentException если формат строки некорректен
     */
    public static Map<String, String> resolveStyles(String input) {
        if (input == null || input.isBlank()) return null;

        Map<String, String> result = new HashMap<>();

        for (String declaration : splitTopLevel(input.strip(), ';')) {
            String trimmed = declaration.strip();
            if (trimmed.isEmpty()) continue;

            List<String> keyValue = splitTopLevel(trimmed, ':');
            if (keyValue.size() != 2) {
                throw new IllegalArgumentException(
                        "Некорректный формат строки стилей: '%s'".formatted(declaration));
            }

            String key = kebabToCamelCase(WHITESPACE_PATTERN.matcher(keyValue.get(0)).replaceAll(""));
            String value = WHITESPACE_PATTERN.matcher(keyValue.get(1).strip()).replaceAll(" ");

            result.put(key, value);
        }

        return result.isEmpty() ? null : result;
    }

    /**
     * Преобразует строку CSS-стилей в строку React-стилей.
     */
    public static String resolveStylesToString(String input) {
        if (input == null) return null;
        Map<String, String> styleMap = resolveStyles(input);
        if (styleMap == null) return null;
        return styleMap.keySet().stream().map(key -> key + ":" + styleMap.get(key))
                .collect(Collectors.joining(";"));
    }

    /**
     * Разбивает строку по разделителю {@code separator} на «верхнем уровне» —
     * вне одинарных и двойных кавычек и вне круглых скобок.
     */
    private static List<String> splitTopLevel(String source, char separator) {
        List<String> parts = new ArrayList<>();
        boolean inSingle = false, inDouble = false;
        int depth = 0, start = 0;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (inSingle) {
                if (c == '\'') inSingle = false;
            } else if (inDouble) {
                if (c == '"') inDouble = false;
            } else if (c == '\'') {
                inSingle = true;
            } else if (c == '"') {
                inDouble = true;
            } else if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth = Math.max(0, depth - 1);
            } else if (c == separator && depth == 0) {
                parts.add(source.substring(start, i));
                start = i + 1;
            }
        }
        parts.add(source.substring(start));
        return parts;
    }

    /**
     * Преобразовать строку из kebab-case в camelCase.
     */
    private static String kebabToCamelCase(String s) {
        StringBuilder builder = new StringBuilder(s.length());
        boolean capitalizeNext = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                builder.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                builder.append(c);
            }
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