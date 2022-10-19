package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;

/**
 * Исходная модель источника данных родительской страницы
 */
public class N2oParentDatasource extends N2oAbstractDatasource {

    public N2oParentDatasource() {
    }

    public N2oParentDatasource(String id) {
        setId(id);
    }
}
