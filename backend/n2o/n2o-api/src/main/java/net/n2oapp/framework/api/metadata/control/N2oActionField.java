package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

@Getter
@Setter
public class N2oActionField extends N2oField {
    private N2oAction action;
    private String actionId;
}
