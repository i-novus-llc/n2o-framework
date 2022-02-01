package net.n2oapp.framework.config.metadata.compile.datasource;

import java.util.HashSet;
import java.util.Set;

public class DatasourceIdsScope extends HashSet<String> {

    public DatasourceIdsScope() {
    }

    public DatasourceIdsScope(Set<String> datasourceIdsScope) {
        this.addAll(datasourceIdsScope);
    }
}
