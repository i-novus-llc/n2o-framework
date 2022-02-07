package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.List;
import java.util.Map;

/**
 * Процессор связывания метаданных с данными
 */
public interface BindProcessor {

    /**
     * Связать метаданные с данными
     *
     * @param compiled Метаданная
     * @param <D>      Тип метаданной
     */
    <D extends Compiled> void bind(D compiled);

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
     * Заменить плейсхолдер на значение
     *
     * @param placeholder Плейсхолдер
     * @param <T>         Тип значения
     * @return Значение
     */
    <T> T resolve(String placeholder, Class<T> clazz);

    /**
     * Конвентировать значение в объект по домену
     *
     * @param value  значение для конвертации
     * @param domain Домен значения
     * @return значение
     */
    Object resolve(String value, String domain);

    /**
     * Конвентировать значение в объект
     *
     * @param value значение для конвертации
     * @return значение
     */
    Object resolve(String value);

    /**
     * Заменить в тексте плейсхолдеры на значения и конвертировать в объект
     *
     * @param value значение для конвертации
     * @return значение
     */
    Object resolve(Object value);

    /**
     * Заменить в тексте плейсхолдеры на значения и конвертировать в объект
     *
     * @param value значение для конвертации
     * @param <T>         Тип значения
     * @return значение
     */
    <T> T resolve(Object value, Class<T> clazz);

    /**
     * Заменить в тексте плейсхолдеры на значения
     *
     * @param text Текст с плейсхолдерами
     * @return Текст со значениями вместо плейсхолдеров
     */
    String resolveText(String text);

    /**
     * Заменить в тексте плейсхолдеры на значения, используя модель
     *
     * @param text Текст с плейсхолдерами
     * @param link Ссылка на модель, на которую ссылаются плейсхолдеры
     * @return Текст со значениями вместо плейсхолдеров
     */
    String resolveText(String text, ModelLink link);

    /**
     * Заменить в строке плейсхолдеры {...} на значения, кроме исключений
     *
     * @param url Строка с плейсхолдерами
     * @return Строка со значениями вместо плейсхолдеров
     */
    String resolveUrl(String url);

    /**
     * Заменить в адресе плейсхолдеры на значения
     *
     * @param url           Адрес
     * @param pathMappings  path параметры
     * @param queryMappings query параметры
     * @return Адрес со значениями вместо плейсхолдеров
     */
    String resolveUrl(String url,
                      Map<String, ? extends BindLink> pathMappings,
                      Map<String, ? extends BindLink> queryMappings);

    /**
     * Заменить в адресе параметры, которые ссылаются на переданную модель
     *
     * @param url  Адрес
     * @param link Ссылка на модель, по которой определяем какие параметры необходимо заменить
     * @return Измененный адрес
     */
    String resolveUrl(String url, ModelLink link);

    /**
     * Пытается превратить ссылку в константное значение.
     *
     * @param link Ссылка
     */
    default BindLink resolveLink(BindLink link) {
        return resolveLink(link, false);
    }

    /**
     * Пытается превратить ссылку в константное значение, если ссылка не меняется (observable=false)
     *
     * @param observable Превращать ли ссылку в константу, если ссылка может измениться на текущей странице?
     * @param link       Ссылка
     */
    BindLink resolveLink(BindLink link, boolean observable);

    /**
     * Получение значения параметра из адресной строки по ссылке
     *
     * @param link Ссылка
     * @return Значение параметра
     */
    Object resolveLinkValue(ModelLink link);

    /**
     * Попытаться разрешить вложенные модели ссылки
     *
     * @param link ссылка на значение
     */
    ModelLink resolveSubModels(ModelLink link);

    /**
     * Получить значение выборки с текущими параметрами запроса
     *
     * @param queryId Идентификатор выборки
     */
    DataSet executeQuery(String queryId);

    /**
     * Получить локализованное сообщение по коду и аргументам
     *
     * @param messageCode Код сообщения
     * @param arguments   Аргументы сообщения
     * @return Локализованное сообщение
     */
    String getMessage(String messageCode, Object... arguments);


    /**
     * Есть ли данные для разрешения параметра
     *
     * @param param
     * @return
     */
    boolean canResolveParam(String param);

}
