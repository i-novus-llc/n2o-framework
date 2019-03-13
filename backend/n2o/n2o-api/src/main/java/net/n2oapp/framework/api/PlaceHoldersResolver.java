package net.n2oapp.framework.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.exception.N2oException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Шаблонезатор текста.
 * Заменяет плейсхолдеры в строке
 */
public class PlaceHoldersResolver {

    private static final String OPTIONAL_SUFFIX = "?";
    private static final String REQUIRED_SUFFIX = "!";

    private String prefix;
    private String suffix;

    /**
     * Создать замену плейсхолдеров
     *
     * @param prefix Начало плейсхолдера
     * @param suffix Окончание плейсолдера
     */
    public PlaceHoldersResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Заменить плейсхолдеры в тексте
     *
     * @param text Текст
     * @param data Данные для замены
     * @return Текст с заменёнными плейсхолдерами, если замена нашлась
     */
    @SuppressWarnings("unchecked")
    public String resolve(String text, Object data) {
        if (data == null)
            return text;
        return safeResolve(text, notReplaceNull(function(data)));
    }

    /**
     * Заменить плейсхолдеры в тексте
     *
     * @param text Текст
     * @param func Функция замены
     * @return Текст с заменёнными плейсхолдерами, если замена нашлась
     */
    public String resolve(String text, Function<String, Object> func) {
        return safeResolve(text, notReplaceNull(func));
    }

    /**
     * Заменить плейсхолдер на значение
     *
     * @param placeholder Плейсхолдер
     * @param func Функция замены
     * @return Значение или то, что пришло, если это не плейсхолдер
     */
    public Object resolveValue(Object placeholder, Function<String, Object> func) {
        return safeResolveValue(placeholder, func);
    }

    /**
     * Заменить плейсхолдер на значение
     *
     * @param placeholder Плейсхолдер
     * @param data        Карта значений
     * @return Значение или то, что пришло, если это не плейсхолдер
     */
    public Object resolveValue(Object placeholder, Map<String, Object> data) {
        return safeResolveValue(placeholder, data::get);
    }


