package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarResource;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarViewType;

import java.util.Map;

/**
 * Исходная модель виджета календарь
 */
@Getter
@Setter
public class N2oCalendar extends N2oWidget {
    private String height;
    private String defaultDate;
    private CalendarViewType defaultView;
    private String[] views;
    private String minTime;
    private String maxTime;
    private Boolean markDaysOff;
    private Boolean selectable;
    private Integer step;
    private Integer timeSlotCount;
    private String titleFieldId;
    private String tooltipFieldId;
    private String startFieldId;
    private String endFieldId;
    private String cellColorFieldId;
    private String disabledFieldId;
    private String resourcesFieldId;
    private CalendarResource[] resources;
    private N2oAbstractAction actionOnSelectSlot;
    private N2oAbstractAction actionOnSelectEvent;
    private Map<String, String> formats;
}
