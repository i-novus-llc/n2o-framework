package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;

/**
 * Событие set-value-expression
 */
@Getter
@Setter
public class N2oSetValueAction extends N2oAbstractAction {
    private String expression;
    private String targetFieldId;

    private String to;
    private String sourceDatasourceId;
    private String sourceModel;
    private String targetDatasourceId;
    private String targetModel;
    private MergeMode mergeMode;

    @Deprecated
    public String getSourceWidget() {
        return sourceDatasourceId;
    }

    @Deprecated
    public void setSourceWidget(String sourceWidget) {
        this.sourceDatasourceId = sourceWidget;
    }

    @Deprecated
    public String getTargetWidget() {
        return targetDatasourceId;
    }

    @Deprecated
    public void setTargetWidget(String targetWidget) {
        this.targetDatasourceId = targetWidget;
    }
}
