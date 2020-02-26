package net.n2oapp.framework.api.metadata.event.action;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;

/**
 * Действие открытия модального окна со страницей
 */
@Getter
@Setter
public class N2oShowModal extends N2oAbstractPageAction {
    private String modalSize;
    private ShowModalMode type;
    private SubmitActionType submitAction;
    private ReduxModel targetModel;
    private String targetWidgetId;
    private String targetFieldId;
    private CopyMode copyMode;

    public enum SubmitActionType {
        invoke,
        copy
    }
}
