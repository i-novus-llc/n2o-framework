package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;

@Getter
@Setter
public abstract class N2oActionField extends N2oField implements ActionsAware {
    private String actionId;
    private N2oAction[] actions;
}
