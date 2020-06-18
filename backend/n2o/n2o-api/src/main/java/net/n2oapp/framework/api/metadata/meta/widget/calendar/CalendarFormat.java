package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Формат дат и времени календаря
 */
@Getter
@Setter
public class CalendarFormat implements Serializable {
    private String id;
    private String value;
}
