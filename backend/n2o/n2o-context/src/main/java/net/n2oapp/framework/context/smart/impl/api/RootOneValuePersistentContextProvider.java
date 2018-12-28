package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

/**
 * Поставщик одного корневого значения контекста с возможностью сохранения
 */
public interface RootOneValuePersistentContextProvider extends OneValuePersistentContextProvider, RootOneValueContextProvider {

    /**
     * Сохранить значение контекста
     * @param value значение
     */
    void set(Object value);

    default void set(Context ctx, Object value) {
        set(value);
    }
}
