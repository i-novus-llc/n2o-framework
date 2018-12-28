package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

import java.util.Map;
import java.util.Set;

/**
 * Поставщик значений контекста
 */
public interface ContextProvider {

    /**
     * Поставщик параметров контекста
     * Если в методе имеется обращение к различным параметрам из контекста, то считается что параметры этого провайдера
     * зависят от других. Набор параметров, от которых зависит провайдер, всегда должен быть один и тот же и не должен
     * зависеть от данных.
     */
    Map<String, Object> get(Context ctx);

    /**
     * Получить набор параметров поставщика контекста
     */
    Set<String> getParams();

    /**
     * Получить параметры, от которых зависит поставщик контекста
     */
    Set<String> getDependsOnParams();

    /**
     * Показывает надо ли кэшировать значения возвращаемые этим поставщиком.
     * По умолчанию, если поставщик зависит от каких-либо других параметров, то кешировать надо.
     */
    default boolean isCacheable() {
        return getDependsOnParams() != null && !getDependsOnParams().isEmpty();
    }
}
