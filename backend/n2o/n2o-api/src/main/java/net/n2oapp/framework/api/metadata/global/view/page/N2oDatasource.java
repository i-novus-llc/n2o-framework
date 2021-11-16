package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Источник данных
 */
@Getter
@Setter
public class N2oDatasource implements Source {

    private String id;
    private String queryId;
    private String objectId;
    private String route;
    private Integer size;
    private DefaultValuesMode defaultValuesMode;
    private Submit submit;
    private Dependency[] dependencies;
    private N2oPreFilter[] filters;

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
