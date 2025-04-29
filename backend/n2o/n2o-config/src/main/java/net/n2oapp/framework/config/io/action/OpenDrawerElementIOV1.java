package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oOpenDrawer;
import net.n2oapp.framework.api.metadata.action.SubmitActionTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия старницы drawer
 */
@Component
public class OpenDrawerElementIOV1 extends AbstractOpenPageElementIOV1<N2oOpenDrawer> {
    @Override
    public void io(Element e, N2oOpenDrawer od, IOProcessor p) {
        super.io(e, od, p);
        p.attribute(e,"refresh-widget-id", od::getRefreshWidgetId, od::setRefreshWidgetId);
        p.attributeBoolean(e, "closable", od::getClosable, od::setClosable);
        p.attributeBoolean(e, "backdrop", od::getBackdrop, od::setBackdrop);
        p.attributeBoolean(e, "close-on-backdrop", od::getCloseOnBackdrop, od::setCloseOnBackdrop);
        p.attribute(e,"width", od::getWidth, od::setWidth);
        p.attribute(e,"height", od::getHeight, od::setHeight);
        p.attribute(e,"placement", od::getPlacement, od::setPlacement);
        p.attributeBoolean(e,"fixed-footer", od::getFixedFooter, od::setFixedFooter);
        p.attributeEnum(e, "submit-action-type", od::getSubmitActionType, od::setSubmitActionType, SubmitActionTypeEnum.class);
        p.attributeEnum(e, "copy-model", od::getCopyModel, od::setCopyModel, ReduxModelEnum.class);
        p.attribute(e, "copy-widget-id", od::getCopyWidgetId, od::setCopyWidgetId);
        p.attribute(e, "copy-field-id", od::getCopyFieldId, od::setCopyFieldId);
        p.attributeEnum(e, "target-model", od::getTargetModel, od::setTargetModel, ReduxModelEnum.class);
        p.attribute(e, "target-widget-id", od::getTargetWidgetId, od::setTargetWidgetId);
        p.attribute(e, "target-field-id", od::getTargetFieldId, od::setTargetFieldId);
        p.attributeEnum(e, "copy-mode", od::getCopyMode, od::setCopyMode, CopyModeEnum.class);
        p.attributeBoolean(e, "close-on-escape", od::getCloseOnEscape, od::setCloseOnEscape);
    }

    @Override
    public String getElementName() {
        return "open-drawer";
    }

    @Override
    public Class<N2oOpenDrawer> getElementClass() {
        return N2oOpenDrawer.class;
    }
}
