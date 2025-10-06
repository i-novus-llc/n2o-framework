package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNavRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.*;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Чтение/запись региона {@code <nav>} версии 3.0
 */
@Component
public class NavRegionIOv3 extends AbstractRegionIOv3<N2oNavRegion> implements BadgeAwareIO<N2oMenuItem> {

    private static final String DATASOURCE = "datasource";
    private static final String MODEL = "model";

    @Override
    public void io(Element e, N2oNavRegion m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "direction", m::getDirection, m::setDirection, N2oNavRegion.DirectionTypeEnum.class);
        p.attribute(e, DATASOURCE, m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, MODEL, m::getModel, m::setModel, ReduxModelEnum.class);
        subItems(e, p, m::getMenuItems, m::setMenuItems);
    }

    private void subItems(Element e, IOProcessor p, Supplier<N2oAbstractMenuItem[]> getMenuItems, Consumer<N2oAbstractMenuItem[]> setMenuItems) {
        p.anyChildren(e, null, getMenuItems, setMenuItems, p.oneOf(N2oAbstractMenuItem.class)
                .add("menu-item", N2oMenuItem.class, this::menuItem)
                .add("link", N2oLinkMenuItem.class, this::linkMenuItem)
                .add("button", N2oButtonMenuItem.class, this::buttonMenuItem)
                .add("divider", N2oDividerMenuItem.class, this::dividerMenuItem)
                .add("dropdown-menu", N2oDropdownMenuItem.class, this::dropdownMenuItem)
                .add("group", N2oGroupMenuItem.class, this::groupMenuItem)
        );
    }

    private void abstractMenuItem(Element e, N2oAbstractMenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeEnum(e, "icon-position", m::getIconPosition, m::setIconPosition, PositionEnum.class);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.attributeEnum(e, "image-shape", m::getImageShape, m::setImageShape, ShapeTypeEnum.class);
        p.attribute(e, DATASOURCE, m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, MODEL, m::getModel, m::setModel, ReduxModelEnum.class);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "enabled", m::getEnabled, m::setEnabled);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void menuItem(Element e, N2oMenuItem m, IOProcessor p) {
        abstractMenuItem(e, m, p);
        badge(e, m, p);
        p.attribute(e, "action-id",  m::getActionId, m::setActionId);
        p.anyChildren(e, null, m::getActions, m::setActions, p.anyOf(N2oAction.class), ActionIOv2.NAMESPACE);
    }

    private void buttonMenuItem(Element e, N2oButtonMenuItem m, IOProcessor p) {
        menuItem(e, m, p);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "description", m::getDescription, m::setDescription);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);
    }

    private void dividerMenuItem(Element e, N2oDividerMenuItem m, IOProcessor p) {
        abstractMenuItem(e, m, p);
    }

    private void linkMenuItem(Element e, N2oLinkMenuItem m, IOProcessor p) {
        abstractMenuItem(e, m, p);
        p.attribute(e, "href", m::getHref, m::setHref);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, TargetEnum.class);
        p.children(e, null, "path-param", m::getPathParams, m::setPathParams, N2oParam::new, this::param);
        p.children(e, null, "query-param", m::getQueryParams, m::setQueryParams, N2oParam::new, this::param);
    }

    private void dropdownMenuItem(Element e, N2oDropdownMenuItem m, IOProcessor p) {
        abstractMenuItem(e, m, p);
        p.attributeEnum(e, "trigger", m::getTrigger, m::setTrigger, TriggerEnum.class);
        p.attributeEnum(e, "position", m::getPosition, m::setPosition, N2oDropdownMenuItem.PositionTypeEnum.class);
        subItems(e, p, m::getMenuItems, m::setMenuItems);
    }

    private void groupMenuItem(Element e, N2oGroupMenuItem m, IOProcessor p) {
        abstractMenuItem(e, m, p);
        p.attributeBoolean(e, "collapsible", m::getCollapsible, m::setCollapsible);
        p.attributeEnum(e, "default-state", m::getDefaultState, m::setDefaultState, N2oGroupMenuItem.GroupStateTypeEnum.class);
        subItems(e, p, m::getMenuItems, m::setMenuItems);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, DATASOURCE, param::getDatasourceId, param::setDatasourceId);
        p.attributeEnum(e, MODEL, param::getModel, param::setModel, ReduxModelEnum.class);
    }

    @Override
    public String getElementName() {
        return "nav";
    }

    @Override
    public Class<N2oNavRegion> getElementClass() {
        return N2oNavRegion.class;
    }
}
