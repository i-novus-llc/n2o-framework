package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;

/**
 * Исходная модель источника, ссылающегося на источник из application.xml
 */
@Getter
@Setter
public class N2oApplicationDatasource extends N2oAbstractDatasource {

    /**
     * Идентификатор источника данных для получения данных с родительской страницы, указывается если идентификаторы
     * источника данных на родительской и открываемой странице отличаются
     */
    private String sourceDatasource;
}
