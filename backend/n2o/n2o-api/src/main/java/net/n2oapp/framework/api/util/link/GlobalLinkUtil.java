package net.n2oapp.framework.api.util.link;

import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

/**
 * Утилиты для работы с глобальными линками
 */
@Deprecated
public class GlobalLinkUtil {
    private static final String LINK = "->";
    private static final String PREFIX = "{";
    private static final String POSTFIX = "}";

    private static PlaceHoldersResolver referencePlaceHoldersResolver = new PlaceHoldersResolver(PREFIX, POSTFIX);

    /**
     * Проверка, что строка является линком
     * @param s любая строка
     * @return true, если строка является линком
     */
    public static boolean isLink(String s) {
        boolean isPlaceHolder = referencePlaceHoldersResolver.isPlaceHolder(s);
        if (!isPlaceHolder)
            return false;
        String link = unwrap(s);
        String[] parts = link.split(LINK);
        return parts.length == 2 && !parts[0].contains("\"") && !parts[0].contains("'");
    }


    /**
     * Создание глобального линка
     * @param type - тип линка
     * @param link - линк
     * @return глобальный линк
     */
    public static String createLink(String type, String link) {
        if (type == null)
            throw new IllegalArgumentException("type can't be null");
        if (link == null)
            throw new IllegalArgumentException("link can't be null");
        return wrap(type.trim() + LINK + link.trim());
    }

    /**
     * Разрешить контекстуальные переменные "super" и "this" в ссылке
     * @param placeholder плейсхолдер
     * @param context конеткст
     * @return ссылка без контекстуальных переменных
     */
    public static String resolveSugar(String placeholder, final CompileContext context) {
        if (!isLink(placeholder)) {
            return placeholder;
        }
        String link = compress(unwrap(placeholder));
        String ref = getRef(link);
        if (ref.contains("this")) {
            link = replaceSugarThis(link, ref, context);
        }
        if (ref.contains("super")) {
            link = replaceSugarParent(link, ref, context);
        }
        return wrap(link);
    }

    /**
     * Получить тип линка
     * @param link линк
     * @return тип
     */
    public static String getType(String link) {
        return parse(link)[0];
    }

    /**
     * Получить ссылку линка
     * @param link линк
     * @return значение
     */
    public static String getRef(String link) {
        return parse(link)[1];
    }

    /**
     * Заменить переменную "this"
     * @param link линк вида a->b
     * @param ref ссылка
     * @param context контекст
     * @return линк с замененной переменной "this"
     */
    private static String replaceSugarThis(String link, String ref, final CompileContext context) {
        String key = "this";
        if (context == null)
            return link;
        String resolve = null;
        if (containsRefContainer(ref)) {
            CompileContext pageContext = findCurrentContextByType(context, N2oPage.class);
            if (pageContext == null)
                return null;
            resolve = pageContext.getId();
        } else {
            CompileContext widgetContext = findCurrentContextByType(context, N2oWidget.class);
            if (widgetContext == null)
                return null;
            resolve = widgetContext.getId();
        }
        return replaceSugar(link, key, resolve);
    }

    /**
     * Заменить переменную "super"
     * @param link линк вида a->b
     * @param ref ссылка
     * @param context контекст
     * @return линк с замененной переменной "super"
     */
    private static String replaceSugarParent(String link, String ref, final CompileContext context) {
        String key = "super";
        if (context == null)
            return link;
        CompileContext thisContext = findCurrentContextByType(context, N2oPage.class);
        if (thisContext == null)
            return link;
        String resolve = null;
        if (containsRefContainer(ref)) {
            CompileContext pageContext = findParentContextByType(thisContext, N2oPage.class);
            if (pageContext == null)
                return null;
            resolve = pageContext.getId();
        } else {
            CompileContext widgetContext = findParentContextByType(thisContext, N2oWidget.class);
            if (widgetContext == null)
                return null;
            resolve = widgetContext.getId();
        }
        return replaceSugar(link, key, resolve);
    }

    private static CompileContext findParentContextByType(final CompileContext context, Class<? extends N2oMetadata> type) {
        if (context == null)
            return null;
        if (context.getParentContextId() == null)
            return null;
        CompileContext parentContext = context.getParentContext(CompilerHolder.get());
        if (type.isAssignableFrom(parentContext.getMetadataClass())) {
            return parentContext;
        } else {
            return findParentContextByType(parentContext, type);
        }
    }

    private static CompileContext findCurrentContextByType(final CompileContext context, Class<? extends N2oMetadata> type) {
        if (context == null)
            return null;
        if (type.isAssignableFrom(context.getMetadataClass())) {
            return context;
        } else {
            if (context.getParentContextId() == null)
                return null;
            return findCurrentContextByType(context.getParentContext(CompilerHolder.get()), type);
        }
    }

    /**
     * Содержит ли ссылка контейнер
     * Примеры:
     * >containsRefContainer("myPage.main:id");   //true
     * >containsRefContainer("myPage:id");        //false
     * >containsRefContainer("myPage.main");      //true
     * @param ref ссылка
     * @return true, если содержит контейнер
     */
    private static boolean containsRefContainer(String ref) {
        String[] parts = ref.split(":");
        return parts[0].contains(".");
    }

    /**
     * Заменить контекстуальную переменную "key" на "resolve"
     * @param link линк
     * @param key контекстуальная переменная
     * @param resolve замена
     * @return линк с замененной переменной
     */
    private static String replaceSugar(String link, String key, String resolve) {
        int idx = link.indexOf(key);
        if (idx == -1) {
            return link;
        }
        String before = link.substring(0, idx);
        String after = link.substring(idx + key.length());
        return new StringBuilder(before).append(resolve).append(after).toString();
    }

    /**
     * Разбор линка на части
     * Пример: parse(" a -> b "); //["a","b"]
     * @param link линк
     * @return массив из 2х элементов: [тип линка, линк]
     * @throws IllegalArgumentException - placeholder не является глобальным линком
     */
    private static String[] parse(String link) {
        String[] parts = link.split(LINK);
        if (parts.length != 2)
            throw new IllegalArgumentException("link invalid (doesn't canResolved '->') : " + link);
        parts[0] = parts[0].trim();
        parts[1] = parts[1].trim();
        return parts;
    }

    /**
     * Сжатие линка
     * @param link линк
     * @return линк без лишних пробелов
     */
    private static String compress(String link) {
        return link.replaceAll("\\s*", "");
    }

    /**
     * Развернуть плейсхолдер в линк
     * Пример: unwrap("{a->b}"); //a->b
     * @param placeholder плейсхолдер
     * @return линк
     */
    public static String unwrap(String placeholder) {
        return referencePlaceHoldersResolver.extractPlaceHolders(placeholder).iterator().next();
    }

    /**
     * Обернуть линк в плейсхолдер
     * Пример: wrap("a->b"); //{a->b}
     * @param link линк
     * @return плейсхолдер
     */
    public static String wrap(String link) {
        return PREFIX + link + POSTFIX;
    }

}
