package net.n2oapp.framework.api.metadata.persister;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Запись метаданных в DOM элементы
 */
public interface NamespacePersister<T extends NamespaceUriAware> extends TypedElementPersister<T>, NamespaceUriAware {

}
