package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import java.util.Map;

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
    <D extends Compiled> CompileContext<D, ?> get(String url, Class<D> compiledClass, Map<String, String[]> params);
}
