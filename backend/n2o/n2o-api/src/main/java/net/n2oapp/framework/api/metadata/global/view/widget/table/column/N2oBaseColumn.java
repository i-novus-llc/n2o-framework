package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Исходная модель базового столбца таблицы
 */
@Getter
@Setter
public abstract class N2oBaseColumn extends N2oAbstractColumn implements ExtensionAttributesAware {
    private String cssClass;
    private String style;
    private String textFieldId;
    private String tooltipFieldId;
    private String width;
    private String label;
    private String icon;
    private String visible;
    private Boolean resizable;
    private String sortingFieldId;
    private SortingDirectionEnum sortingDirection;
    private ColumnVisibility[] columnVisibilities;
    private AlignmentEnum alignment;
    private AlignmentEnum contentAlignment;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Getter
    @Setter
    public static class ColumnVisibility implements Source, DatasourceIdAware {
        private String value;
        private String datasourceId;
        private ReduxModelEnum model;
    }
}
