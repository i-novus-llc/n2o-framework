package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

/**
 * Маршрутизатор URL адресов метаданных.
 */
public interface MetadataRouter {
    /**
     * Поиск метаданных по URL адресу и типу.
     *
     * @param url           URL адрес
     * @return Маршрут
     */
    <D extends Compiled> CompileContext<D, ?> get(String url, Class<D> compiledClass);
}
