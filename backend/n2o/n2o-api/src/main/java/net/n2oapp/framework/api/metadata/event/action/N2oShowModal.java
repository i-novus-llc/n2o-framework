package net.n2oapp.framework.api.metadata.event.action;


import lombok.Getter;
import lombok.Setter;

/**
 * Действие открытия модального окна со страницей
 */
@Getter
@Setter
public class N2oShowModal extends N2oAbstractPageAction {
    private String modalSize;
    private ShowModalMode mode;
}
