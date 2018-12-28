package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * Утилиты для работы с ссылками
 * @see StringUtils
 */
@Deprecated
public class RefUtil {
    private static PlaceHoldersResolver referencePlaceHoldersResolver = new PlaceHoldersResolver("{", "}");

    /**
     * Проверка, что текст содержит ссылку на модель (например, "table_{name}")
     * или значение вычисляемое на клиенте (например, "`'page_'+table`"
     * @param text - текст
     * @return true - есть ссылки, false - нет ссылок (простой текст)
     */
    public static boolean hasRefs(String text) {
        return StringUtils.hasLink(text) || StringUtils.isJs(text);
    }

    /**
     * Проверка, что текст это ссылка на модель (например, "{name}")
     * @param text - текст
     * @return true - ссылка, false - текст
     */
    public static boolean isRef(String text) {
        boolean isPlaceholder = StringUtils.isLink(text);
        return isPlaceholder && !text.contains("\"") && !text.contains("'");
    }


    public static String resolve(String text, Map<String, String> placeholders) {
        return StringUtils.resolveLinks(text, placeholders);
    }

    /**
     * Получить все ссылки в тексте
     * @param text - текст со ссылками (например, "привет, {name} {surname}")
     * @return набор ссылок или пустой набор
     */
    public static Set<String> getRefs(String text) {
        return referencePlaceHoldersResolver.extractPlaceHolders(text);
    }

    /**
     * Получить ссылку из текста
     * @param text - текст обернутый в ссылку
     * @return ссылка или null
     */
    public static String unwrapRef(String text) {
        if (!isRef(text))
            return null;
        return text.substring(1, text.length() - 1);
    }

    @Deprecated //@use hasRefs
    public static boolean isDynamic(String text) {
        return text != null && text.contains("{") && text.contains("}");
    }
}
