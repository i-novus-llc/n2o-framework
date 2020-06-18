package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Ресурс календаря
 */
@Getter
@Setter
public class CalendarResource implements Serializable {
    private String resourceId;
    private String resourceTitle;
}
