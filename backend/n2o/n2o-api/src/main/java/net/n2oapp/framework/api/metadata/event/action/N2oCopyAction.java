package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;

/**
 * Событие копирования модели
 */
@Getter
@Setter
public class N2oCopyAction extends N2oAbstractAction implements N2oAction {
    private ReduxModel sourceModel;
    private String sourceDatasource;
    private String sourceFieldId;

    private ReduxModel targetModel;
    private String targetDatasource;
    private String targetFieldId;
    private String targetClientPageId;
    private CopyMode mode;

    @Deprecated
    public String getSourceWidgetId() {
        return sourceDatasource;
    }

    @Deprecated
    public void setSourceWidgetId(String sourceWidgetId) {
        this.sourceDatasource = sourceWidgetId;
    }

    @Deprecated
    public String getTargetWidgetId() {
        return targetDatasource;
    }

    @Deprecated
    public void setTargetWidgetId(String targetWidgetId) {
        this.targetDatasource = targetWidgetId;
    }
}
