package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.ElementClassAware;
import net.n2oapp.framework.api.metadata.aware.ElementNameAware;

/**
 * Типизированный ридер элементов
 */
public interface TypedElementReader<T> extends ElementReader<T>, ElementClassAware<T>, ElementNameAware {

}
