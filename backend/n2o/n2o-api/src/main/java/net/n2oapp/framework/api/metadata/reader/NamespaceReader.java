package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Чтение моделей основанных на неймспейсе
 */
public interface NamespaceReader<T extends NamespaceUriAware> extends TypedElementReader<T>, NamespaceUriAware {

}
