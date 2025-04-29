package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;

/**
 * Событие копирования модели
 */
@Getter
@Setter
public class N2oCopyAction extends N2oAbstractAction implements N2oAction {
    private String sourceDatasourceId;
    private ReduxModelEnum sourceModel;
    private String sourceFieldId;

    private String targetDatasourceId;
    private ReduxModelEnum targetModel;
    private String targetFieldId;
    private PageRefEnum targetPage;

    private Boolean closeOnSuccess;
    private CopyModeEnum mode;
    private Boolean validate;

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
