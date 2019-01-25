package net.n2oapp.framework.config.io.page;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.region.RegionIOv1;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы  версии 2.0
 */
@Component
public class StandardPageElementIOv2 implements NamespaceIO<N2oStandardPage> {
    private Namespace regionDefaultNamespace = RegionIOv1.NAMESPACE;
    private Namespace pageDefaultNamespace = PageIOv2.NAMESPACE;
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oStandardPage m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "layout", m::getLayout, m::setLayout);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.anyChildren(e,"regions",m::getN2oRegions,m::setN2oRegions,p.anyOf(N2oRegion.class),regionDefaultNamespace);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionsBar::new, this::action);
        p.childAttributeEnum(e,"actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.extensionAttributes(e, m::getExtAttributes, m::setExtAttributes);
}

    private void action(Element e, ActionsBar a, IOProcessor p) {
        p.attribute(e,"id",a::getId,a::setId);
        p.attribute(e,"name",a::getLabel,a::setLabel);
        p.attribute(e,"widget-id",a::getWidgetId,a::setWidgetId);
        p.attributeEnum(e,"model",a::getModel,a::setModel, ReduxModel.class);
        p.attribute(e,"icon",a::getIcon,a::setIcon);
        p.attribute(e,"hotkey",a::getHotkey,a::setHotkey);
        p.attribute(e,"visible", a::getVisible,a::setVisible);
        p.attribute(e,"enabled", a::getEnabled, a::setEnabled);
        p.anyChild(e, null, a::getAction, a::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public Class<N2oStandardPage> getElementClass() {
        return N2oStandardPage.class;
    }

    @Override
    public N2oStandardPage newInstance(Element element) {
        return new N2oStandardPage();
    }

    @Override
    public String getElementName() {
        return "page";
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }

    public void setRegionDefaultNamespace(String regionDefaultNamespace) {
        this.regionDefaultNamespace = Namespace.getNamespace(regionDefaultNamespace);
    }


}
