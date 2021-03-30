package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Исходная модель ccылки
 */
@Getter
@Setter
public class N2oAnchor extends N2oAbstractAction implements N2oAction {
    private String href;
    private Target target;
    private N2oParam[] pathParams;
    private N2oParam[] queryParams;

    public N2oAnchor() {
    }

    public N2oAnchor(String href) {
        this.href = href;
    }
}
