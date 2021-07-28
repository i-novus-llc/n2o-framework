package net.n2oapp.framework.api;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.api.PlaceHoldersResolver.replaceNullByEmpty;
import static net.n2oapp.framework.api.PlaceHoldersResolver.replaceOptional;


/**
 * Утилиты для работы с особыми строками в N2O
 */
public abstract class StringUtils {
    private static PlaceHoldersResolver propertyPlaceHoldersResolver = new PlaceHoldersResolver("${", "}");
    private static PlaceHoldersResolver contextPlaceHoldersResolver = new PlaceHoldersResolver("#{", "}");
    private static PlaceHoldersResolver jsPlaceHoldersResolver = new PlaceHoldersResolver("`", "`");
    private static PlaceHoldersResolver linkPlaceHoldersResolver = new PlaceHoldersResolver("{", "}");
    private static PlaceHoldersResolver jsonPlaceHoldersResolver = new PlaceHoldersResolver("{{", "}}");
    private static final String PATTERN = "^([a-zA-Z$_][a-zA-Z0-9$_]*\\(\\))$";

    /**
     * Проверка, что строка - настройка
     * Примеры:
     * {@code
     *      isProperty("${prop}");      //true
     *      isProperty("prop");         //false
     *      isProperty("{prop}");       //false
     *      }
     * @param s - строка
     * @return Да (true), нет (false)
     */
    public static boolean isProperty(String s) {
        return propertyPlaceHoldersResolver.isPlaceHolder(s);
    }

    /**
     * Проверка, что текст содержит настройки
     * Примеры:
     * {@code
     *      hasProperty("${prop}");             //true
     *      hasProperty("ab ${prop} cd");       //true
     *      hasProperty("abcd");                //false
     *      hasProperty("ab {prop} cd");        //false
     *      }
     * @param text Текст
     * @return Содержит (true) или нет (false)
     */
    public static boolean hasProperty(String text) {
        return propertyPlaceHoldersResolver.hasPlaceHolders(text);
    }

    /**
     * Проверка, что строка - это контекст
     * Примеры:
     *      isContext("#{username}");       //true
     *      isContext("username");          //false
     *      isContext("{username}");        //false
     * @param s - строка
     * @return true - контекст, false - не контекст
     */
    public static boolean isContext(String s) {
        return contextPlaceHoldersResolver.isPlaceHolder(s);
    }

    /**
     * Проверка, что строка содержит контекст
     * Примеры:
     *      hasContext("#{username}");             //true
     *      hasContext("ab #{username} cd");       //true
     *      hasContext("ab username cd");          //false
     *      hasContext("ab {username} cd");        //false
     * @param text Текст
     * @return Содержит (true) или нет (false)
     */
    public static boolean hasContext(String text) {
        return contextPlaceHoldersResolver.hasPlaceHolders(text);
    }

    /**
     * Проверка, что значение - ссылка.
     * Примеры:
     * {@code
     *      isLink("{abc}");        //true
     *      isLink("abc");          //false
     *      isLink("{"a" : "b"}");  //false
     * }
     * @param value Значение
     * @return Является ссылкой (true)
     */
    public static boolean isLink(Object value) {
        return linkPlaceHoldersResolver.isPlaceHolder(value) && !jsonPlaceHoldersResolver.isPlaceHolder(value);
    }

    /**
     * Получение текста внутри ссылки
     *
     * @param text Ссылка
     * @return Текст внутри ссылки или null, если входящий текст не является ссылкой
     */
    public static String unwrapLink(String text) {
        return isLink(text) ? text.substring(1, text.length() - 1) : null;
    }

    /**
     * Проверка, что строка окаймлена экранированными символами
     * Примеры:
     * {@code
     *      isEscapedString("'true'");  //true
     *      isEscapedString("'123'");   //false
     *      isEscapedString("true");    //false
     * }
     * @param text Текст
     * @return
     */
    public static boolean isEscapedString(String text) {
        return text.startsWith("'") && text.endsWith("'");
    }

    /**
     * Получение текста внутри экранированных символов
     *
     * @param text Текст
     * @return Текст без экранированных символов или исходный текст
     */
    public static String unwrapEscapedString(String text) {
        return isEscapedString(text) ? text.substring(1, text.length() - 1) : text;
    }

    /**
     * Проверка, что значение - json(то есть обрамлено двойными {{ }} )
     * Примеры:
     * {@code
     *      isJson("{{"a" : "b"}}");        //true
     *      isJson("{"a" : "b"}");          //false
     * }
     * @param value Значение
     * @return Является json (true)
     */
    public static boolean isJson(Object value) {
        return jsonPlaceHoldersResolver.isPlaceHolder(value);
    }

    /**
     * Проверка, что строка содержит ссылку.
     * Примеры:
     * {@code
     *      hasLink("{username}");               //true
     *      hasLink("ab {username} cd");         //true
     *      hasLink("ab username cd");           //false
     *      hasLink("ab ${username} cd");        //false
     *      }
     * @param text Текст
     * @return Содержит (true) или нет (false)
     */
    public static boolean hasLink(String text) {
        return text != null && text.matches(".*(?<![#$])\\{.+}.*");
    }

