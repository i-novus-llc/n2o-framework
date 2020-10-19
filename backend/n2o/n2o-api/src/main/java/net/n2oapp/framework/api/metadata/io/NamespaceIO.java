package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Чтение / запись типизированных метаданных основанных на неймспейсе
 * @param <T> Тип модели
 */
public interface NamespaceIO<T extends NamespaceUriAware>
        extends TypedElementIO<T>, NamespaceUriAware {
}
