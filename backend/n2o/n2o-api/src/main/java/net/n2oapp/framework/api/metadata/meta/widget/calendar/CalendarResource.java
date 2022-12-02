package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Ресурс календаря
 */
@Getter
@Setter
public class CalendarResource implements Compiled {
    @JsonProperty
    private String id;
    @JsonProperty
    private String title;
}
