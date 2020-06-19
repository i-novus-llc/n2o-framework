package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCalendar;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarFormat;
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
        p.attributeBoolean(e, "toolbar", m::getToolbar, m::setToolbar);
        p.attribute(e, "min-date", m::getMinDate, m::setMinDate);
        p.attribute(e, "max-date", m::getMaxDate, m::setMaxDate);
        p.attributeBoolean(e, "mark-days-off", m::getMarkDaysOff, m::setMarkDaysOff);
        p.attributeBoolean(e, "selectable", m::getSelectable, m::setSelectable);
        p.attributeInteger(e, "step", m::getStep, m::setStep);
        p.attributeInteger(e, "timeslot-count", m::getTimeSlotCount, m::setTimeSlotCount);
        p.attribute(e, "title-field-id", m::getTitleFieldId, m::setTitleFieldId);
        p.attribute(e, "tooltip-field-id", m::getTooltipFieldId, m::setTooltipFieldId);
        p.attribute(e, "all-day-field-id", m::getAllDayFieldId, m::setAllDayFieldId);
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
        p.children(e, "formats", "format", m::getFormats, m::setFormats, CalendarFormat.class, this::format);
    }

    private void resource(Element e, CalendarResource r, IOProcessor p) {
        p.attribute(e, "id", r::getResourceId, r::setResourceId);
        p.attribute(e, "title", r::getResourceTitle, r::setResourceTitle);
    }

    private void format(Element e, CalendarFormat f, IOProcessor p) {
        p.attribute(e, "id", f::getId, f::setId);
        p.attribute(e, "value", f::getValue, f::setValue);
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