    /**
     * Проверка, что строка - javaScript выражение
     * Примеры:
     * {@code
     *      isJs("`1 == 1`");       //true
     *      isJs("{1 == 1}");       //false
     *      isJs("1 == 1");         //false
     *      }
     * @param s - строка
     * @return true - javaScript выражение, false - не javaScript выражение
     */
    public static boolean isJs(Object s) {
        return jsPlaceHoldersResolver.isPlaceHolder(s);
    }

    /**
     * Проверка, что строка - javaScript функция
     * Примеры:
     * {@code
     *      isFunction("now()");    //true
     *      isFunction("now");      //false
     *      }
     * @param s - строка
     * @return true - javaScript функция, false - не javaScript функция
     */
    public static boolean isFunction(String s) {
        if (s == null)
            return false;
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * Проверка, что значение - динамическое (определяется на клиенте)
     * @param value Значение
     * @return true - динамическое, false - не динамическое
     */
    public static boolean isDynamicValue(Object value) {
        if (!(value instanceof String))
            return false;
        String s = (String) value;
        return isJs(s) || isContext(s) || isLink(s) || isProperty(s) || isFunction(s);
    }

    /**
     * Заменить в тексте плейсхолдеры с настройками
     * @param text Текст с плейсхолдерами ${...}
     * @param properties Значения свойств
     * @return Текст без плейсхолдеров
     */
    public static String resolveProperties(String text, Object properties) {
        return propertyPlaceHoldersResolver.resolve(text, properties);
    }

    /**
     * Заменить в тексте плейсхолдеры с настройками
     * @param text Текст с плейсхолдерами ${...}
     * @param properties Функция для получения зачений свойств
     * @return Текст без плейсхолдеров
     */
    public static String resolveProperties(String text, Function<String, Object> properties) {
        return propertyPlaceHoldersResolver.resolve(text, properties);
    }

    /**
     * Заменить в тексте плейсхолдеры с контекстом
     * @param text Текст с плейсхолдерами #{...}
     * @param context Контекст
     * @return Текст без плейсхолдеров
     */
    public static String resolveContext(String text, Context context) {
        try {
            return contextPlaceHoldersResolver.resolve(text, PlaceHoldersResolver.replaceNullByEmpty(replaceOptional(context::get)));
        } catch (NotFoundPlaceholderException e) {
            throw new NotFoundContextPlaceholderException(e.getPlaceholder());
        }
    }

    /**
     * Заменить в тексте плейсхолдеры с ссылками
     * @param text Текст с плейсхолдерами {...}
     * @param data Значения ссылок
     * @return Текст без плейсхолдеров
     */
    public static String resolveLinks(String text, Object data) {
        return linkPlaceHoldersResolver.resolve(text, replaceNullByEmpty(data));
    }

    /**
     * Заменить в тексте плейсхолдеры с ссылками
     * @param text Текст с плейсхолдерами {...}
     * @param data Функция для получения значений ссылок
     * @return Текст без плейсхолдеров
     */
    public static String resolveLinks(String text, Function<String, Object> data) {
        return linkPlaceHoldersResolver.resolve(text, PlaceHoldersResolver.replaceNullByEmpty(data));
    }

    /**
     * Собрать в тексте плейсхолдеры с ссылками
     * @param text Текст с плейсхолдерами {...}
     * @return Список параметров из плейсхолдеров
     */
    public static Set<String> collectLinks(String text) {
        return linkPlaceHoldersResolver.extractPlaceHolders(text);
    }

    /**
     * Сравнивает строку на соответствие маске
     * @param mask - маска (* - любые символы)
     * @param val - сравниваемое значение
     * @return - результат сравнения
     */
    public static boolean maskMatch(String mask, String val) {
        return mask == null || val == null ?
                mask == null && val == null :
                val.matches(maskToRegex(mask));
    }

    /**
     * Конвертирует маску в RegEx
     * @param mask - маска (* - любые символы)
     * @return - регулярное выражение
     */
    public static String maskToRegex(String mask) {
        if (mask == null) {
            return null;
        }
        if (!mask.contains("*")) {
            return "\\Q" + mask + "\\E";
        }

        StringBuilder sb = new StringBuilder();
        if (mask.startsWith("*")) {
            sb.append("(.*)");
        }
        String[] pieces = mask.split("\\*");
        for (int i = 0; i < pieces.length; i++) {
            if (!pieces[i].isEmpty()) {
                sb.append("\\Q").append(pieces[i]).append("\\E");
                if (i + 1 < pieces.length || mask.endsWith("*")) {
                    sb.append("(.*)");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Убирает переводы на новую строку, пробелы в начале и в конце
     * @param str Строка
     * @return Строка без начальных и конечныъх переводов на новую строку и пробелов
     */
    public static String simplify(String str) {
        if (str == null || str.isEmpty())
            return str;
        String result = str.trim();
        result = org.springframework.util.StringUtils.trimLeadingCharacter(result, '\n');
        result = org.springframework.util.StringUtils.trimTrailingCharacter(result, '\n');
        return result.trim();
    }

    /**
     * Проверка, что текст содержит шаблон поиска
     *
     * @param str Строка
     * @return Содержит (true) или нет (false)
     */
    public static boolean hasWildcard(String str) {
        if (str == null)
            return false;
        return str.contains("*");
    }
    public static boolean isEmpty(@Nullable Object str) {
        return (str == null || "".equals(str));
    }
}
