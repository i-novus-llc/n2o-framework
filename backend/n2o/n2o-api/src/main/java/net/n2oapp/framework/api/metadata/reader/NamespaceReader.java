package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;

/**
 * Чтение моделей основанных на неймспейсе
 */
public interface NamespaceReader<T extends NamespaceUriAware> extends TypedElementReader<T>, NamespaceUriAware {

}
