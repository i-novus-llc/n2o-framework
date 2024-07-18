package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oMultiAction extends N2oAbstractAction {
    N2oAction[] n2oActions;
}
