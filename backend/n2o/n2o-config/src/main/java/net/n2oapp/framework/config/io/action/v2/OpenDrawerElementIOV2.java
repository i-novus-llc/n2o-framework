package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenDrawer;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия страницы drawer
 */
@Component
public class OpenDrawerElementIOV2 extends AbstractOpenPageElementIOV2<N2oOpenDrawer> {
    @Override
    public void io(Element e, N2oOpenDrawer od, IOProcessor p) {
        super.io(e, od, p);
        p.attributeArray(e,"refresh-datasources", ",", od::getRefreshDatasources, od::setRefreshDatasources);
        p.attributeBoolean(e, "closable", od::getClosable, od::setClosable);
        p.attributeBoolean(e, "backdrop", od::getBackdrop, od::setBackdrop);
        p.attributeBoolean(e, "close-on-backdrop", od::getCloseOnBackdrop, od::setCloseOnBackdrop);
        p.attribute(e,"width", od::getWidth, od::setWidth);
        p.attribute(e,"height", od::getHeight, od::setHeight);
        p.attribute(e,"placement", od::getPlacement, od::setPlacement);
        p.attribute(e,"level", od::getLevel, od::setLevel);
        p.attributeBoolean(e,"fixed-footer", od::getFixedFooter, od::setFixedFooter);
        p.attributeEnum(e, "submit-action-type", od::getSubmitActionType, od::setSubmitActionType, SubmitActionType.class);
        p.attributeEnum(e, "copy-model", od::getCopyModel, od::setCopyModel, ReduxModel.class);
        p.attribute(e, "copy-datasource", od::getCopyDatasource, od::setCopyDatasource);
        p.attribute(e, "copy-field-id", od::getCopyFieldId, od::setCopyFieldId);
        p.attributeEnum(e, "target-model", od::getTargetModel, od::setTargetModel, ReduxModel.class);
        p.attribute(e, "target-datasource", od::getTargetDatasource, od::setTargetDatasource);
        p.attribute(e, "target-field-id", od::getTargetFieldId, od::setTargetFieldId);
        p.attributeEnum(e, "copy-mode", od::getCopyMode, od::setCopyMode, CopyMode.class);
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
