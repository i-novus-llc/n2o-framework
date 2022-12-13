package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.VisualAttribute;
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
    @VisualAttribute
    private String id;
    private String src;
    @VisualAttribute
    private String cssClass;
    @VisualAttribute
    private String style;
    @VisualAttribute
    private String textFieldId;
    @VisualAttribute
    private String tooltipFieldId;
    @VisualAttribute
    private String width;
    @VisualAttribute
    private String labelName;
    @VisualAttribute
    private String labelIcon;
    @VisualAttribute
    private String visible;
    @VisualAttribute
    private Boolean resizable;
    @VisualAttribute
    private String sortingFieldId;
    @VisualAttribute
    private DirectionType sortingDirection;
    @VisualAttribute
    private ColumnFixedPosition fixed;
    private ColumnVisibility[] columnVisibilities;
    @VisualAttribute
    private Boolean hideOnBlur;
    @VisualAttribute
    private Alignment alignment;
    @VisualAttribute
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
