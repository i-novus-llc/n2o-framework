package net.n2oapp.framework.api.metadata.action.ifelse;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;

/**
 * Исходная модель ветки условного оператора if-else
 */
@Getter
@Setter
public abstract class N2oConditionBranch extends N2oAbstractAction implements ActionsAware, ModelAware, DatasourceIdAware {
    private String test;
    private String actionId;
    private N2oAction[] actions;

    private String datasourceId;
    private ReduxModelEnum model;
}
