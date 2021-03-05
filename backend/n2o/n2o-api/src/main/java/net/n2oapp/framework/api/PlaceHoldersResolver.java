package net.n2oapp.framework.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import net.n2oapp.criteria.dataset.NestedUtils;
import net.n2oapp.framework.api.exception.N2oException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.env.PropertyResolver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Шаблонизатор текста.
 * Заменяет плейсхолдеры в строке
 */
public class PlaceHoldersResolver {

    private static final String OPTIONAL_SUFFIX = "?";
    private static final String REQUIRED_SUFFIX = "!";

    private String prefix;
    private String suffix;
    private Boolean onlyJavaVariable;
    private Function<String, Integer> defaultSuffixIdx = str -> {
        String[] ends = str.split("\\W");
        return ends.length > 0 ? ends[0].length() : 0;
    };

    /**
     * Создать замену плейсхолдеров
     *
     * @param prefix Начало плейсхолдера
     * @param suffix Окончание плейсхолдера. Если не задано, то до первого не буквенного символа.
     */
    public PlaceHoldersResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.onlyJavaVariable = false;
    }

    /**
     * Создать замену плейсхолдеров
     *
     * @param prefix           Начало плейсхолдера
     * @param suffix           Окончание плейсхолдера. Если не задано, то до первого не буквенного символа.
     * @param onlyJavaVariable Учитывать плейсхолдеры соответствующие только спецификации java переменных
     * @param defaultSuffixIdx Функция вычисления индекса конца плейсхолдера
     */
    public PlaceHoldersResolver(String prefix, String suffix, Boolean onlyJavaVariable, Function<String, Integer> defaultSuffixIdx) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.defaultSuffixIdx = defaultSuffixIdx;
        this.onlyJavaVariable = onlyJavaVariable;
    }

    /**
     * Создать замену плейсхолдеров
     *
     * @param prefix           Начало плейсхолдера
     * @param suffix           Окончание плейсхолдера. Если не задано, то до первого не буквенного символа.
     * @param onlyJavaVariable Учитывать плейсхолдеры соответствующие только спецификации java переменных
     */
    public PlaceHoldersResolver(String prefix, String suffix, Boolean onlyJavaVariable) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.onlyJavaVariable = onlyJavaVariable;
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
     * Заменить плейсхолдеры в json
     *
     * @param json - строка json
     * @param func Функция замены
     * @return Текст с заменёнными плейсхолдерами, если замена нашлась
     */
    public String resolveJson(String json, Function<String, Object> func, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(resolvePlaceholders(objectMapper.readTree(json), func::apply));
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    /**
     * Заменить плейсхолдер на значение
     *
     * @param placeholder Плейсхолдер
     * @param func        Функция замены
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
                if (idxSuffix == 0) {
                    sb.append(prefix).append(suffix);
                    sb.append(split[i].substring(idxNext));
                }
            } else {
                idxSuffix = defaultSuffixIdx.apply(split[i]);
                idxNext = idxSuffix;
                if (idxSuffix == 0) {
                    sb.append(prefix);
                    sb.append(split[i].substring(idxNext));
                }
            }
            if (idxSuffix > 0) {
                String placeholder = split[i].substring(0, idxSuffix);
                if (onlyJavaVariable && !NestedUtils.isJavaVariable(placeholder)) {
                    sb.append(prefix);
                    sb.append(split[i]);
                } else {
                    Object value = callback.apply(placeholder);
                    sb.append(value);
                    sb.append(split[i].substring(idxNext));
                }
            }
        }
        return sb.toString();
    }

    private JsonNode resolvePlaceholders(JsonNode json, Function<String, Object> callback) {
        if (json == null) return null;
        if (json.isObject()) {
            ObjectNode root = (ObjectNode) json;
            Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getValue().isTextual()) {
                    String value = field.getValue().textValue();
                    if (isPlaceHolder(value)) {
                        Object result = safeResolveValue(value, callback);
                        field.setValue(new POJONode(result));
                    } else if (hasPlaceHolders(value)) {
                        String result = safeResolve(value, callback);
                        field.setValue(new TextNode(result));
                    }
                } else if (field.getValue().isObject()) {
                    resolvePlaceholders(field.getValue(), callback);
                } else if (field.getValue().isArray()) {
                    ArrayNode array = (ArrayNode) field.getValue();
                    for (JsonNode jsonNode : array) {
                        resolvePlaceholders(jsonNode, callback);
                    }
                }
            }
            return root;
        } else if (json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode jsonNode : array) {
                resolvePlaceholders(jsonNode, callback);
            }
            return array;
        }
        return null;
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
            return result != null ? result : prefix.concat(key).concat(suffix);
        };
    }

    private Function<String, Object> replaceNull(Object data) {
        return key -> function(data).apply(key);
    }

    /**
     * Возвращает json-валидное значение в строке
     */
    public static Function<String, Object> replaceByJson(Function<String, Object> callback, ObjectMapper mapper) {
        return key -> {
            Object result = callback.apply(key);
            try {
                if (result instanceof String)
                    return result;
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
            if (StringUtils.isEmpty(value))
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
            if (StringUtils.isEmpty(value)) {
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
