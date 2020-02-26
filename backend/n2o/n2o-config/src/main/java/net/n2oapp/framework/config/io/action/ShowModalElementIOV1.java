package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.event.action.ShowModalMode;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись лействия открытия старницы поверх текущей
 */
@Component
public class ShowModalElementIOV1 extends AbstractOpenPageElementIOV1<N2oShowModal> {
    @Override
    public void io(Element e, N2oShowModal sm, IOProcessor p) {
        super.io(e, sm, p);
        p.attribute(e,  "modal-size", sm::getModalSize, sm::setModalSize);
        p.attribute(e,  "object-id", sm::getObjectId, sm::setObjectId);
        p.attribute(e,"refresh-widget-id", sm::getRefreshWidgetId, sm::setRefreshWidgetId);
        p.attributeEnum(e,  "type", sm::getType, sm::setType, ShowModalMode.class);
        p.attributeEnum(e, "submit-action", sm::getSubmitAction, sm::setSubmitAction, N2oShowModal.SubmitActionType.class);
        p.attributeEnum(e, "target-model", sm::getTargetModel, sm::setTargetModel, ReduxModel.class);
        p.attribute(e, "target-widget-id", sm::getTargetWidgetId, sm::setTargetWidgetId);
        p.attribute(e, "target-field-id", sm::getTargetFieldId, sm::setTargetFieldId);
        p.attributeEnum(e, "copy-mode", sm::getCopyMode, sm::setCopyMode, CopyMode.class);
    }

    @Override
    public String getElementName() {
        return "show-modal";
    }

    @Override
    public Class<N2oShowModal> getElementClass() {
        return N2oShowModal.class;
    }
}
