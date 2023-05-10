package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.cell.v2.SwitchIO;
import org.jdom2.Element;

/**
 * Чтение\запись базовых свойств списковых виджетов
 */
public abstract class AbstractListWidgetElementIOv4<T extends N2oAbstractListWidget> extends WidgetElementIOv4<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.child(e, null, "pagination", m::getPagination, m::setPagination, N2oPagination::new, this::pagination);
        p.child(e, null, "rows", m::getRows, m::setRows, N2oRow::new, this::rows);
    }

    private void rows(Element e, N2oRow r, IOProcessor p) {
        p.attribute(e, "class", r::getRowClass, r::setRowClass);
        p.attribute(e, "style", r::getStyle, r::setStyle);
        p.child(e, null, "click", r::getRowClick, r::setRowClick, N2oRowClick::new, this::rowClick);
        p.child(e, null, "switch", r::getColor, r::setColor, new SwitchIO());
    }

    private void pagination(Element e, N2oPagination page, IOProcessor p) {
        p.attribute(e, "src", page::getSrc, page::setSrc);
        p.attributeBoolean(e, "prev", page::getPrev, page::setPrev);
        p.attributeBoolean(e, "next", page::getNext, page::setNext);
        p.read(e, page, (element, n2oPagination) -> {
            String attr = element.getAttributeValue("show-count");
            if ("true".equals(attr)) {
                page.setShowCount(ShowCountType.ALWAYS);
            }
            else if ("false".equals(attr)) {
                page.setShowCount(ShowCountType.NEVER);
            }
        });//deprecated
        p.attributeBoolean(e, "show-last", page::getShowLast, page::setShowLast);
        p.attribute(e, "prev-label", page::getPrevLabel, page::setPrevLabel);
        p.attribute(e, "prev-icon", page::getPrevIcon, page::setPrevIcon);
        p.attribute(e, "next-label", page::getNextLabel, page::setNextLabel);
        p.attribute(e, "next-icon", page::getNextIcon, page::setNextIcon);
        p.attribute(e, "class", page::getClassName, page::setClassName);
        p.attribute(e, "style", page::getStyle, page::setStyle);
        p.attributeEnum(e, "place", page::getPlace, page::setPlace, Place.class);
    }

    private void rowClick(Element e, N2oRowClick m, IOProcessor p) {
        p.attribute(e, "action-id", m::getActionId, m::setActionId);
        p.attribute(e, "enabled", m::getEnabled, m::setEnabled);
        p.anyChildren(e, null, m::getActions, m::setActions, p.anyOf(N2oAction.class), ActionIOv1.NAMESPACE);
    }
}
