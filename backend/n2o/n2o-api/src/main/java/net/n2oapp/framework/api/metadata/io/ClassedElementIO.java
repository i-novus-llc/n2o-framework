package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.ElementClassAware;

/**
 * Чтение / запись моделей определенного типа
 * @param <T> Тип модели
 */
public interface ClassedElementIO<T> extends ElementIO<T>,
        ElementClassAware<T> {

}
