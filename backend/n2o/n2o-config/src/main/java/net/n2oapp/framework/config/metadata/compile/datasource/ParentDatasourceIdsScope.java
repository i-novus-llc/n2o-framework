package net.n2oapp.framework.config.metadata.compile.datasource;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Информация об идентификаторах источников данных родительской страницы
 */
@Getter
@Setter
public class ParentDatasourceIdsScope {
    private Map<String, String> datasourceIdsMap;

    public ParentDatasourceIdsScope(Map<String, String> datasourceIdsMap) {
        this.datasourceIdsMap = datasourceIdsMap;
    }
}

