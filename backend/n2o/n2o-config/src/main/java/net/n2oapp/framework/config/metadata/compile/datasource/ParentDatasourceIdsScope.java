package net.n2oapp.framework.config.metadata.compile.datasource;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * Информация об идентификаторах источников данных родительской страницы
 */
@Getter
@Setter
public class ParentDatasourceIdsScope {
    private String pageId;
    private HashSet<String> datasources;

    public ParentDatasourceIdsScope(String pageId) {
        this.pageId = pageId;
    }
}

