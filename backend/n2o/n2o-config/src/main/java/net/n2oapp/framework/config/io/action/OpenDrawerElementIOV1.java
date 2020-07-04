package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.N2oOpenDrawer;
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
        p.attributeBoolean(e, "backdrop-closable", od::getBackdropClosable, od::setBackdropClosable);
        p.attribute(e,"width", od::getWidth, od::setWidth);
        p.attribute(e,"height", od::getHeight, od::setHeight);
        p.attribute(e,"placement", od::getPlacement, od::setPlacement);
        p.attribute(e,"level", od::getLevel, od::setLevel);
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
