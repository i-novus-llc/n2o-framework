package net.n2oapp.framework.api.metadata.control.interval;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода интервала дат
 */
@Getter
@Setter
@VisualComponent
public class N2oDateInterval extends N2oSimpleIntervalField {
    @VisualAttribute
    private String dateFormat;
    @VisualAttribute
    private String timeFormat;
    @VisualAttribute
    private String max;
    @VisualAttribute
    private String min;
    @VisualAttribute
    private Boolean utc;
}
