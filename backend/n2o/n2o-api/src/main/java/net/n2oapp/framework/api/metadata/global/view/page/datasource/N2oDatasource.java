package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель базового источника данных
 */
@Getter
@Setter
public abstract class N2oDatasource extends N2oAbstractDatasource implements NamespaceUriAware {

    private Dependency[] dependencies;

    /**
     * Зависимости
     */
    @Getter
    @Setter
    public abstract static class Dependency implements Source {
        private String on;
    }

    @Getter
    @Setter
    public static class FetchDependency extends Dependency {
        private ReduxModelEnum model;
    }

    @Getter
    @Setter
    public static class CopyDependency extends Dependency {
        private String targetFieldId;
        private ReduxModelEnum targetModel;
        private ReduxModelEnum sourceModel;
        private String sourceFieldId;
        private Boolean submit;
        private Boolean applyOnInit;
    }
}
