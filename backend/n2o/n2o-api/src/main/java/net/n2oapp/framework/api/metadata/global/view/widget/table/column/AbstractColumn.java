package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
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
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Getter
    @Setter
    public static class ColumnVisibility implements Source {
        private String value;
        private String refWidgetId;
        private ReduxModel refModel;
    }
}
