package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.cell.v2.SwitchIO;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;

/**
 * Чтение\запись базовых свойств списковых виджетов
 */
public abstract class AbstractListWidgetElementIOv5<T extends N2oAbstractListWidget> extends WidgetElementIOv5<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.child(e, null, "pagination", m::getPagination, m::setPagination, N2oPagination::new, this::pagination);
        p.child(e, null, "rows", m::getRows, m::setRows, N2oRow::new, this::rows);
    }

    private void rows(Element e, N2oRow r, IOProcessor p) {
        p.attribute(e, "src", r::getSrc, r::setSrc);
        p.attribute(e, "class", r::getRowClass, r::setRowClass);
        p.attribute(e, "style", r::getStyle, r::setStyle);
        p.child(e, null, "click", r::getRowClick, r::setRowClick, N2oRowClick::new, this::rowClick);
        p.child(e, null, "switch", r::getColor, r::setColor, new SwitchIO());
        p.child(e, null, "overlay", r::getRowOverlay, r::setRowOverlay, N2oRowOverlay::new, this::rowOverlay);
    }

    private void pagination(Element e, N2oPagination page, IOProcessor p) {
        p.attribute(e, "src", page::getSrc, page::setSrc);
        p.attributeBoolean(e, "prev", page::getPrev, page::setPrev);
        p.attributeBoolean(e, "next", page::getNext, page::setNext);
        p.attributeEnum(e, "show-count", page::getShowCount, page::setShowCount, ShowCountTypeEnum.class);
        p.attributeBoolean(e, "show-last", page::getShowLast, page::setShowLast);
        p.attribute(e, "prev-label", page::getPrevLabel, page::setPrevLabel);
        p.attribute(e, "prev-icon", page::getPrevIcon, page::setPrevIcon);
        p.attribute(e, "next-label", page::getNextLabel, page::setNextLabel);
        p.attribute(e, "next-icon", page::getNextIcon, page::setNextIcon);
        p.attribute(e, "class", page::getClassName, page::setClassName);
        p.attribute(e, "style", page::getStyle, page::setStyle);
        p.attributeEnum(e, "place", page::getPlace, page::setPlace, PlaceEnum.class);
        p.attributeBoolean(e, "routable", page::getRoutable, page::setRoutable);
    }

    private void rowClick(Element e, N2oRowClick m, IOProcessor p) {
        ActionsAwareIO.action(e, m, null, ActionIOv2.NAMESPACE, p);
        p.attribute(e, "enabled", m::getEnabled, m::setEnabled);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void rowOverlay(Element e, N2oRowOverlay m, IOProcessor p) {
        p.attribute(e, "class", m::getClassName, m::setClassName);
        p.child(e, null, "toolbar", m::getToolbar, m::setToolbar, new ToolbarIOv2());
    }
}
