package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;


/**
 * Провайдер корневого контекста
 */
public interface RootOneValueContextProvider extends RootContextProvider, OneValueContextProvider {

    /**
     * Значение контекста
     */
    Object getValue();

    @Override
    default Object getValue(Context context) {
        return getValue();
    }

}
