package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;

import java.util.Map;

/**
 * Процессор сборки метаданных
 */
public interface CompileProcessor {

    /**
     * Собрать объект
     *
     * @param source  Исходный объект
     * @param context Контекст сборки
     * @param scopes  Объекты, влияющие на последующую сборку. Должны быть разных классов.
     * @param <S>     Тип исходного объекта
     * @param <D>     Тип собранного объекта
     * @return Собранный объект
     */
    <D extends Compiled, S> D compile(S source, CompileContext<?, ?> context, Object... scopes);

    /**
     * Собрать дополнительные атрибуты
     *
     * @param source исходный объект с атрибутами
     * @return собранные атрибуты
     */
    Map<String, Object> mapAttributes(ExtensionAttributesAware source);

    /**
     * Получить метаданную, оказывающую влияние на сборку
     *
     * @param scopeClass Класс метаданной
     * @param <D>        Тип скоупа
     * @return Метаданная, оказывающая влияние на сборку, или null
     */
    <D> D getScope(Class<D> scopeClass);

    /**
     * Получить собранный объект по идентификатору
     *
     * @param context Контекст сборки
     * @param <D>     Тип собранного объекта
     * @return Собранный объект
     */
    <D extends Compiled> D getCompiled(CompileContext<D, ?> context);

    /**
     * Получить исходный объект по идентификатору
     *
     * @param id          Идентификатор
     * @param sourceClass Класс исходного объекта
     * @param <S>         Тип исходного объекта
     * @return Исходный объект
     */
    <S extends SourceMetadata> S getSource(String id, Class<S> sourceClass);

    /**
     * Зарегистрировать новый маршрут метаданных под контекст
     *
     * @param context Контекст сборки
     */
    <D extends Compiled> void addRoute(CompileContext<D, ?> context);

    /**
     * Зарегистрировать новый маршрут метаданных под контекст
     *
     * @param route   Шаблон URL
     * @param context Контекст сборки
     */
    <D extends Compiled> void addRoute(String route, CompileContext<D, ?> context);


    /**
     * Заменить плейсхолдер на значение и конвертировать в класс
     *
     * @param placeholder Плейсхолдер
     * @param <T>         Тип значения
     * @return Значение
     */
    <T> T resolve(String placeholder, Class<T> clazz);

    /**
     * Заменить плейсхолдер на значение конвертировать по домену
     *
     * @param placeholder значение для конвертации
     * @param domain      Домен значения
     * @return значение
     */
    Object resolve(String placeholder, String domain);

    /**
     * Заменить плейсхолдер на значение и конвертировать с автоподбором типа
     *
     * @param placeholder значение для конвертации
     * @return значение
     */
    Object resolve(String placeholder);

    /**
     * Превратить текст с ссылками в JS код
     *
     * @param text  Текст
     * @param clazz Тип значения, если это не JS код
     * @return JS код или объект типа clazz
     */
    Object resolveJS(String text, Class<?> clazz);

    /**
     * Превратить текст с ссылками в JS код
     *
     * @param text Текст
     * @return JS код или исходная строка
     */
    default String resolveJS(String text) {
        return (String) resolveJS(text, String.class);
    }

    /**
     * Получить локализованное сообщение по коду и аргументам
     *
     * @param messageCode Код сообщения
     * @param arguments   Аргументы сообщения
     * @return Локализованное сообщение
     */
    String getMessage(String messageCode, Object... arguments);

    /**
     * Заменить свойства исходной метаданной значениями перекрывающей метаданной, если они не пусты
     *
     * @param source   Исходная метаданная
     * @param override Перекрывающая метаданная
     * @param <S>      Тип значения
     * @return Исходная метаданная с перекрытыми свойствами
     */
    <S extends Source> S merge(S source, S override);

    /**
     * Привести значение к значению по умолчанию, если оно null.
     * Если первое значение по умолчанию тоже null, берется следующее и т.д.
     *
     * @param value              Исходное значение
     * @param defaultValue1      Первое значения по умолчанию
     * @param otherDefaultValues Следующие значения по умолчанию
     * @param <T>                Тип значения
     * @return Значение приведенное к значению по умолчанию
     */
    @SuppressWarnings("unchecked")
    default <T> T cast(T value, T defaultValue1, Object... otherDefaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (otherDefaultValues != null)
            for (Object defaultValue : otherDefaultValues) {
                if (defaultValue != null) {
                    return (T) defaultValue;
                }
            }
        return null;
    }
}
