package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Добавить фильтры
     *
     * @param filters Список фильтров
     */
    public void addFilters(List<N2oPreFilter> filters) {
        List<N2oPreFilter> list = new ArrayList<>();
        if (this.filters != null)
            list.addAll(Arrays.asList(this.filters));
        list.addAll(filters);
        this.filters = list.toArray(new N2oPreFilter[list.size()]);
    }

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
