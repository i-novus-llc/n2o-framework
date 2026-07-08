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

    private String sourceDatasourceId;
    private String sourceModel;

    private String targetDatasourceId;
    private String targetModel;
    private String to;

    private MergeModeEnum mergeMode;
    private Boolean validate;
}
