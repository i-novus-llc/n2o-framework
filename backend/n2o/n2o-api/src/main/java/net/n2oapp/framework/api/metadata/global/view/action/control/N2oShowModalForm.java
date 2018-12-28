package net.n2oapp.framework.api.metadata.global.view.action.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;

/**
 * Действие открытия модального окна с формой
 */
@Getter
@Setter
public class N2oShowModalForm extends N2oAbstractPageAction {
    private String formId;
    private String modalSize;
}
