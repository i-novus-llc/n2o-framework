package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

public class DataSourcesScope extends StrictMap<String, N2oDatasource> {

    public DataSourcesScope() {
    }

    public DataSourcesScope(DataSourcesScope scope) {
        this.putAll(scope);
    }
}
