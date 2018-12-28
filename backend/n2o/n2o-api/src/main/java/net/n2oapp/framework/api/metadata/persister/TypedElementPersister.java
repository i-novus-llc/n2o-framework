package net.n2oapp.framework.api.metadata.persister;

import net.n2oapp.framework.api.metadata.aware.ElementClassAware;
import net.n2oapp.framework.api.metadata.aware.ElementNameAware;

/**
 * Запись типизированных элементов
 */
public interface TypedElementPersister<T> extends ElementPersister<T>, ElementClassAware<T>, ElementNameAware {
    Class<T> getElementClass();
}
