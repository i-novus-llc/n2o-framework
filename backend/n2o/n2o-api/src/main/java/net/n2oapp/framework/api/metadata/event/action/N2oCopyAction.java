package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;

/**
 * Событие копирования модели
 */
@Getter
@Setter
public class N2oCopyAction extends N2oAbstractAction implements N2oAction {
    private ReduxModel sourceModel;
    private String sourceDatasourceId;
    private String sourceFieldId;

    private ReduxModel targetModel;
    private String targetDatasourceId;
    private String targetFieldId;
    private PageRef targetPage;
    private Boolean closeOnSuccess;
    private CopyMode mode;

    @Deprecated
    public String getSourceWidgetId() {
        return sourceDatasourceId;
    }

    @Deprecated
    public void setSourceWidgetId(String sourceWidgetId) {
        this.sourceDatasourceId = sourceWidgetId;
    }

    @Deprecated
    public String getTargetWidgetId() {
        return targetDatasourceId;
    }

    @Deprecated
    public void setTargetWidgetId(String targetWidgetId) {
        this.targetDatasourceId = targetWidgetId;
    }
}
