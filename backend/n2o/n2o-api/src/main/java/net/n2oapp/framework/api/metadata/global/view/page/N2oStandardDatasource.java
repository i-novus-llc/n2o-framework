package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель стандартного источника данных
 */
@Getter
@Setter
public class N2oStandardDatasource extends N2oDatasource implements Submittable {
    private String route;
    private String queryId;
    private String objectId;
    private Submit submit;
    private DefaultValuesMode defaultValuesMode;
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
}
