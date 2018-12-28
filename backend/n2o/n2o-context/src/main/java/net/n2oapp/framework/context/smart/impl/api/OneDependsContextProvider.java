package net.n2oapp.framework.context.smart.impl.api;

import java.util.Collections;
import java.util.Set;

/**
 * Поставщик значений контекста, зависящих от одного значения
 */
public interface OneDependsContextProvider extends ContextProvider {

    /**
     * Получить значение зависимого контекста
     */
    String getDependsOn();

    @Override
    default Set<String> getDependsOnParams() {
        return Collections.singleton(getDependsOn());
    }
}
