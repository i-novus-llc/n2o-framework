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
    @Deprecated
    private String targetFieldId;

    private String to;
    @Deprecated
    private String sourceWidget;
    private String sourceDatasource;
    private String sourceModel;
    @Deprecated
    private String targetWidget;
    private String targetDatasource;
    private String targetModel;
    private MergeMode mergeMode;

}
