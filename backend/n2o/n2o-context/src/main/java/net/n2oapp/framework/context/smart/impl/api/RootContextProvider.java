package net.n2oapp.framework.context.smart.impl.api;

import java.util.Collections;
import java.util.Set;

/**
 * Поставщик корневых значений контекста
 */
public interface RootContextProvider extends ContextProvider {

    @Override
    default Set<String> getDependsOnParams() {
        return Collections.emptySet();
    }
}
