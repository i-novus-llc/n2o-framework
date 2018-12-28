package net.n2oapp.framework.api.context;

import java.util.Collections;
import java.util.Map;

/**
 * Контекст
 * @author iryabov
 * @since 22.07.2016
 */
public interface Context {
    /**
     * Получение значения контекста по имени
     * @param name - имя контекста
     * @return значение
     */
    Object get(String name);

    /**
     * Сохранение значений в контекст
     * @param dataSet - значения
     */
    void set(Map<String, Object> dataSet);

    /**
     * Сохранение значения конеткста
     * @param name имя контекста
     * @param value значение
     */
    default void set(String name, Object value) {
        set(Collections.singletonMap(name, value));
    }

}
