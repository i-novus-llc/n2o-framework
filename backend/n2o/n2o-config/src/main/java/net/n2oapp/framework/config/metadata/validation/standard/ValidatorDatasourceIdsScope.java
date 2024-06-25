package net.n2oapp.framework.config.metadata.validation.standard;

import java.util.HashSet;
import java.util.Set;

/**
 * Информация о идентификаторах источников данных, ссылающихся на источник из application.xml
 */
public class ValidatorDatasourceIdsScope extends HashSet<String> {

    public ValidatorDatasourceIdsScope() {
    }

    public ValidatorDatasourceIdsScope(Set<String> datasourceIdsScope) {
        this.addAll(datasourceIdsScope);
    }
}
