package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRow;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.widget.table.cell.SwitchIO;
import org.jdom.Element;

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
        p.attributeBoolean(e, "last", page::getLast, page::setLast);
        p.attributeBoolean(e, "first", page::getFirst, page::setFirst);
        p.attributeBoolean(e, "show-count", page::getShowCount, page::setShowCount);
        p.attributeBoolean(e, "hide-single-page", page::getHideSinglePage, page::setHideSinglePage);
    }

    private void rowClick(Element e, N2oRowClick m, IOProcessor p) {
        p.attribute(e, "action-id", m::getActionId, m::setActionId);
        p.attribute(e, "enabled", m::getEnabled, m::setEnabled);
        p.anyChild(e, null, m::getAction, m::setAction, p.anyOf(N2oAction.class), ActionIOv1.NAMESPACE);
    }
}
