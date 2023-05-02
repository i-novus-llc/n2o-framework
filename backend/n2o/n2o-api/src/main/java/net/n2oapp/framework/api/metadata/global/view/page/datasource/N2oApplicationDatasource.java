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
     * Идентификатор источника данных из приложения. Используется, если id текущего датасорса и id датасорса,
     * на который он ссылается, должны отличаться. Например, при пробрасывание датасорса на следующую страницу
     */
    private String sourceDatasource;
}
