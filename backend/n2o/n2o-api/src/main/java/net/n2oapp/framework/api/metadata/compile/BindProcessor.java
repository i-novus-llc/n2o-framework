package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.BindLink;
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
     * @param url    Адрес
     * @param link   Ссылка на модель, по которой определяем какие параметры необходимо заменить
     * @return Измененный адрес
     */
    String resolveUrl(String url, ModelLink link);

    /**
     * Попытаться разрешить значение ModelLink
     *
     * @param link исходная ссылка на значение
     * @return ссылка с константой(если получилось разрешить ссылку) или исходная ссылка
     */
    ModelLink resolveLink(ModelLink link);

    /**
     * Попытаться разрешить вложенные модели ссылки
     *
     * @param link  ссылка на значение
     * @param links исходный список ссылок
     */
    void resolveSubModels(ModelLink link, List<ModelLink> links);

}
