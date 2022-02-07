package net.n2oapp.framework.api.metadata.event.action;

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
    private String sourceDatasource;
    private String sourceModel;
    private String targetDatasource;
    private String targetModel;
    private MergeMode mergeMode;

    @Deprecated
    public String getSourceWidget() {
        return sourceDatasource;
    }

    @Deprecated
    public void setSourceWidget(String sourceWidget) {
        this.sourceDatasource = sourceWidget;
    }

    @Deprecated
    public String getTargetWidget() {
        return targetDatasource;
    }

    @Deprecated
    public void setTargetWidget(String targetWidget) {
        this.targetDatasource = targetWidget;
    }
}
