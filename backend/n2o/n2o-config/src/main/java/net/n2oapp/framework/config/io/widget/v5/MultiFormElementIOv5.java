package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oMultiForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.PlaceEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import net.n2oapp.framework.config.io.fieldset.v5.FieldsetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись виджета MultiForm версии 5.0
 */
@Component
public class MultiFormElementIOv5 extends WidgetElementIOv5<N2oMultiForm> {

    @Override
    public void io(Element e, N2oMultiForm m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "unsaved-data-prompt", m::getUnsavedDataPrompt, m::setUnsavedDataPrompt);
        p.child(e, null, "fields", m::getForm, m::setForm, N2oForm::new, this::form);
        p.child(e, null, "pagination", m::getPagination, m::setPagination, N2oPagination::new, this::pagination);
        p.merge(m, getElementName());
    }

    private void form(Element e, N2oForm f, IOProcessor p) {
        p.anyChildren(e, null, f::getItems, f::setItems, p.anyOf(SourceComponent.class),
                FieldsetIOv5.NAMESPACE, ControlIOv3.NAMESPACE);
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

    @Override
    public String getElementName() {
        return "multi-form";
    }

    @Override
    public Class<N2oMultiForm> getElementClass() {
        return N2oMultiForm.class;
    }
}