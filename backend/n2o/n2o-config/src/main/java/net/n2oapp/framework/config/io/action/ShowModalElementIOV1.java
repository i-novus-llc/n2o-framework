package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись лействия открытия модального окна
 */
@Component
public class ShowModalElementIOV1 extends AbstractOpenPageElementIOV1<N2oShowModal> {
    @Override
    public void io(Element e, N2oShowModal sm, IOProcessor p) {
        super.io(e, sm, p);
        p.attribute(e, "modal-size", sm::getModalSize, sm::setModalSize);
        p.attributeBoolean(e, "scrollable", sm::getScrollable, sm::setScrollable);
        p.attribute(e, "refresh-widget-id", sm::getRefreshWidgetId, sm::setRefreshWidgetId);
        p.attributeEnum(e, "submit-action-type", sm::getSubmitActionType, sm::setSubmitActionType, SubmitActionType.class);
        p.attributeEnum(e, "copy-model", sm::getCopyModel, sm::setCopyModel, ReduxModel.class);
        p.attribute(e, "copy-widget-id", sm::getCopyWidgetId, sm::setCopyWidgetId);
        p.attribute(e, "copy-field-id", sm::getCopyFieldId, sm::setCopyFieldId);
        p.attributeEnum(e, "target-model", sm::getTargetModel, sm::setTargetModel, ReduxModel.class);
        p.attribute(e, "target-widget-id", sm::getTargetWidgetId, sm::setTargetWidgetId);
        p.attribute(e, "target-field-id", sm::getTargetFieldId, sm::setTargetFieldId);
        p.attributeEnum(e, "copy-mode", sm::getCopyMode, sm::setCopyMode, CopyMode.class);
        p.attributeBoolean(e, "has-header", sm::getHasHeader, sm::setHasHeader);
        p.attribute(e, "class", sm::getClassName, sm::setClassName);
        p.attribute(e, "backdrop", sm::getBackdrop, sm::setBackdrop);
        p.attribute(e, "style", sm::getStyle, sm::setStyle);
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
