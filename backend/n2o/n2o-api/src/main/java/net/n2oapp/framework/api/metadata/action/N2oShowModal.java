package net.n2oapp.framework.api.metadata.action;


import lombok.Getter;
import lombok.Setter;

/**
 * Действие открытия модального окна
 */
@Getter
@Setter
public class N2oShowModal extends N2oAbstractPageAction {
    private ModalSizeEnum modalSize;
    private String backdrop;
    private Boolean scrollable;
    private Boolean hasHeader;
    private String className;
    private String style;
}
