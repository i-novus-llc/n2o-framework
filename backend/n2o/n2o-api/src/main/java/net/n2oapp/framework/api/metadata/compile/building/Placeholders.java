package net.n2oapp.framework.api.metadata.compile.building;

/**
 * Функции для оборачивания ссылок в плейсхолдеры
 */
public abstract class Placeholders {

    /**
     * Обернуть код сообщения в плейсхолдер
     *
     * @param messageCode Код сообщения
     * @return Плейсхолдер сообщения
     */
    public static String message(String messageCode) {
        return "${" + messageCode + "}";
    }

    /**
     * Обернуть свойство в плейсхолдер
     *
     * @param property Свойство
     * @return Плейсхолдер свойства
     */
    public static String property(String property) {
        return "${" + property + "}";
    }

    /**
     * Обернуть контекст пользователя в плейсхолдер
     *
     * @param context Контекст пользователя
     * @return Плейсхолдер контекста пользователя
     */
    public static String context(String context) {
        return "#{" + context + "}";
    }

    /**
     * Обернуть ссылку в плейсхолдер
     *
     * @param reference Ссылка
     * @return Плейсхолдер ссылки
     */
    public static String ref(String reference) {
        return "{" + reference + "}";
    }


    /**
     * Параметризация двоеточием
     *
     * @param parameter Параметр
     * @return Параметр с двоеточнием спереди
     */
    public static String colon(String parameter) {
        return ":" + parameter;
    }

    /**
     * Параметризация хештегом
     *
     * @param parameter Параметр
     * @return Хештег
     */
    public static String hash(String parameter) {
        return "#" + parameter;
    }

    /**
     * Параметризация "собакой"
     *
     * @param parameter Параметр
     * @return "Собака" тег
     */
    public static String at(String parameter) {
        return "@" + parameter;
    }

    /**
     * Выражение доступа по ключу карты через SpEL
     *
     * @param parameter Ключ карты
     * @return Выражение на SpEL
     */
    public static String spel(String parameter) {
        return "['" + parameter + "']";
    }

    /**
     * Параметризация javaScript выражением
     *
     * @param script javaScript выражение
     * @return Выражение обернутое в обратные апострофы
     */
    public static String js(String script) {
        return "`" + script + "`";
    }
}
