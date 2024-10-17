package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oOnFailAction extends N2oAbstractAction {
    private N2oAction[] actions;
}
