package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель базового источника данных
 */
@Getter
@Setter
public abstract class N2oDatasource extends N2oAbstractDatasource implements NamespaceUriAware{

    private Integer size;
    private Dependency[] dependencies;

    /**
     * Зависимости
     */
    @Getter
    @Setter
    public static class Dependency implements Source {
    }

    @Getter
    @Setter
    public static class FetchDependency extends Dependency {
        private String on;
        private ReduxModel model;
    }
}
