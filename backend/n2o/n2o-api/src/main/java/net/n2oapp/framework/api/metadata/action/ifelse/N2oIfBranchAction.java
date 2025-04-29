package net.n2oapp.framework.api.metadata.action.ifelse;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Исходная модель ветки if оператора if-else
 */
@Getter
@Setter
public class N2oIfBranchAction extends N2oConditionBranch {
    private String datasourceId;
    private ReduxModelEnum model;
}
