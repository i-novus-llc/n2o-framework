package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

/**
 * Клиентская модель календаря
 */
@Getter
@Setter
public class Calendar extends Widget<CalendarWidgetComponent> {

    public Calendar() {
        super(new CalendarWidgetComponent());
    }

    @JsonProperty("calendar")
    @Override
    public CalendarWidgetComponent getComponent() {
        return super.getComponent();
    }
}
