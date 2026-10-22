package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;

import java.util.*;

public class DataSourcesScope extends HashMap<String, N2oAbstractDatasource> {
    public DataSourcesScope() {
        super();
    }

    public DataSourcesScope(Map<String, ? extends N2oAbstractDatasource> m) {
        super(m);
    }

    /**
     * Добавление источника данных в scope.
     * Если источник с таким идентификатором уже есть, он сливается с добавляемым
     *
     * @param datasource Добавляемый источник данных
     * @param p          Процессор сборки метаданных
     */
    public void putOrMerge(N2oAbstractDatasource datasource, CompileProcessor p) {
        N2oAbstractDatasource existing = get(datasource.getId());
        put(datasource.getId(), existing != null ? p.merge(datasource, existing) : datasource);
    }
}