    /**
     * Получает набор плейсхолдеров из текста
     *
     * @param text текст
     * @return плейсхолдеры
     */
    public Set<String> extractPlaceHolders(String text) {
        Set<String> result = new LinkedHashSet<>();
        if (text == null)
            return result;
        String[] split = text.split(Pattern.quote(prefix));
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                int idxSuffix = split[i].indexOf(suffix);
                if (idxSuffix > 0) {
                    result.add(split[i].substring(0, idxSuffix));
                }
            }
        }
        return result;
    }


    /**
     * Примеры:
     * PlaceHoldersResolver hasPlaceHolders = new PlaceHoldersResolver("#{", "}"};
     * hasPlaceHolders("#{text}");       >true
     * hasPlaceHolders("name:#{text}");  >true
     * hasPlaceHolders("name:{text}");   >false
     *
     * @param text текст
     * @return true - содержит плейсхолдер, false - не содержит
     */
    public boolean hasPlaceHolders(String text) {
        return text != null && text.contains(prefix) && text.contains(suffix) && text.indexOf(prefix) < text.indexOf(suffix);
    }

    /**
     * Примеры:
     * PlaceHoldersResolver hasPlaceHolders = new PlaceHoldersResolver("#{", "}"};
     * isPlaceHolders("#{text}");       >true
     * isPlaceHolders("name:#{text}");  >false
     *
     * @param param Параметр
     * @return true - является плейсхолдером, false - не является плейсхолдером
     */
    public boolean isPlaceHolder(Object param) {
        if (param == null)
            return false;
        if (!(param instanceof String))
            return false;
        String text = (String) param;
        return text.startsWith(prefix) && text.endsWith(suffix);
    }

    @SuppressWarnings("unchecked")
    private static Function<String, Object> function(Object data) {
        if (data instanceof Function) {
            return (Function<String, Object>) data;
        } else if (data instanceof PropertyResolver) {
            return ((PropertyResolver) data)::getProperty;
        } else if (data instanceof Map) {
            return ((Map) data)::get;
        } else if (data instanceof List) {
            return k -> ((List) data).get(Integer.parseInt(k));
        } else if (data != null && data.getClass().isArray()) {
            Object[] array = (Object[]) data;
            return k -> array[Integer.parseInt(k)];
        } else if (data instanceof String || data instanceof Number || data instanceof Date) {
            return k -> data;
        } else {
            try {
                Map<String, String> map = BeanUtils.describe(data);
                return map::get;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private String safeResolve(String text, Function<String, Object> callback) {
        if (text == null) return null;
        StringBuilder sb = new StringBuilder();
        String[] split = text.split(Pattern.quote(prefix));
        if (split.length <= 1)
            return text;
        sb.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            int idxSuffix;
            int idxNext;
            if (suffix != null && !suffix.isEmpty()) {
                idxSuffix = split[i].indexOf(suffix);
                idxNext = idxSuffix + 1;
            } else {
                String[] ends = split[i].split("\\W");
                idxSuffix = ends[0].length();
                idxNext = idxSuffix;
            }
            if (idxSuffix > 0) {
                String placeholder = split[i].substring(0, idxSuffix);
                Object value = callback.apply(placeholder);
                sb.append(value);
                sb.append(split[i].substring(idxNext));
            }
        }
        return sb.toString();
    }

    private Object safeResolveValue(Object placeholder, Function<String, Object> func) {
        if (!isPlaceHolder(placeholder))
            return placeholder;
        String text = (String) placeholder;
        return func.apply(text.substring(prefix.length(), text.length() - suffix.length()));
    }

    private Function<String, Object> notReplaceNull(Object data) {
        return key -> {
            Object result = function(data).apply(key);
            return result != null ? result.toString() : prefix.concat(key).concat(suffix);
        };
    }

    /**
     * Возвращает json-валидное значение в строке
     */
    public static Function<String, Object> replaceByJson(Function<String, Object> callback, ObjectMapper mapper) {
        return key -> {
            Object result = callback.apply(key);
            try {
                if (result instanceof String)
                    return ((String) result).replace("\"", "\\\"");
                return mapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                throw new N2oException(e);
            }
        };
    }

    public static Function<String, Object> replaceNullByEmpty(Function<String, Object> callback) {
        return key -> {
            Object result = callback.apply(key);
            return result != null ? result.toString() : "";
        };
    }

    public static Function<String, Object> replaceNullByEmpty(Object data) {
        return replaceNullByEmpty(function(data));
    }

    public static Function<String, Object> replaceRequired(Function<String, Object> callback) {
        return key -> {
            Object value = callback.apply(key);
            if (value == null)
                throw new NotFoundPlaceholderException(key);
            return value;
        };
    }

    public static Function<String, Object> replaceRequired(Object data) {
        return replaceRequired(function(data));
    }

    public static Function<String, Object> replaceOptional(Function<String, Object> data) {
        return key -> {
            String placeholder = extractPlaceholder(key);
            Object value = data.apply(placeholder);
            if (value == null) {
                if (extractRequired(key))
                    throw new NotFoundPlaceholderException(placeholder);
                return extractOptional(key);
            }
            return value;
        };
    }

    public static Function<String, Object> replaceOptional(Object data) {
        return replaceOptional(function(data));
    }

    /**
     * Примеры:
     * extractPlaceholder("org.id")     >org.id
     * extractPlaceholder("org.id?")    >org.id
     * extractPlaceholder("org.id?1")   >org.id
     *
     * @param expression выражение, содержащее контекстный параметр
     * @return контекстный параметр
     */
    private static String extractPlaceholder(String expression) {
        int idxOptional = expression.indexOf(OPTIONAL_SUFFIX);
        if (idxOptional < 0) {
            idxOptional = expression.indexOf(REQUIRED_SUFFIX);
            if (idxOptional < 0)
                return expression;
        }
        return expression.substring(0, idxOptional);
    }

    private static boolean extractRequired(String expression) {
        return !expression.contains(OPTIONAL_SUFFIX) && expression.endsWith(REQUIRED_SUFFIX);
    }

    private static Object extractOptional(String expression) {
        int optionalIdx = expression.indexOf(OPTIONAL_SUFFIX);
        if (optionalIdx == -1)
            return null;
        String defVal = expression.substring(optionalIdx + 1);
        return defVal.isEmpty() ? null : defVal;
    }
}
