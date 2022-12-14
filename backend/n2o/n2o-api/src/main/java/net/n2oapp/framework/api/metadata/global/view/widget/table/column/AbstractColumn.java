package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Абстрактный столбец таблицы
 */
@Getter
@Setter
public abstract class AbstractColumn implements IdAware, Source, ExtensionAttributesAware {
    @N2oAttribute
    private String id;
    private String src;
    @N2oAttribute
    private String cssClass;
    @N2oAttribute
    private String style;
    @N2oAttribute
    private String textFieldId;
    @N2oAttribute
    private String tooltipFieldId;
    @N2oAttribute
    private String width;
    @N2oAttribute
    private String labelName;
    @N2oAttribute
    private String labelIcon;
    @N2oAttribute
    private String visible;
    @N2oAttribute
    private Boolean resizable;
    @N2oAttribute
    private String sortingFieldId;
    @N2oAttribute
    private DirectionType sortingDirection;
    @N2oAttribute
    private ColumnFixedPosition fixed;
    private ColumnVisibility[] columnVisibilities;
    @N2oAttribute
    private Boolean hideOnBlur;
    @N2oAttribute
    private Alignment alignment;
    @N2oAttribute
    private Alignment contentAlignment;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Getter
    @Setter
    public static class ColumnVisibility implements Source, DatasourceIdAware {
        private String value;
        private String datasourceId;
        private ReduxModel model;

        @Deprecated
        public String getRefWidgetId() {
            return datasourceId;
        }

        @Deprecated
        public void setRefWidgetId(String refWidgetId) {
            this.datasourceId = refWidgetId;
        }
    }
}
