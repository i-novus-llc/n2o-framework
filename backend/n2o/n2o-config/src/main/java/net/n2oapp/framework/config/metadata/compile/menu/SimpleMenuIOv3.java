package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись меню 3.0
 */
@Component
public class SimpleMenuIOv3 implements NamespaceIO<N2oSimpleMenu> {

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getElementName() {
        return "menu";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/menu-3.0";
    }

    @Override
    public void io(Element e, N2oSimpleMenu m, IOProcessor p) {
        p.children(e, null, "menu-item", m::getMenuItems, m::setMenuItems,
                N2oSimpleMenu.MenuItem.class, this::menuItem);
        p.children(e, null, "dropdown-menu", m::getDropdownMenus, m::setDropdownMenus,
                N2oSimpleMenu.SubMenuItem.class, this::dropDownMenu);
    }

    private void menuItem(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getLabel, m::setLabel);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "badge-color", m::getBadgeColor, m::setBadgeColor);
        p.attributeInteger(e, "badge", m::getBadge, m::setBadge);
        p.child(e, null, "open-page", m::getOpenPage, m::setOpenPage, N2oOpenPage.class, this::openPage);
        p.child(e, null, "a", m::getAnchor, m::setAnchor, N2oAnchor.class, this::anchor);
    }

    private void anchor(Element e, N2oAnchor m, IOProcessor p) {
        p.attribute(e, "href", m::getHref, m::setHref);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, Target.class);
        p.children(e, null, "query-param", m::getQueryParams, m::setQueryParams, N2oParam.class, this::param);
        p.children(e, null, "path-param", m::getPathParams, m::setPathParams, N2oParam.class, this::param);
    }

    private void openPage(Element e, N2oOpenPage m, IOProcessor p) {
        p.attribute(e, "page-id", m::getPageId, m::setPageId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "page-name", m::getPageName, m::setPageName);
        p.attribute(e, "redirect-url-after-submit", m::getRedirectUrlAfterSubmit, m::setRedirectUrlAfterSubmit);
        p.attribute(e, "submit-label", m::getSubmitLabel, m::setSubmitLabel);
        p.attributeBoolean(e, "close-after-submit", m::getCloseAfterSubmit, m::setCloseAfterSubmit);
        p.attributeBoolean(e, "focus-after-submit", m::getFocusAfterSubmit, m::setFocusAfterSubmit);
        p.attribute(e, "submit-operation-id", m::getSubmitOperationId, m::setSubmitOperationId);
        p.attributeEnum(e, "upload", m::getUpload, m::setUpload, UploadType.class);
        p.attributeEnum(e, "submit-model", m::getSubmitModel, m::setSubmitModel, ReduxModel.class);
        p.attributeEnum(e, "redirect-target-after-submit", m::getRedirectTargetAfterSubmit,
                m::setRedirectTargetAfterSubmit, Target.class);
        p.children(e, null, "query-param", m::getQueryParams, m::setQueryParams, N2oParam.class, this::param);
        p.children(e, null, "path-param", m::getPathParams, m::setPathParams, N2oParam.class, this::param);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, "ref-widget-id", param::getRefWidgetId, param::setRefWidgetId);
        p.attributeEnum(e, "ref-model", param::getRefModel, param::setRefModel, ReduxModel.class);
    }

    private void dropDownMenu(Element e, N2oSimpleMenu.SubMenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.attributeEnum(e, "image-shape", m::getImageShape, m::setImageShape, ImageShape.class);
        p.children(e, null, "menu-item", m::getMenuItems, m::setMenuItems,
                N2oSimpleMenu.MenuItem.class, this::menuItem);
        p.children(e, null, "divider", m::getDivider, m::setDivider, N2oSimpleMenu.Divider.class,
                this::divider);
    }

    private void divider(Element e, N2oSimpleMenu.Divider m, IOProcessor p) {
    }
}
