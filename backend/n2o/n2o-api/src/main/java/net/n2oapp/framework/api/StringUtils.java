package net.n2oapp.framework.api;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;
import net.n2oapp.framework.api.util.RefUtil;
import net.n2oapp.framework.api.util.link.GlobalLinkUtil;
import net.n2oapp.framework.api.util.link.PageLinkUtil;

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
     * @return Соджержит (true) или нет (false)
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
        return linkPlaceHoldersResolver.isPlaceHolder(value) && ((String)value).matches("\\{[\\w.]+}");
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
     * @return Соджержит (true) или нет (false)
     */
    public static boolean hasLink(String text) {
        return isLink(text) || linkPlaceHoldersResolver.hasPlaceHolders(text);
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
        return s instanceof String && jsPlaceHoldersResolver.isPlaceHolder(s);
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
     * Проверка, что строка - это ссылка
     * Примеры:
     *      isRef("{id}");      //true
     *      isRef("{a -> b}");  //true
     *      isRef("text{id}");  //false
     *      isRef("id");        //false
     *      isRef("${id}");     //false
     * @param s - строка
     * @return true - ссылка, false - не ссылка
     */
    @Deprecated
    public static boolean isRef(String s) {
        return RefUtil.isRef(s);
    }

    /**
     * Проверка, что строка - глобальная ссылка
     * Примеры:
     *      isGlobalLink("{a->b}");             //true
     *      isGlobalLink("{ a -> b }");         //true
     *      isGlobalLink("{ a -> b.c:d }");     //true
     *      isGlobalLink("{ a - > b }");        //false
     *      isGlobalLink("{a.b}");              //false
     *      isGlobalLink("{->a.b}");            //false
     *      isGlobalLink("${a->b}");            //false
     * @param s - строка
     * @return true - глобальная ссылка, false - не глобальная ссылка
     */
    @Deprecated
    public static boolean isGlobalLink(String s) {
        return GlobalLinkUtil.isLink(s);
    }

    // создание global-линков
    @Deprecated //use PageLinkUtil.createSelectLink
    public static String toLink(String containerId, String fieldId) {
        return PageLinkUtil.createSelectLink(containerId, fieldId);
    }

    @Deprecated //use PageLinkUtil.createSelectLink
    public static String toLink(String pageId, String containerId, String fieldId) {
        return toLink(pageId + "." + containerId, fieldId);
    }

    @Deprecated //use PageLinkUtil.createSelectLink
    public static String toLinkWithoutField(String pageId, String containerId) {
        return toLink(pageId, containerId, null);
    }

}
