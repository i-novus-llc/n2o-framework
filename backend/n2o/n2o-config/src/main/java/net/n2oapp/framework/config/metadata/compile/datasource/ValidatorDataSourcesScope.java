package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;

import java.util.Map;

public class ValidatorDataSourcesScope extends DataSourcesScope{

    public ValidatorDataSourcesScope() {
        super();
    }

    public ValidatorDataSourcesScope(Map<? extends String, ? extends N2oAbstractDatasource> m) {
        super(m);
    }
}
