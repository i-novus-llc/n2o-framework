package net.n2oapp.framework.api.metadata.event.action.ifelse;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

/**
 * Исходная модель ветки условного оператора if-else
 */
@Getter
@Setter
public abstract class N2oConditionBranch extends N2oAbstractAction implements ActionsAware {
    private String test;
    private String actionId;
    private N2oAction[] actions;
}
