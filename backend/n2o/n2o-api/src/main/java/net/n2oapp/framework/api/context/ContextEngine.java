package net.n2oapp.framework.api.context;

import java.util.Map;

/**
 * Сервис для получения значений контекста
 * @author iryabov
 * @since 22.07.2016
 */
public interface ContextEngine extends Context {
    /**
     * Получение контекста по базовым параметрам
     * Например, можно получить контекст конкретного пользователя
     * @param param - имя контекста
     * @param baseParams - базовые значения контекста
     * @return значение контекста
     */
    Object get(String param, Map<String, Object> baseParams);

    /**
     * Установка контекста по базовым параметрам
     * @param dataSet - контекст
     * @param baseParams - базовые параметры
     */
    void set(Map<String, Object> dataSet, Map<String, Object> baseParams);

    /**
     * Обновление контекста
     */
    default void refresh() {  }

}
