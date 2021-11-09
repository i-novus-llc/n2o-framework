package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCalendar;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarResource;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarViewType;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета календарь
 */
@Component
public class CalendarWidgetIOv4 extends WidgetElementIOv4<N2oCalendar> {

    @Override
    public void io(Element e, N2oCalendar m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "height", m::getHeight, m::setHeight);
        p.attribute(e, "default-date", m::getDefaultDate, m::setDefaultDate);
        p.attributeEnum(e, "default-view", m::getDefaultView, m::setDefaultView, CalendarViewType.class);
        p.attributeArray(e, "views", ",", m::getViews, m::setViews);
        p.attribute(e, "min-time", m::getMinTime, m::setMinTime);
        p.attribute(e, "max-time", m::getMaxTime, m::setMaxTime);
        p.attributeBoolean(e, "mark-days-off", m::getMarkDaysOff, m::setMarkDaysOff);
        p.attributeBoolean(e, "selectable", m::getSelectable, m::setSelectable);
        p.attributeInteger(e, "step", m::getStep, m::setStep);
        p.attributeInteger(e, "timeslot-count", m::getTimeSlotCount, m::setTimeSlotCount);
        p.attribute(e, "title-field-id", m::getTitleFieldId, m::setTitleFieldId);
        p.attribute(e, "tooltip-field-id", m::getTooltipFieldId, m::setTooltipFieldId);
        p.attribute(e, "start-field-id", m::getStartFieldId, m::setStartFieldId);
        p.attribute(e, "end-field-id", m::getEndFieldId, m::setEndFieldId);
        p.attribute(e, "cell-color-field-id", m::getCellColorFieldId, m::setCellColorFieldId);
        p.attribute(e, "disabled-field-id", m::getDisabledFieldId, m::setDisabledFieldId);
        p.children(e, "resources", "resource", m::getResources, m::setResources, CalendarResource.class, this::resource);
        p.childAttribute(e, "resources", "value-field-id", m::getResourcesFieldId, m::setResourcesFieldId);
        p.anyChild(e, "action-on-select-slot", m::getActionOnSelectSlot, m::setActionOnSelectSlot,
                p.anyOf(N2oAbstractAction.class), ActionIOv1.NAMESPACE);
        p.anyChild(e, "action-on-select-event", m::getActionOnSelectEvent, m::setActionOnSelectEvent,
                p.anyOf(N2oAbstractAction.class), ActionIOv1.NAMESPACE);
        p.childrenToStringMap(e, "formats", "format", "id", "value", m::getFormats, m::setFormats);
    }

    private void resource(Element e, CalendarResource r, IOProcessor p) {
        p.attribute(e, "id", r::getId, r::setId);
        p.attribute(e, "title", r::getTitle, r::setTitle);
    }

    @Override
    public Class<N2oCalendar> getElementClass() {
        return N2oCalendar.class;
    }

    @Override
    public String getElementName() {
        return "calendar";
    }
}
