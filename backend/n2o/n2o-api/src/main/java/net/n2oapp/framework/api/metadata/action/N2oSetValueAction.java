package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oFileAware;

/**
 * Событие set-value-expression
 */
@Getter
@Setter
public class N2oSetValueAction extends N2oAbstractAction implements N2oFileAware {
    private String expression;
    private String file;

    private String sourceDatasourceId;
    private String sourceModel;

    private String targetDatasourceId;
    private String targetModel;
    private String to;

    private MergeModeEnum mergeMode;
    private Boolean validate;
}