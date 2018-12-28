package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.ElementNameAware;

/**
 * Чтение / запись моделей определенного элемента
 * @param <T> Тип модели
 */
public interface NamedElementIO<T> extends ElementIO<T>, ElementNameAware {
}
