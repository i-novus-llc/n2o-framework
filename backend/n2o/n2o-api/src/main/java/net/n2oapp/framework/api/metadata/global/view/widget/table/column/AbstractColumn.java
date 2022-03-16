package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

import java.util.Map;

/**
 * Абстрактный столбец таблицы
 */
@Getter
@Setter
public abstract class AbstractColumn implements IdAware, Source, ExtensionAttributesAware {
    private String id;
    private String src;
    private String cssClass;
    private String style;
    private String textFieldId;
    private String tooltipFieldId;
    private String width;
    private String format;
    private String labelName;
    private String labelIcon;
    private LabelType labelType;
    private String visible;
    private Boolean resizable;
    private String visibilityCondition;
    private String sortingFieldId;
    private DirectionType sortingDirection;
    private ColumnFixedPosition fixed;
    private ColumnVisibility[] columnVisibilities;
    private Boolean hideOnBlur;
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
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
