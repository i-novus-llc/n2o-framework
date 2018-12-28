package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Реестр метаданных
 */
public interface MetadataRegister {
    /**
     * Зарегистрировать метаданную
     *
     * @param info Мета информация
     * @param <I>  Тип мета информации
     */
    <I extends SourceInfo> void add(I info);

    /**
     * Зарегистрировать список метаданных
     *
     * @param infoList Список мета информации
     * @param <I>      Тип мета информации
     */
    <I extends SourceInfo> void addAll(Collection<I> infoList);

    /**
     * Получить мета информацию о метаданной
     *
     * @param id          Идентификатор метаданной
     * @param sourceClass Исходный класс метаданной
     * @return Мета информация
     */
    SourceInfo get(String id, Class<? extends SourceMetadata> sourceClass);

    /**
     * Найти информацию о метаданных определенного класса
     *
     * @param sourceClass класс
     * @return Найденные метаданные
     */
    List<SourceInfo> find(Class<? extends SourceMetadata> sourceClass);

    /**
     * Найти информацию о метаданных
     *
     * @param criteria Критерий поиска
     * @return Найденные метаданные
     */
    List<SourceInfo> find(Predicate<SourceInfo> criteria);

    /**
     * Найти информацию о метаданных определнного типа
     *
     * @param criteria  Критерий поиска
     * @param infoClass Класс типа информации
     * @return Найденные метаданные
     */
    <I extends SourceInfo> List<I> find(Predicate<I> criteria, Class<I> infoClass);

    /**
     * Удалить информацию из реестра
     *
     * @param id          Идентификатор метаданной
     * @param sourceClass Исходный класс метаданной
     */
    void remove(String id, Class<? extends SourceMetadata> sourceClass);

    /**
     * Очистить реестр полностью
     */
    void clearAll();

    /**
     * Обновить информацию о метаданной
     *
     * @param info Информация о метаданной
     * @param <I>  Тип информации
     */
    <I extends SourceInfo> void update(I info);

    /**
     * Содержит ли реестр информацию о метаданной
     *
     * @param id          Идентификатор метаданной
     * @param sourceClass Исходный клксс метаданной
     * @return Содержит или нет
     */
    boolean contains(String id, Class<? extends SourceMetadata> sourceClass);
}
