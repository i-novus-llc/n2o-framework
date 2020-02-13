package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oLeftRightPage;
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
 * Чтение\запись страницы c двумя регионами версии 2.0
 */
@Component
public class LeftRightPageElementIOV2 implements NamespaceIO<N2oLeftRightPage> {
    private Namespace regionDefaultNamespace = RegionIOv1.NAMESPACE;
    private Namespace pageDefaultNamespace = PageIOv2.NAMESPACE;
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oLeftRightPage m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.anyChildren(e, "left", m::getLeft, m::setLeft, p.anyOf(N2oRegion.class), regionDefaultNamespace);
        p.childAttribute(e, "left", "width", m::getLeftWidth, m::setLeftWidth);
        p.anyChildren(e, "right", m::getRight, m::setRight, p.anyOf(N2oRegion.class), regionDefaultNamespace);
        p.childAttribute(e, "right", "width", m::getRightWidth, m::setRightWidth);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionsBar::new, this::action);
        p.childAttributeEnum(e, "actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void action(Element e, ActionsBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "widget-id", a::getWidgetId, a::setWidgetId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "hotkey", a::getHotkey, a::setHotkey);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChild(e, null, a::getAction, a::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public Class<N2oLeftRightPage> getElementClass() {
        return N2oLeftRightPage.class;
    }

    @Override
    public N2oLeftRightPage newInstance(Element element) {
        return new N2oLeftRightPage();
    }

    @Override
    public String getElementName() {
        return "left-right-page";
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }
}
