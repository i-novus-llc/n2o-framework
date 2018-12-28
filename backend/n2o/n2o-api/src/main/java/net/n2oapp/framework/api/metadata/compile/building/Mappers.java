package net.n2oapp.framework.api.metadata.compile.building;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Функции для маппинга
 */
public abstract class Mappers {

    /**
     * Обернуть значение в список
     * @param item Значение
     * @param <T> Тип значения
     * @return Список с единственным значением
     */
    public static <T> List<T> singletonList(T item) {
        List<T> list = new ArrayList<>();
        list.add(item);
        return list;
    }

    /**
     * Обернуть значение в массив
     * @param item Значение
     * @param <T> Тип значения
     * @return Массив с единственным значением
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] singletonArray(T item) {
        return (T[]) Array.newInstance(item.getClass(), 1);
    }
}
