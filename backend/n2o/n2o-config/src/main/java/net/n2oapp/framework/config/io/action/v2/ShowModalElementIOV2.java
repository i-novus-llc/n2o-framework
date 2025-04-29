package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.ModalSizeEnum;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.action.SubmitActionTypeEnum;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись лействия открытия модального окна версии 2.0
 */
@Component
public class ShowModalElementIOV2 extends AbstractOpenPageElementIOV2<N2oShowModal> {
    @Override
    public void io(Element e, N2oShowModal sm, IOProcessor p) {
        super.io(e, sm, p);
        p.attributeEnum(e, "modal-size", sm::getModalSize, sm::setModalSize, ModalSizeEnum.class);
        p.attribute(e, "backdrop", sm::getBackdrop, sm::setBackdrop);
        p.attributeBoolean(e, "scrollable", sm::getScrollable, sm::setScrollable);
        p.attributeBoolean(e, "has-header", sm::getHasHeader, sm::setHasHeader);
        p.attribute(e, "class", sm::getClassName, sm::setClassName);
        p.attribute(e, "style", sm::getStyle, sm::setStyle);
        p.attributeEnum(e, "submit-action-type", sm::getSubmitActionType, sm::setSubmitActionType, SubmitActionTypeEnum.class);
        p.attributeEnum(e, "copy-model", sm::getCopyModel, sm::setCopyModel, ReduxModelEnum.class);
        p.attribute(e, "copy-datasource", sm::getCopyDatasourceId, sm::setCopyDatasourceId);
        p.attribute(e, "copy-field-id", sm::getCopyFieldId, sm::setCopyFieldId);
        p.attributeEnum(e, "target-model", sm::getTargetModel, sm::setTargetModel, ReduxModelEnum.class);
        p.attribute(e, "target-datasource", sm::getTargetDatasourceId, sm::setTargetDatasourceId);
        p.attribute(e, "target-field-id", sm::getTargetFieldId, sm::setTargetFieldId);
        p.attributeEnum(e, "target-page", sm::getTargetPage, sm::setTargetPage, PageRefEnum.class);
        p.attributeEnum(e, "copy-mode", sm::getCopyMode, sm::setCopyMode, CopyModeEnum.class);
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
