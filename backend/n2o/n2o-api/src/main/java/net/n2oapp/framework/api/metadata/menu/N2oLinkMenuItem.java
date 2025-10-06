package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;

/**
 * Исходная модель элемента меню {@code <link>}
 */
@Getter
@Setter
public class N2oLinkMenuItem extends N2oAbstractMenuItem {
    private String href;
    private TargetEnum target;
    private N2oParam[] pathParams;
    private N2oParam[] queryParams;
}