package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода даты
 */
@Getter
@Setter
@VisualComponent
public class N2oDatePicker extends N2oPlainField {
    @VisualAttribute
    private String dateFormat;
    @VisualAttribute
    private String timeFormat;
    @VisualAttribute
    private String defaultTime;
    @VisualAttribute
    private String max;
    @VisualAttribute
    private String min;
    @VisualAttribute
    private Boolean utc;
}
