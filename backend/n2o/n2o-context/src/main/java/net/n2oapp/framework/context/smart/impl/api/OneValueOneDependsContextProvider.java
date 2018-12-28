package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

/**
 * Поставщик одного значения контекста, зависящего от другого значения
 */
public interface OneValueOneDependsContextProvider extends OneValueContextProvider, OneDependsContextProvider {

    /**
     * Получить значение контекста, зависящего от другого значения
     * @param dependsOnValue другое значение
     * @return значение контекста
     */
    Object getValue(Object dependsOnValue);

    default Object getValue(Context context) {
        return getValue(context.get(getDependsOn()));
    }

}
