package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

import java.util.Map;

/**
 * Поставщик значений контекста с возможностью сохранения значений
 */
public interface PersistentContextProvider extends ContextProvider {

    /**
     * Сохранить значения контекста
     * @param ctx контекст
     * @param values значения
     */
    void set(Context ctx, Map<String, Object> values);

}
