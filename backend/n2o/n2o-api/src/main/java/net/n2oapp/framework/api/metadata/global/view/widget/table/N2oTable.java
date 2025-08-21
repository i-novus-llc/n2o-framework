package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;


@Getter
@Setter
public class N2oTable extends N2oAbstractTable {
    private N2oTableFilters filters;
    private ChildrenToggleEnum children;

    @Getter
    @Setter
    public static class N2oTableFilters implements Source {
        private FilterPositionEnum place;
        private Boolean fetchOnChange;
        private Boolean fetchOnClear;
        private Boolean fetchOnEnter;
        private SourceComponent[] items;
        private String datasourceId;
        @JsonIgnore
        private N2oStandardDatasource datasource;
        @Deprecated
        private String defaultValuesQueryId;
    }

    @Override
    @Deprecated
    public void adapterV4() {
        super.adapterV4();
        if (getFilters() != null && getFilters().getDefaultValuesQueryId() != null) {
            N2oStandardDatasource datasource = new N2oStandardDatasource();
            getFilters().setDatasource(datasource);
            datasource.setQueryId(getFilters().getDefaultValuesQueryId());
            datasource.setDefaultValuesMode(DefaultValuesModeEnum.MERGE);
        }
    }
}
