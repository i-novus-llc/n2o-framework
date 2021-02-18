package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.Date;
import java.util.Map;

/**
 * Клиентская модель компонента календаря
 */
@Getter
@Setter
public class CalendarWidgetComponent extends WidgetComponent {
    @JsonProperty
    private Integer size;
    @JsonProperty
    private String height;
    @JsonProperty
    private Date date;
    @JsonProperty
    private String defaultView;
    @JsonProperty
    private String[] views;
    @JsonProperty
    private String minTime;
    @JsonProperty
    private String maxTime;
    @JsonProperty
    private Boolean markDaysOff;
    @JsonProperty
    private Boolean selectable;
    @JsonProperty
    private Integer step;
    @JsonProperty
    private Integer timeSlots;
    @JsonProperty
    private String titleFieldId;
    @JsonProperty
    private String tooltipFieldId;
    @JsonProperty
    private String startFieldId;
    @JsonProperty
    private String endFieldId;
    @JsonProperty
    private String cellColorFieldId;
    @JsonProperty
    private String disabledFieldId;
    @JsonProperty
    private String resourceFieldId;
    @JsonProperty
    private CalendarResource[] resources;
    @JsonProperty
    private Action onSelectSlot;
    @JsonProperty
    private Action onSelectEvent;
    @JsonProperty
    private Map<String, String> formats;
}
