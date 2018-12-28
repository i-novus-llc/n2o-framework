package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

/**
 * Поставщик одного значения контекста с возможностью сохранения, зависимый от другого значения
 */
public interface OneValueOneDependsOnPersistentContextProvider extends OneValuePersistentContextProvider, OneValueOneDependsContextProvider {

    /**
     * Сохранить значение контекста, зависящего от другого контекста
     * @param dependsOnValue другое значение
     * @param value сохраняемое значение
     */
    void set(Object dependsOnValue, Object value);

    default void set(Context ctx, Object value) {
        set(ctx.get(getDependsOn()), value);
    }

}
