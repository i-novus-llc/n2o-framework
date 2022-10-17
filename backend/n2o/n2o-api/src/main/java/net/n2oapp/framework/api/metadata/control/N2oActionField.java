package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.SourceActionsAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

@Getter
@Setter
public abstract class N2oActionField extends N2oField implements SourceActionsAware {
    private String actionId;
    private N2oAction[] actions;
}
