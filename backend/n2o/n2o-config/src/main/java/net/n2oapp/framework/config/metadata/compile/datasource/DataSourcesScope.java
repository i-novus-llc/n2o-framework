package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;

public class DataSourcesScope extends StrictMap<String, N2oAbstractDatasource> {

    public DataSourcesScope() {
    }

    public DataSourcesScope(Map<? extends String, ? extends N2oAbstractDatasource> m) {
        super(m);
    }
}
