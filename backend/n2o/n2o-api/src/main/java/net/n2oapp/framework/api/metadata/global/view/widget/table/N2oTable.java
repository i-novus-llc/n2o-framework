package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;


@Getter
@Setter
@N2oComponent
public class N2oTable extends N2oAbstractTable {
    private N2oTableFilters filters;
    @N2oAttribute("Тип отображения дочерних строк таблицы")
    private ChildrenToggle children;

    @Getter
    @Setter
    public static class N2oTableFilters implements Source {
        private FilterPosition place;
        private Boolean searchOnChange;
        private SourceComponent[] items;
        private String datasourceId;
        @JsonIgnore
        private N2oStandardDatasource datasource;
        @Deprecated
        private String defaultValuesQueryId;
    }

    @Deprecated
    public void adapterV4() {
        super.adapterV4();
        if (getFilters() != null && getFilters().getDefaultValuesQueryId() != null) {
            N2oStandardDatasource datasource = new N2oStandardDatasource();
            getFilters().setDatasource(datasource);
            datasource.setQueryId(getFilters().getDefaultValuesQueryId());
            datasource.setDefaultValuesMode(DefaultValuesMode.merge);
        }
    }
}
