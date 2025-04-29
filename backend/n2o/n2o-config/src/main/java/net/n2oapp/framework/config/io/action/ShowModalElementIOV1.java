package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.ModalSizeEnum;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.action.SubmitActionTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
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
        p.attributeEnum(e, "modal-size", sm::getModalSize, sm::setModalSize, ModalSizeEnum.class);
        p.attributeBoolean(e, "scrollable", sm::getScrollable, sm::setScrollable);
        p.attribute(e, "refresh-widget-id", sm::getRefreshWidgetId, sm::setRefreshWidgetId);
        p.attributeEnum(e, "submit-action-type", sm::getSubmitActionType, sm::setSubmitActionType, SubmitActionTypeEnum.class);
        p.attributeEnum(e, "copy-model", sm::getCopyModel, sm::setCopyModel, ReduxModelEnum.class);
        p.attribute(e, "copy-widget-id", sm::getCopyWidgetId, sm::setCopyWidgetId);
        p.attribute(e, "copy-field-id", sm::getCopyFieldId, sm::setCopyFieldId);
        p.attributeEnum(e, "target-model", sm::getTargetModel, sm::setTargetModel, ReduxModelEnum.class);
        p.attribute(e, "target-widget-id", sm::getTargetWidgetId, sm::setTargetWidgetId);
        p.attribute(e, "target-field-id", sm::getTargetFieldId, sm::setTargetFieldId);
        p.attributeEnum(e, "copy-mode", sm::getCopyMode, sm::setCopyMode, CopyModeEnum.class);
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
