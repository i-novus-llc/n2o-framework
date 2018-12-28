package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;

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
    RoutingResult get(String url);
}
