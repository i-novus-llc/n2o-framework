package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;

import java.util.*;

public class DataSourcesScope extends HashMap<String, N2oAbstractDatasource> {
    public DataSourcesScope() {
        super();
    }

    public DataSourcesScope(Map<String, ? extends N2oAbstractDatasource> m) {
        super(m);
    }
}