package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

import java.util.Map;

/**
 * Поставщик одного значения контекста с возможностью сохранения
 */
public interface OneValuePersistentContextProvider extends OneValueContextProvider, PersistentContextProvider {

    /**
     * Сохранить значение контекста
     */
    void set(Context ctx, Object value);

    default void set(Context ctx, Map<String, Object> values) {
        set(ctx, values.get(getParam()));
    }
}
