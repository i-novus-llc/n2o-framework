package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;

/**
 * Исходная модель источника данных родительской страницы
 */
@Getter
@Setter
public class N2oParentDatasource extends N2oAbstractDatasource {
    /**
     * Поле является признаком того, что источник был неявно проброшен с родительской страницы (для обратной совместимости!),
     * и не является источником, который задан на текущей странице с помощью элемента <parent-datasource/>
     */
    private boolean fromParentPage;

    /**
     * Идентификатор источника данных родительской страницы. Используется, если id текущего датасорса и id датасорса,
     * на который он ссылается, должны отличаться.
     */
    private String sourceDatasource;

    public N2oParentDatasource() {
    }

    public N2oParentDatasource(String id, String sourceDatasource, boolean fromParentPage) {
        setId(id);
        this.sourceDatasource = sourceDatasource;
        this.fromParentPage = fromParentPage;
    }
}
