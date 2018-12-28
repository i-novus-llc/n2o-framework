package net.n2oapp.framework.engine.sql.jdbc;

import net.n2oapp.routing.datasource.RuntimeRoutingDataSource;

/**
 * Инициализация динамического источника данных
 */
public interface RoutingDataSourceInitializer {
    /**
     * Инициализровать источники данных
     * {@code routingDataSource.addDataSource("jdbc/my", myDataSource);}
     * @param routingDataSource Динамический источник данных
     */
    void initialize(RuntimeRoutingDataSource routingDataSource);
}
