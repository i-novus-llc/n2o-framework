package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Событие закрытия страницы
 */
@Getter
@Setter
public class N2oCloseAction extends N2oAbstractAction implements N2oAction {
    private String redirectUrl;
    private Target redirectTarget;
    private Boolean prompt;
    private Boolean refresh;
}
