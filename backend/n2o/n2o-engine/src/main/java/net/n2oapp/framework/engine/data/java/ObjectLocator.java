package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;

/**
 * Поиск объекта по DI провайдеру
 * @param <T> Тип DI провайдера
 */
public interface ObjectLocator<T extends DIProvider> {

    Object locate(Class<?> targetClass, T provider);

    boolean match(DIProvider provider);
}
